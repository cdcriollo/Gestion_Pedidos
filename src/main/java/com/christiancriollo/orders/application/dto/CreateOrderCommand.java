package com.christiancriollo.orders.application.dto;

import java.util.List;
import java.util.UUID;

/**
 * Comando para crear un pedido.
 */
public record CreateOrderCommand(
        UUID customerId,
        List<CreateOrderItemCommand> items
) {
}