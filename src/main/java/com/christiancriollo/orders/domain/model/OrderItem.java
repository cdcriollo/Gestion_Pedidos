package com.christiancriollo.orders.domain.model;
import com.christiancriollo.orders.domain.exception.BusinessException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Representa un producto dentro de un pedido.
 */

public class OrderItem {

    private final UUID productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private final Integer quantity;

    public OrderItem(
            UUID productId,
            String productName,
            BigDecimal unitPrice,
            Integer quantity) {

        if (productId == null) {
            throw new BusinessException("Producto es requerido.");
        }

        if (productName == null || productName.isBlank()) {
            throw new BusinessException("Nombre del producto es requerido.");
        }

        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Precio unitario debe ser mayor que cero.");
        }

        if (quantity == null || quantity <= 0) {
            throw new BusinessException("Cantidad debe ser mayor que cero.");
        }

        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public BigDecimal calculateSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem that)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}
