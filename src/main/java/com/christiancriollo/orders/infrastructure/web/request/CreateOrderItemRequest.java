package com.christiancriollo.orders.infrastructure.web.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderItemRequest(

        @NotNull(message = "El producto es requerido")
        UUID productId,

        @NotBlank(message = "El nombre del producto es requerido")
        String productName,

        @NotNull(message = "El precio unitario es requerido")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
        BigDecimal unitPrice,

        @NotNull(message = "La cantidad es requerida")
        @Min(value = 1, message = "La cantidad debe ser mayor que cero")
        Integer quantity

) {
}