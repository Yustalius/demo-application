package utils.logging.utils;

import utils.logging.api.LogWorkerInterface;

/**
 * Фабрика для создания различных типов обработчиков логов
 */
public class LogWorkerFactory {

    /**
     * Тип обработчика логов
     */
    public enum LogWorkerType {
        /**
         * Логгер с буферизацией, отправляет логи пакетами
         */
        BUFFERED,
        
        /**
         * Логгер без буферизации, отправляет каждый лог сразу
         */
        DIRECT
    }
    
    /**
     * Создает обработчик логов указанного типа
     * @param type тип обработчика логов
     * @param loggerUrl URL сервера логирования
     * @return обработчик логов указанного типа
     */
    public static LogWorkerInterface createLogWorker(LogWorkerType type, String loggerUrl) {
        return switch (type) {
            case BUFFERED -> new BufferedLogWorker(loggerUrl);
            case DIRECT -> new DirectLogWorker(loggerUrl);
        };
    }
} 