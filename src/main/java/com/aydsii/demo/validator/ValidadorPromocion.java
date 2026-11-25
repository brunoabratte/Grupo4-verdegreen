package com.aydsii.demo.validator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aydsii.demo.dto.PromocionRequestDTO;
import com.aydsii.demo.exception.PromocionInvalidaException;

/**
 * PATRÓN SINGLETON (CU-03 Cargar Promoción).
 *
 * Intención (teoría): garantizar que la clase tenga UNA sola instancia y dar un punto de
 * acceso global a ella.
 *
 * Por qué acá: las reglas de validación de una promoción (FE-01, FE-02, FE-03) no tienen
 * estado, son las mismas para todos los pedidos y no hace falta más de un objeto que las
 * conozca. Tener una única instancia evita crear un validador por cada request.
 *
 * Cómo se implementa (igual que el diagrama de la teoría):
 *   - constructor PRIVADO: nadie puede hacer "new ValidadorPromocion()".
 *   - atributo estático privado "instancia": la clase lleva el registro de su única instancia.
 *   - método estático público obtenerInstancia(): crea la instancia la primera vez
 *     (if instancia == null) y después siempre devuelve la misma.
 *   - "synchronized" porque Tomcat atiende varios requests en hilos distintos y, sin eso,
 *     dos hilos podrían crear dos instancias a la vez.
 *
 * Uso:  ValidadorPromocion.obtenerInstancia().validar(datos);
 */
public final class ValidadorPromocion {

    // Simple Logging Facade: se depende de la API de SLF4J, no de Log4j2 directamente.
    private static final Logger log = LoggerFactory.getLogger(ValidadorPromocion.class);

    private static final BigDecimal PORCENTAJE_MAXIMO = BigDecimal.valueOf(100);

    /** La única instancia de la clase. */
    private static ValidadorPromocion instancia;

    private ValidadorPromocion() {
        log.debug("Se crea la única instancia de ValidadorPromocion");
    }

    public static synchronized ValidadorPromocion obtenerInstancia() {
        if (instancia == null) {
            instancia = new ValidadorPromocion();
        }
        return instancia;
    }

    /** Valida los datos con la fecha de hoy. */
    public void validar(PromocionRequestDTO datos) {
        validar(datos, LocalDate.now());
    }

    /** Igual que validar(datos), pero con la fecha de "hoy" como parámetro (permite probarlo). */
    public void validar(PromocionRequestDTO datos, LocalDate hoy) {
        log.debug("Validando datos de la promoción '{}'", datos.getNombre());
        validarObligatorios(datos);     // FE-01
        validarPorcentaje(datos);       // FE-02
        validarFechas(datos, hoy);      // FE-03
    }

    // FE-01: falta el nombre, el porcentaje, alguna fecha, o no se indicó ningún producto
    private void validarObligatorios(PromocionRequestDTO datos) {
        List<String> faltantes = new ArrayList<>();
        if (datos.getNombre() == null || datos.getNombre().isBlank()) faltantes.add("nombre");
        if (datos.getPorcentajeDescuento() == null) faltantes.add("porcentajeDescuento");
        if (datos.getFechaInicio() == null) faltantes.add("fechaInicio");
        if (datos.getFechaFin() == null) faltantes.add("fechaFin");
        if (datos.getCodigosProductos() == null
                || datos.getCodigosProductos().stream().noneMatch(Objects::nonNull)) {
            faltantes.add("codigosProductos");
        }
        if (!faltantes.isEmpty()) {
            log.warn("Promoción rechazada (FE-01): faltan datos obligatorios {}", faltantes);
            throw new PromocionInvalidaException("Faltan datos obligatorios: " + String.join(", ", faltantes));
        }
    }

    // FE-02: el porcentaje es <= 0 o > 100
    private void validarPorcentaje(PromocionRequestDTO datos) {
        BigDecimal porcentaje = datos.getPorcentajeDescuento();
        if (porcentaje.signum() <= 0 || porcentaje.compareTo(PORCENTAJE_MAXIMO) > 0) {
            log.warn("Promoción rechazada (FE-02): porcentaje inválido {}", porcentaje);
            throw new PromocionInvalidaException(
                "El porcentaje de descuento debe ser mayor a 0 y menor o igual a 100");
        }
    }

    // FE-03: fin anterior al inicio, o fin que ya pasó
    private void validarFechas(PromocionRequestDTO datos, LocalDate hoy) {
        if (datos.getFechaFin().isBefore(datos.getFechaInicio())) {
            log.warn("Promoción rechazada (FE-03): fin {} anterior al inicio {}",
                datos.getFechaFin(), datos.getFechaInicio());
            throw new PromocionInvalidaException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        if (datos.getFechaFin().isBefore(hoy)) {
            log.warn("Promoción rechazada (FE-03): la fecha de fin {} ya pasó", datos.getFechaFin());
            throw new PromocionInvalidaException("La fecha de fin ya pasó");
        }
    }
}
