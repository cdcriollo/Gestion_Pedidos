package com.christiancriollo.orders.application.dto;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representa un producto solicitado para crear un pedido.
 */

public record CreateOrderItemCommand(
     UUID productId,
     String productName,
     BigDecimal unitPrice,
     Integer quantity

) {
         
}
