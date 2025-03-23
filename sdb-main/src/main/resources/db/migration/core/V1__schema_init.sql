-- V1__create_tables.sql

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS public.user_creds (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    pass VARCHAR(255) NOT NULL
);

-- Таблица продуктов
CREATE TABLE IF NOT EXISTS public.products (
    id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) UNIQUE NOT NULL,
    description TEXT,
    price INTEGER NOT NULL
);

-- Таблица пользовательских данных
CREATE TABLE IF NOT EXISTS public.users (
    id INTEGER PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    age INTEGER,
    CONSTRAINT fk_user_creds FOREIGN KEY (id) REFERENCES public.user_creds(id) ON DELETE CASCADE
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS public.orders (
    order_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'IN_WORK', 'FINISHED', 'CANCELED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES public.user_creds(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Таблица элементов заказа
CREATE TABLE IF NOT EXISTS public.order_items (
    order_item_id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    product_id INTEGER,
    quantity INTEGER NOT NULL,
    price INTEGER NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES public.orders(order_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES public.products(id) ON DELETE CASCADE ON UPDATE CASCADE
);