package sdb.warehouse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import utils.logging.Logger;

/**
 * Конфигурация для системы логирования.
 * Импортирует автоконфигурацию из common-utils и создает бин логгера.
 */
@Configuration
public class LoggingConfig {

    /**
     * Создает бин логгера для внедрения в компоненты
     * 
     * @param loggingUrl URL сервера логирования из application.yaml
     * @return настроенный экземпляр логгера
     */
    @Bean
    public Logger logger(@Value("${app.logging.url}") String loggingUrl) {
        return new Logger(loggingUrl);
    }
} 