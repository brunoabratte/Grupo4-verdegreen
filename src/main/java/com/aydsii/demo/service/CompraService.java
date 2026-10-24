package com.aydsii.demo.service;

import com.aydsii.demo.dto.CompraRequestDTO;
import com.aydsii.demo.dto.CompraResponseDTO;
import com.aydsii.demo.exception.PagoRechazadoException;
import com.aydsii.demo.exception.StockInsuficienteException;
import com.aydsii.demo.model.*;
import com.aydsii.demo.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompraService {
    private final CarritoRepository carritoRepository;
    private final DetalleCarritoRepository detalleCarritoRepository;
    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final PromocionRepository promocionRepository;
    private final PagoRepository pagoRepository;
    private final PasarelaPago pasarelaPago;
    private final EntityManager entityManager;

    // Repositorios de detalle/producto/promocion/pago: necesarios para tus mappings JPA
    // porque Carrito y Pedido no tienen colecciones y Pago referencia a Pedido.
    public CompraService(CarritoRepository carritoRepository,
                         DetalleCarritoRepository detalleCarritoRepository,
                         PedidoRepository pedidoRepository,
                         DetallePedidoRepository detallePedidoRepository,
                         PromocionRepository promocionRepository,
                         PagoRepository pagoRepository,
                         PasarelaPago pasarelaPago,
                         EntityManager entityManager) {
        this.carritoRepository = carritoRepository;
        this.detalleCarritoRepository = detalleCarritoRepository;
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.promocionRepository = promocionRepository;
        this.pagoRepository = pagoRepository;
        this.pasarelaPago = pasarelaPago;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Map<String,Object> obtenerResumenCompra(Long clienteID) {
        Carrito carrito = obtenerCarrito(clienteID);
        comprobarClienteAutenticado(carrito);
        List<DetalleCarrito> items = obtenerItems(carrito);
        validarStock(items);
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        List<Map<String,Object>> lineas = new ArrayList<>();
        LocalDate hoy = LocalDate.now();
        for (DetalleCarrito item : items) {
            Producto producto = item.getProducto();
            BigDecimal descuento = porcentajeMejorPromocion(producto.getCodigo(), hoy);
            BigDecimal subtotal = calcularSubtotal(producto.getPrecio(), item.getCantidad(), descuento);
            Map<String,Object> linea = new LinkedHashMap<>();
            linea.put("codigo",producto.getCodigo());
            linea.put("nombre",producto.getNombre());
            linea.put("cantidad",item.getCantidad());
            linea.put("precioUnitario",producto.getPrecio());
            linea.put("porcentajeAplicado",descuento);
            linea.put("subtotal",subtotal);
            lineas.add(linea);
            total = total.add(subtotal);
        }
        Map<String,Object> resumen = new LinkedHashMap<>();
        resumen.put("clienteID", clienteID);
        resumen.put("items", lineas);
        resumen.put("total", total);
        return resumen;
    }

    @Transactional
    public CompraResponseDTO realizarCompra(CompraRequestDTO request) {
        if (request.getMetodoPago() == null) throw new IllegalArgumentException("Elegí método de pago");
        validarEntrega(request);
        Carrito carrito = obtenerCarrito(request.getClienteID());
        comprobarClienteAutenticado(carrito);
        // Leemos solo los codigos del carrito ANTES de cargar sus productos.
        // Así el primer acceso al stock se hace bajo bloqueo en la BD.
        List<Long> ids = detalleCarritoRepository.obtenerCodigosProducto(carrito.getId_carrito())
            .stream().distinct().sorted().toList();
        if (ids.isEmpty()) throw new IllegalArgumentException("Carrito vacio");
        List<Producto> bloqueados = bloquearProductos(ids);
        List<DetalleCarrito> items = obtenerItems(carrito);
        Map<Long,Producto> productos = bloqueados.stream()
            .collect(Collectors.toMap(Producto::getCodigo, p -> p));
        if (productos.size() != ids.size()) throw new StockInsuficienteException(List.of("Producto eliminado"));
        validarStock(items, productos);

        LocalDate hoy = LocalDate.now();
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        List<DetallePedido> detalles = new ArrayList<>();
        for (DetalleCarrito item : items) {
            Producto producto = productos.get(item.getProducto().getCodigo());
            BigDecimal descuento = porcentajeMejorPromocion(producto.getCodigo(), hoy);
            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setPorcentajeAplicado(descuento);
            total = total.add(calcularSubtotal(producto.getPrecio(), item.getCantidad(), descuento));
            detalles.add(detalle);
        }
        if (total.signum() <= 0) throw new IllegalArgumentException("Total de compra invalido");

        // Tu Pedido NO tiene setPago() ni agregarDetalle(): se guardan por separado.
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now());
        pedido.setCliente(carrito.getCliente());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setModalidadEntrega(request.getModalidadEntrega());
        pedido.setDireccionEnvio(request.getModalidadEntrega() == ModalidadEntrega.ENVIO
            ? request.getDireccionEnvio().trim() : null);
        pedido.setTotal(total);
        pedidoRepository.saveAndFlush(pedido); // necesitamos el Long id_pedido para @MapsId

        // Las claves compuestas son (id_pedido, codigo_producto), no autonumericas.
        for (DetallePedido detalle : detalles) {
            detalle.setPedido(pedido);
            detalle.setId(new DetallePedidoId(pedido.getId_pedido(), detalle.getProducto().getCodigo()));
        }
        detallePedidoRepository.saveAll(detalles);

        Pago pago = new Pago();
        pago.setPedido(pedido); // Pago es el propietario de la relacion (@ManyToOne)
        pago.setMetodoPago(request.getMetodoPago());
        pago.setMonto(total);
        pago.setFechaGeneracion(LocalDateTime.now());
        pago.setEstado(EstadoPago.PENDIENTE);

        if (request.getMetodoPago() == MetodoPago.MERCADO_PAGO) {
            // Integración SIMULADA: si falla lanzamos exception y @Transactional
            // revierte el Pedido, sus detalles y cualquier cambio de stock.
            String respuesta = pasarelaPago.solicitarPago(total, pedido.getId_pedido());
            if (!"APROBADO".equalsIgnoreCase(respuesta)) {
                throw new PagoRechazadoException("Mercado Pago rechazó el pago; conservamos el carrito");
            }
            pago.setEstado(EstadoPago.CONFIRMADO);
            pedido.setEstado(EstadoPedido.APROBADO);
        } else if (request.getMetodoPago() == MetodoPago.TRANSFERENCIA) {
            pago.setEstado(EstadoPago.PENDIENTE);
            pago.setFechaVencimiento(LocalDateTime.now().plusHours(72));
        } else {
            throw new IllegalArgumentException("Metodo de pago no reconocido");
        }
        pagoRepository.save(pago);

        // Registrar la compra descuenta stock (RN-07) y vacía el carrito.
        for (DetalleCarrito item : items) {
            Producto p = productos.get(item.getProducto().getCodigo());
            p.setStock(p.getStock() - item.getCantidad());
        }
        detalleCarritoRepository.deleteAll(items);
        return new CompraResponseDTO(pedido.getId_pedido(), pedido.getEstado(), pedido.getTotal());
    }

    /**
     * Bloquea los productos dentro de la transacción de realizarCompra.
     * No requiere agregar métodos a ProductoRepository.
     * Recibe IDs ordenados para reducir el riesgo de interbloqueos.
     */
    private List<Producto> bloquearProductos(List<Long> ids) {
        List<Producto> productos = new ArrayList<>();
        for (Long id : ids) {
            Producto producto = entityManager.find(
                Producto.class, id, LockModeType.PESSIMISTIC_WRITE
            );
            if (producto != null) {
                productos.add(producto);
            }
        }
        return productos;
    }

    private Carrito obtenerCarrito(Long clienteID) {
        if (clienteID == null) throw new IllegalArgumentException("Falta clienteID");
        return carritoRepository.findByClienteIdCliente(clienteID)
            .orElseThrow(() -> new NoSuchElementException("El cliente no tiene carrito"));
    }

    private List<DetalleCarrito> obtenerItems(Carrito carrito) {
        List<DetalleCarrito> items = detalleCarritoRepository.obtenerItems(carrito.getId_carrito());
        if (items.isEmpty()) throw new IllegalArgumentException("Carrito vacio");
        return items;
    }

    private void validarStock(List<DetalleCarrito> items) {
        validarStock(items, items.stream().map(DetalleCarrito::getProducto)
            .collect(Collectors.toMap(Producto::getCodigo, p -> p)));
    }

    private void validarStock(List<DetalleCarrito> items, Map<Long,Producto> productos) {
        Map<Long,Integer> cantidades = new HashMap<>();
        for (DetalleCarrito item : items) {
            if (item.getCantidad() == null || item.getCantidad() <= 0)
                throw new IllegalArgumentException("Cantidad de carrito invalida");
            cantidades.merge(item.getProducto().getCodigo(),item.getCantidad(),Integer::sum);
        }
        List<String> faltantes = new ArrayList<>();
        for (var entrada : cantidades.entrySet()) {
            Producto producto = productos.get(entrada.getKey());
            if (producto == null || !Boolean.TRUE.equals(producto.getActivo()) ||
                producto.getStock() == null || producto.getStock() < entrada.getValue()) {
                faltantes.add(producto == null ? "Producto eliminado" : producto.getNombre());
            }
        }
        if (!faltantes.isEmpty()) throw new StockInsuficienteException(faltantes);
    }

    private BigDecimal porcentajeMejorPromocion(Long codigo, LocalDate fecha) {
        return promocionRepository.buscarVigente(codigo,fecha).stream()
            .max(Comparator.comparing(Promocion::getPorcentajeDescuento))
            .map(Promocion::getPorcentajeDescuento)
            .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calcularSubtotal(BigDecimal precio, int cantidad, BigDecimal porcentaje) {
        return precio.multiply(BigDecimal.valueOf(cantidad))
            .multiply(BigDecimal.ONE.subtract(porcentaje.movePointLeft(2)))
            .setScale(2, RoundingMode.HALF_UP);
    }

    private void comprobarClienteAutenticado(Carrito carrito) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() ||
            !carrito.getCliente().getEmail().equalsIgnoreCase(auth.getName())) {
            throw new AccessDeniedException("No podes comprar con el carrito de otro cliente");
        }
    }

    private void validarEntrega(CompraRequestDTO request) {
        if (request.getModalidadEntrega() == null)
            throw new IllegalArgumentException("Elegí modalidad de entrega");
        if (request.getModalidadEntrega() == ModalidadEntrega.ENVIO &&
            (request.getDireccionEnvio() == null || request.getDireccionEnvio().isBlank())) {
            throw new IllegalArgumentException("Para envio hace falta la direccion");
        }
    }
}

