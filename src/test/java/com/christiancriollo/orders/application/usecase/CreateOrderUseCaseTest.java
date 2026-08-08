package com.christiancriollo.orders.application.usecase;

import com.christiancriollo.orders.application.dto.CreateOrderCommand;
import com.christiancriollo.orders.application.dto.CreateOrderItemCommand;
import com.christiancriollo.orders.application.dto.OrderResponse;
import com.christiancriollo.orders.application.exception.ResourceNotFoundException;
import com.christiancriollo.orders.application.port.CustomerServicePort;
import com.christiancriollo.orders.application.port.ProductServicePort;
import com.christiancriollo.orders.domain.model.Order;
import com.christiancriollo.orders.domain.model.OrderStatus;
import com.christiancriollo.orders.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerServicePort customerServicePort;

    @Mock
    private ProductServicePort productServicePort;

    private CreateOrderUseCase useCase;

    private UUID customerId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        useCase = new CreateOrderUseCase(
                orderRepository,
                customerServicePort,
                productServicePort
        );

        customerId = UUID.fromString(
                "11111111-1111-1111-1111-111111111111"
        );

        productId = UUID.fromString(
                "22222222-2222-2222-2222-222222222222"
        );
    }

    @Test
    void shouldCreateOrderSuccessfully() {

        CreateOrderCommand command = new CreateOrderCommand(
                customerId,
                List.of(
                        new CreateOrderItemCommand(
                                productId,
                                "Producto prueba",
                                new BigDecimal("35000.00"),
                                1
                        )
                )
        );

        when(customerServicePort.existsById(customerId))
                .thenReturn(true);

        when(productServicePort.existsById(productId))
                .thenReturn(true);

        ArgumentCaptor<Order> orderCaptor =
                ArgumentCaptor.forClass(Order.class);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponse response = useCase.execute(command);

        verify(customerServicePort)
                .existsById(customerId);

        verify(productServicePort)
                .existsById(productId);

        verify(orderRepository)
                .save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();

        assertNotNull(response);
        assertNotNull(response.id());

        assertEquals(
                customerId,
                response.customerId()
        );

        assertEquals(
                new BigDecimal("35000.00"),
                response.total()
        );

        assertEquals(OrderStatus.PENDING, response.status());

        assertEquals(
                customerId,
                savedOrder.getCustomerId()
        );

        assertEquals(
                1,
                savedOrder.getItems().size()
        );
    }

    @Test
    void shouldRejectOrderWhenCustomerDoesNotExist() {

        CreateOrderCommand command = new CreateOrderCommand(
                customerId,
                List.of(
                        new CreateOrderItemCommand(
                                productId,
                                "Producto prueba",
                                new BigDecimal("35000.00"),
                                1
                        )
                )
        );

        when(customerServicePort.existsById(customerId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> useCase.execute(command)
                );

        assertEquals(
                "Cliente no encontrado: " + customerId,
                exception.getMessage()
        );

        verify(customerServicePort)
                .existsById(customerId);

        verify(productServicePort, never())
                .existsById(any());

        verify(orderRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectOrderWhenProductDoesNotExist() {

        CreateOrderCommand command = new CreateOrderCommand(
                customerId,
                List.of(
                        new CreateOrderItemCommand(
                                productId,
                                "Producto prueba",
                                new BigDecimal("35000.00"),
                                1
                        )
                )
        );

        when(customerServicePort.existsById(customerId))
                .thenReturn(true);

        when(productServicePort.existsById(productId))
                .thenReturn(false);

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> useCase.execute(command)
                );

        assertEquals(
                "Producto no encontrado: " + productId,
                exception.getMessage()
        );

        verify(customerServicePort)
                .existsById(customerId);

        verify(productServicePort)
                .existsById(productId);

        verify(orderRepository, never())
                .save(any());
    }

    @Test
    void shouldValidateAllProductsBeforeSavingOrder() {

        UUID secondProductId = UUID.fromString(
                "33333333-3333-3333-3333-333333333333"
        );

        CreateOrderCommand command = new CreateOrderCommand(
                customerId,
                List.of(
                        new CreateOrderItemCommand(
                                productId,
                                "Producto 1",
                                new BigDecimal("10000.00"),
                                1
                        ),
                        new CreateOrderItemCommand(
                                secondProductId,
                                "Producto 2",
                                new BigDecimal("20000.00"),
                                1
                        )
                )
        );

        when(customerServicePort.existsById(customerId))
                .thenReturn(true);

        when(productServicePort.existsById(productId))
                .thenReturn(true);

        when(productServicePort.existsById(secondProductId))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> useCase.execute(command)
        );

        verify(productServicePort)
                .existsById(productId);

        verify(productServicePort)
                .existsById(secondProductId);

        verify(orderRepository, never())
                .save(any());
    }
}