package utils.logging;

import utils.logging.api.LogWorkerInterface;
import utils.logging.model.LogLevel;
import utils.logging.model.LogTask;
import utils.logging.utils.LogWorkerFactory;
import utils.logging.utils.LogWorkerFactory.LogWorkerType;

import static utils.logging.utils.LoggerUtils.getCurrentTimestamp;
import static utils.logging.utils.LoggerUtils.getPath;

/**
 * Логгер для отправки логов на сервер
 */
public class Logger {
  private final LogWorkerInterface logWorker;

  /**
   * Создает логгер с буферизацией (по умолчанию)
   * @param loggerUrl URL сервера логирования
   */
  public Logger(String loggerUrl) {
    this(loggerUrl, LogWorkerType.BUFFERED);
  }

  /**
   * Создает логгер указанного типа
   * @param loggerUrl URL сервера логирования
   * @param type тип логгера (с буферизацией или без)
   */
  public Logger(String loggerUrl, LogWorkerType type) {
    this.logWorker = LogWorkerFactory.createLogWorker(type, loggerUrl);
  }

  /**
   * Логирование информационного сообщения
   * @param message сообщение для логирования
   */
  public void info(String message) {
    log(LogLevel.INFO, message);
  }

  /**
   * Логирование информационного сообщения с вложением
   * @param message сообщение для логирования
   * @param attachment вложение для добавления к сообщению
   * @param <T> тип вложения
   */
  public <T> void info(String message, T attachment) {
    log(LogLevel.INFO, message + attachment.toString());
  }

  /**
   * Логирование предупреждения
   * @param message сообщение для логирования
   */
  public void warn(String message) {
    log(LogLevel.WARN, message);
  }

  /**
   * Логирование предупреждения с вложением
   * @param message сообщение для логирования
   * @param attachment вложение для добавления к сообщению
   * @param <T> тип вложения
   */
  public <T> void warn(String message, T attachment) {
    log(LogLevel.WARN, message + attachment.toString());
  }

  /**
   * Логирование ошибки
   * @param message сообщение для логирования
   */
  public void error(String message) {
    log(LogLevel.ERROR, message);
  }

  /**
   * Логирование ошибки с вложением
   * @param message сообщение для логирования
   * @param attachment вложение для добавления к сообщению
   * @param <T> тип вложения
   */
  public <T> void error(String message, T attachment) {
    log(LogLevel.ERROR, message + attachment.toString());
  }

  /**
   * Выполняет логирование сообщения с указанным уровнем
   * @param level уровень логирования
   * @param message сообщение для логирования
   */
  private void log(LogLevel level, String message) {
    logWorker.enqueueLog(new LogTask(
        getCurrentTimestamp(),
        level,
        getPath(),
        message
    ));
  }

  /**
   * Корректно завершает работу логгера
   */
  public void shutdown() {
    logWorker.shutdown();
  }
}