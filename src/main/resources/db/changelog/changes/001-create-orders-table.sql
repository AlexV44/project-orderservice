--liquibase formatted sql

--changeset alexv44:001_create_orders_table

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    total_price DECIMAL(19, 2) NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);

--rollback DROP TABLE orders;