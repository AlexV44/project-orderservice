--liquibase formatted sql

--changeset alexv44:003_create_order_items_table

CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    item_id UUID NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (id)
    CONSTRAINT fk_order_items_orders FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_items FOREIGN KEY (item_id) REFERENCES items (id)
);

CREATE INDEX idx_order_items_order_id ON order_items (order_id);
CREATE INDEX idx_order_items_item_id ON order_items (item_id);

--rollback DROP TABLE items;