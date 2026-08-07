package com.christiancriollo.orders.infrastructure.adapter.customer;

import com.christiancriollo.orders.application.port.CustomerServicePort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StubCustomerServiceAdapter implements CustomerServicePort {

    @Override
    public boolean existsById(UUID customerId) {
        return true;
    }
}
