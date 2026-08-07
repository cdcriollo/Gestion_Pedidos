package com.christiancriollo.orders.application.port;
import java.util.UUID;

/**
 * Puerto para consultar productos.
 */

public interface ProductServicePort {
    boolean existsById(UUID productId);

}
