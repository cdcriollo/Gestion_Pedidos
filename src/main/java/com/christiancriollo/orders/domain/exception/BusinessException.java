package com.christiancriollo.orders.domain.exception;

/**
 * Excepción utilizada para representar violaciones a las reglas de negocio.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

}
