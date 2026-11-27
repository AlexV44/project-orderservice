--liquibase formatted sql

--changeset alexv44:004_add_audit_to_order_items
ALTER TABLE order_items ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT now();
ALTER TABLE order_items ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT now();

--rollback ALTER TABLE order_items DROP COLUMN created_at, DROP COLUMN updated_at;