package com.christiancriollo.orders.domain.repository;
import com.christiancriollo.orders.domain.model.Order;
import java.util.Optional;
import java.util.UUID;

/**
 * Contrato para la persistencia de pedidos.
 */

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(UUID id);
}
