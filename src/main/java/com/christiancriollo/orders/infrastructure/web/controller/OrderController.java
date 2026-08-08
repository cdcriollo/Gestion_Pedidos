package com.christiancriollo.orders.infrastructure.web.controller;
import com.christiancriollo.orders.application.dto.OrderResponse;
import com.christiancriollo.orders.application.usecase.CreateOrderUseCase;
import com.christiancriollo.orders.infrastructure.web.mapper.OrderRequestMapper;
import com.christiancriollo.orders.infrastructure.web.request.CreateOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.christiancriollo.orders.application.usecase.CancelOrderUseCase;
import com.christiancriollo.orders.application.usecase.ConfirmOrderUseCase;
import com.christiancriollo.orders.application.usecase.GetOrderUseCase;
import java.util.UUID;

@RestController
@RequestMapping("/orders")

public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderRequestMapper mapper;
    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            OrderRequestMapper mapper,
            ConfirmOrderUseCase confirmOrderUseCase,
            CancelOrderUseCase cancelOrderUseCase,
            GetOrderUseCase getOrderUseCase) 
            {

        this.createOrderUseCase = createOrderUseCase;
        this.mapper = mapper;
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        return createOrderUseCase.execute(
                mapper.toCommand(request)
        );
    }

    @PostMapping("/{orderId}/confirm")
    public OrderResponse confirmOrder(
            @PathVariable UUID orderId) {

        return confirmOrderUseCase.execute(orderId);
    }

    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancelOrder(
            @PathVariable UUID orderId) {

        return cancelOrderUseCase.execute(orderId);
    }

     @GetMapping("/{orderId}")
    public OrderResponse getOrder(
            @PathVariable UUID orderId) {

        return getOrderUseCase.execute(orderId);
    }

}
