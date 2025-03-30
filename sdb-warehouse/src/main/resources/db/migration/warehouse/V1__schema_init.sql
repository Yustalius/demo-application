-- V1__Create_products_and_orders_tables.sql

-- Таблица продуктов
CREATE TABLE IF NOT EXISTS public.products (
    id SERIAL PRIMARY KEY,
    external_product_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    CONSTRAINT products_unique UNIQUE (external_product_id)
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS public.orders (
    id SERIAL PRIMARY KEY,
    external_order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    CONSTRAINT orders_products_fk FOREIGN KEY (product_id) REFERENCES public.products(id) ON DELETE SET NULL
);