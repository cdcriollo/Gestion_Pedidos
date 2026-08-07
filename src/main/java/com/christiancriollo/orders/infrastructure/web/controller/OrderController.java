package com.christiancriollo.orders.infrastructure.web.controller;
import com.christiancriollo.orders.application.dto.OrderResponse;
import com.christiancriollo.orders.application.usecase.CreateOrderUseCase;
import com.christiancriollo.orders.infrastructure.web.mapper.OrderRequestMapper;
import com.christiancriollo.orders.infrastructure.web.request.CreateOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")

public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderRequestMapper mapper;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            OrderRequestMapper mapper) {

        this.createOrderUseCase = createOrderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        return createOrderUseCase.execute(
                mapper.toCommand(request)
        );
    }

}
