package com.christiancriollo.orders.infrastructure.web.mapper;
import com.christiancriollo.orders.application.dto.CreateOrderCommand;
import com.christiancriollo.orders.application.dto.CreateOrderItemCommand;
import com.christiancriollo.orders.infrastructure.web.request.CreateOrderItemRequest;
import com.christiancriollo.orders.infrastructure.web.request.CreateOrderRequest;
import org.springframework.stereotype.Component;

@Component

public class OrderRequestMapper {
    public CreateOrderCommand toCommand(CreateOrderRequest request) {

        return new CreateOrderCommand(
                request.customerId(),
                request.items()
                        .stream()
                        .map(this::toCommand)
                        .toList()
        );
    }

    private CreateOrderItemCommand toCommand(
            CreateOrderItemRequest request) {

        return new CreateOrderItemCommand(
                request.productId(),
                request.productName(),
                request.unitPrice(),
                request.quantity()
        );
    }

}
