package com.christiancriollo.orders.application.usecase;

import com.christiancriollo.orders.application.dto.CreateOrderCommand;
import com.christiancriollo.orders.application.dto.CreateOrderItemCommand;
import com.christiancriollo.orders.application.dto.OrderResponse;
import com.christiancriollo.orders.application.exception.ResourceNotFoundException;
import com.christiancriollo.orders.application.port.CustomerServicePort;
import com.christiancriollo.orders.application.port.ProductServicePort;
import com.christiancriollo.orders.domain.model.Order;
import com.christiancriollo.orders.domain.model.OrderItem;
import com.christiancriollo.orders.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final CustomerServicePort customerServicePort;
    private final ProductServicePort productServicePort;

    public CreateOrderUseCase(
            OrderRepository orderRepository,
            CustomerServicePort customerServicePort,
            ProductServicePort productServicePort) {

        this.orderRepository = orderRepository;
        this.customerServicePort = customerServicePort;
        this.productServicePort = productServicePort;
    }

    public OrderResponse execute(CreateOrderCommand command) {

        // 1. Validar cliente
        if (!customerServicePort.existsById(command.customerId())) {
            throw new ResourceNotFoundException(
                    "Cliente no encontrado: " + command.customerId()
            );
        }

        // 2. Validar productos
        for (CreateOrderItemCommand item : command.items()) {

            if (!productServicePort.existsById(item.productId())) {
                throw new ResourceNotFoundException(
                        "Producto no encontrado: " + item.productId()
                );
            }
        }

        // 3. Crear los items del dominio
        List<OrderItem> items = command.items()
                .stream()
                .map(item -> new OrderItem(
                        item.productId(),
                        item.productName(),
                        item.unitPrice(),
                        item.quantity()
                ))
                .toList();

        // 4. Crear el pedido
        Order order = new Order(
                UUID.randomUUID(),
                command.customerId(),
                items
        );

        // 5. Persistir
        Order savedOrder = orderRepository.save(order);

        // 6. Construir respuesta
        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.calculateTotal(),
                savedOrder.getStatus()
        );
    }
}