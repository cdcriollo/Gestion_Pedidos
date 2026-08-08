package com.christiancriollo.orders.infrastructure.persistence.mapper;

import com.christiancriollo.orders.domain.model.Order;
import com.christiancriollo.orders.domain.model.OrderItem;
import com.christiancriollo.orders.infrastructure.persistence.entity.OrderEntity;
import com.christiancriollo.orders.infrastructure.persistence.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderPersistenceMapper {

    public OrderEntity toEntity(Order order) {

        OrderEntity entity = new OrderEntity(
                order.getId(),
                order.getCustomerId(),
                order.getStatus()
        );

        List<OrderItemEntity> itemEntities = order.getItems()
                .stream()
                .map(item -> toItemEntity(item, entity))
                .toList();

        entity.setItems(itemEntities);

        return entity;
    }

    private OrderItemEntity toItemEntity(
            OrderItem item,
            OrderEntity orderEntity) {

        OrderItemEntity entity = new OrderItemEntity(
                item.getProductId(),
                item.getProductName(),
                item.getUnitPrice(),
                item.getQuantity()
        );

        entity.setOrder(orderEntity);

        return entity;
    }

    public Order toDomain(OrderEntity entity) {

        List<OrderItem> items = entity.getItems()
                .stream()
                .map(item -> new OrderItem(
                        item.getProductId(),
                        item.getProductName(),
                        item.getUnitPrice(),
                        item.getQuantity()
                ))
                .toList();

        Order order = new Order(
                entity.getId(),
                entity.getCustomerId(),
                items
        );

        /*
         * El dominio actualmente inicia todas las órdenes
         * en PENDING. Para reconstruir una orden persistida
         * debemos recuperar su estado real.
         */
        if (entity.getStatus().name().equals("CONFIRMED")) {
            order.confirm();
        } else if (entity.getStatus().name().equals("CANCELLED")) {
            order.cancel();
        }

        return order;
    }
}