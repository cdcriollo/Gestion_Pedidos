package com.christiancriollo.orders.infrastructure.web.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
     @NotNull(message = "El cliente es requerido")
        UUID customerId,

        @NotEmpty(message = "La orden debe contener al menos un item")
        List<@Valid CreateOrderItemRequest> items
) {


}
