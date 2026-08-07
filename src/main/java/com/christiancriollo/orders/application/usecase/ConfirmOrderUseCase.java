package com.christiancriollo.orders.application.usecase;

import com.christiancriollo.orders.application.dto.OrderResponse;
import com.christiancriollo.orders.application.exception.ResourceNotFoundException;
import com.christiancriollo.orders.domain.model.Order;
import com.christiancriollo.orders.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ConfirmOrderUseCase {

    private final OrderRepository orderRepository;

    public ConfirmOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse execute(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pedido no encontrado: " + orderId
                ));

        order.confirm();

        Order savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.calculateTotal(),
                savedOrder.getStatus()
        );
    }
}