package com.christiancriollo.orders.domain.model;
import com.christiancriollo.orders.domain.exception.BusinessException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa un pedido.
 */

public class Order {
    private final UUID id;
    private final UUID customerId;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(UUID id, UUID customerId, List<OrderItem> items) {

        if (id == null) {
            throw new BusinessException("Id orden es requerido.");
        }

        if (customerId == null) {
            throw new BusinessException("Id cliente es requerido.");
        }

        if (items == null || items.isEmpty()) {
            throw new BusinessException("La orden debe contener al menos un item.");
        }

        this.id = id;
        this.customerId = customerId;
        this.items = new ArrayList<>(items);
        this.status = OrderStatus.PENDING;
    }

    public void confirm() {

        if (status == OrderStatus.CONFIRMED) {
            throw new BusinessException("La orden ha sido confirmada.");
        }

        if (status == OrderStatus.CANCELLED) {
            throw new BusinessException("Una orden cancelada no puede ser confirmada.");
        }

        status = OrderStatus.CONFIRMED;
    }

    public void cancel() {

        if (status == OrderStatus.CONFIRMED) {
            throw new BusinessException("Una orden confirmada no puede ser cancelada.");
        }

        if (status == OrderStatus.CANCELLED) {
            throw new BusinessException("La orden ha sido cancelada.");
        }

        status = OrderStatus.CANCELLED;
    }

    public BigDecimal calculateTotal() {

        return items.stream()
                .map(OrderItem::calculateSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public OrderStatus getStatus() {
        return status;
    }

}
