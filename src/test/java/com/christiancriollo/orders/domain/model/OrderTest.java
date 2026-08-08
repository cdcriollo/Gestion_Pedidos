package com.christiancriollo.orders.domain.model;

import com.christiancriollo.orders.domain.exception.BusinessException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private final UUID orderId =
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    private final UUID customerId =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private final UUID productId =
            UUID.fromString("22222222-2222-2222-2222-222222222222");

    private Order createOrder() {

        OrderItem item = new OrderItem(
                productId,
                "Producto prueba",
                new BigDecimal("10000.00"),
                2
        );

        return new Order(
                orderId,
                customerId,
                List.of(item)
        );
    }

    @Test
    void shouldCreateOrderWithPendingStatus() {

        Order order = createOrder();

        assertEquals(orderId, order.getId());
        assertEquals(customerId, order.getCustomerId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void shouldCalculateOrderTotal() {

        OrderItem item1 = new OrderItem(
                productId,
                "Producto 1",
                new BigDecimal("10000.00"),
                2
        );

        OrderItem item2 = new OrderItem(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "Producto 2",
                new BigDecimal("5000.00"),
                3
        );

        Order order = new Order(
                orderId,
                customerId,
                List.of(item1, item2)
        );

        assertEquals(
                new BigDecimal("35000.00"),
                order.calculateTotal()
        );
    }

    @Test
    void shouldConfirmPendingOrder() {

        Order order = createOrder();

        order.confirm();

        assertEquals(
                OrderStatus.CONFIRMED,
                order.getStatus()
        );
    }

    @Test
    void shouldCancelPendingOrder() {

        Order order = createOrder();

        order.cancel();

        assertEquals(
                OrderStatus.CANCELLED,
                order.getStatus()
        );
    }

    @Test
    void shouldNotConfirmCancelledOrder() {

        Order order = createOrder();

        order.cancel();

        BusinessException exception = assertThrows(
                BusinessException.class,
                order::confirm
        );

        assertEquals(
                "Una orden cancelada no puede ser confirmada.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotCancelConfirmedOrder() {

        Order order = createOrder();

        order.confirm();

        BusinessException exception = assertThrows(
                BusinessException.class,
                order::cancel
        );

        assertEquals(
                "Una orden confirmada no puede ser cancelada.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotConfirmAlreadyConfirmedOrder() {

        Order order = createOrder();

        order.confirm();

        BusinessException exception = assertThrows(
                BusinessException.class,
                order::confirm
        );

        assertEquals(
                "La orden ha sido confirmada.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotCancelAlreadyCancelledOrder() {

        Order order = createOrder();

        order.cancel();

        BusinessException exception = assertThrows(
                BusinessException.class,
                order::cancel
        );

        assertEquals(
                "La orden ha sido cancelada.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotCreateOrderWithoutItems() {

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> new Order(
                        orderId,
                        customerId,
                        List.of()
                )
        );

        assertEquals(
                "La orden debe contener al menos un item.",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotCreateOrderWithoutCustomer() {

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> new Order(
                        orderId,
                        null,
                        List.of(
                                new OrderItem(
                                        productId,
                                        "Producto prueba",
                                        new BigDecimal("10000.00"),
                                        1
                                )
                        )
                )
        );

        assertEquals(
                "Id cliente es requerido.",
                exception.getMessage()
        );
    }
}