package com.christiancriollo.orders.application.dto;
import com.christiancriollo.orders.domain.model.OrderStatus;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Respuesta de un pedido.
 */

public record OrderResponse(
    UUID id,
    UUID customerId,
    BigDecimal total,
    OrderStatus status
) {

}
