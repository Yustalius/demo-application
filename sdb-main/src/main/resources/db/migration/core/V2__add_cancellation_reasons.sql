-- V2__add_cancellation_reasons.sql

-- Таблица для хранения причин отмены заказов
CREATE TABLE IF NOT EXISTS public.cancellation_reasons (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    reason JSON,
    CONSTRAINT cancellation_reasons_orders_fk FOREIGN KEY (order_id) REFERENCES public.orders(order_id) ON DELETE CASCADE
);

-- Создание индекса для ускорения поиска по order_id
CREATE INDEX IF NOT EXISTS idx_cancellation_reasons_order_id ON public.cancellation_reasons(order_id);

-- Комментарий к таблице
COMMENT ON TABLE public.cancellation_reasons IS 'Таблица для хранения причин отмены заказов в формате JSON';

-- Комментарии к полям
COMMENT ON COLUMN public.cancellation_reasons.id IS 'Уникальный идентификатор записи';
COMMENT ON COLUMN public.cancellation_reasons.order_id IS 'Идентификатор заказа, к которому относится причина отмены';
COMMENT ON COLUMN public.cancellation_reasons.reason IS 'Причина отмены заказа в формате JSON'; 