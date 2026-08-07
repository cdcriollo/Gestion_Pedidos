package com.christiancriollo.orders.application.port;
import java.util.UUID;

/**
 * Puerto para consultar clientes.
 */

public interface CustomerServicePort {

    boolean existsById(UUID customerId);
}
