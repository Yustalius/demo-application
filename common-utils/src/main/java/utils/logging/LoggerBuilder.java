package utils.logging;

import utils.logging.utils.LogWorkerFactory.LogWorkerType;

/**
 * Билдер для создания экземпляров логгера с различными настройками
 */
public class LoggerBuilder {
    private String loggerUrl;
    private LogWorkerType type = LogWorkerType.BUFFERED;

    /**
     * Устанавливает URL сервера логирования
     * @param loggerUrl URL сервера логирования
     * @return экземпляр билдера
     */
    public LoggerBuilder setLoggerUrl(String loggerUrl) {
        this.loggerUrl = loggerUrl;
        return this;
    }

    /**
     * Устанавливает тип логгера: с буферизацией
     * @return экземпляр билдера
     */
    public LoggerBuilder useBufferedLogger() {
        this.type = LogWorkerType.BUFFERED;
        return this;
    }

    /**
     * Устанавливает тип логгера: без буферизации (немедленная отправка)
     * @return экземпляр билдера
     */
    public LoggerBuilder useDirectLogger() {
        this.type = LogWorkerType.DIRECT;
        return this;
    }

    /**
     * Создает экземпляр логгера с указанными настройками
     * @return настроенный экземпляр логгера
     * @throws IllegalStateException если URL сервера логирования не был установлен
     */
    public Logger build() {
        if (loggerUrl == null || loggerUrl.isEmpty()) {
            throw new IllegalStateException("Logger URL is not set");
        }
        
        return new Logger(loggerUrl, type);
    }
    
    /**
     * Статический метод для начала построения логгера
     * @return новый экземпляр билдера
     */
    public static LoggerBuilder builder() {
        return new LoggerBuilder();
    }
} 