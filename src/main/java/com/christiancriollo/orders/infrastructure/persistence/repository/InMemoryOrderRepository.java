package com.christiancriollo.orders.infrastructure.persistence.repository;

import com.christiancriollo.orders.domain.model.Order;
import com.christiancriollo.orders.domain.repository.OrderRepository;
//import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class InMemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> database = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {

        database.put(order.getId(), order);

        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {

        return Optional.ofNullable(database.get(id));
    }
}