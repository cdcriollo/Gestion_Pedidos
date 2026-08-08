CREATE TABLE orders (
    id UUID NOT NULL,
    customer_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,

    CONSTRAINT pk_orders
        PRIMARY KEY (id)
);

CREATE TABLE order_items (
    id UUID NOT NULL,
    product_id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    unit_price NUMERIC(19, 4) NOT NULL,
    quantity INTEGER NOT NULL,
    order_id UUID NOT NULL,

    CONSTRAINT pk_order_items
        PRIMARY KEY (id),

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
);

CREATE INDEX idx_order_items_order_id
    ON order_items(order_id);