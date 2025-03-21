package utils.logging.api;

import utils.logging.model.LogTask;

/**
 * Интерфейс для обработчиков логов.
 * Определяет основные методы, необходимые для работы логгера.
 */
public interface LogWorkerInterface {
    
    /**
     * Добавление лога для обработки
     * @param logTask задание для логирования
     */
    void enqueueLog(LogTask logTask);
    
    /**
     * Корректное завершение работы логгера
     */
    void shutdown();
} 