package com.christiancriollo.orders.infrastructure.adapter.product;

import com.christiancriollo.orders.application.port.ProductServicePort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StubProductServiceAdapter implements ProductServicePort {

    @Override
    public boolean existsById(UUID productId) {
        return true;
    }
}