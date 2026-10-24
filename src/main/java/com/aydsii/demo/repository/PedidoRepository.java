package com.aydsii.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aydsii.demo.model.Pedido;

@Repository 
public interface PedidoRepository extends JpaRepository<Pedido, Long>{ //JPA ya implementa save()

}
