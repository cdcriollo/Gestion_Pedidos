package com.christiancriollo.orders.application.usecase;

import com.christiancriollo.orders.application.dto.OrderResponse;
import com.christiancriollo.orders.application.exception.ResourceNotFoundException;
import com.christiancriollo.orders.domain.model.Order;
import com.christiancriollo.orders.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetOrderUseCase {

    private final OrderRepository orderRepository;

    public GetOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse execute(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido no encontrado: " + orderId
                ));

        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                order.calculateTotal(),
                order.getStatus()
        );
    }
}