package com.christiancriollo.orders.infrastructure.persistence.repository;

import com.christiancriollo.orders.domain.model.Order;
import com.christiancriollo.orders.domain.repository.OrderRepository;
import com.christiancriollo.orders.infrastructure.persistence.mapper.OrderPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaOrderRepositoryAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderPersistenceMapper mapper;

    public JpaOrderRepositoryAdapter(
            JpaOrderRepository jpaOrderRepository,
            OrderPersistenceMapper mapper) {

        this.jpaOrderRepository = jpaOrderRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {

        var entity = mapper.toEntity(order);

        var savedEntity = jpaOrderRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(UUID id) {

        return jpaOrderRepository.findById(id)
                .map(mapper::toDomain);
    }
}