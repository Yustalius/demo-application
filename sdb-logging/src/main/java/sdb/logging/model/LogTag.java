package sdb.logging.model;

/**
 * Теги для разделения логов по разным файлам
 */
public enum LogTag {
  /**
   * Все логи, записываются в sdb.log
   */
  ALL("sdb-logging/log/sdb.log"),
  
  /**
   * Логи основного приложения, записываются в sdb-core.log
   */
  CORE("sdb-logging/log/sdb-core.log"),
  
  /**
   * Логи сервиса склада, записываются в sdb-wh.log
   */
  WAREHOUSE("sdb-logging/log/sdb-wh.log"),
  
  /**
   * Логи сервиса очередей сообщений, записываются в sdb-rabbit.log
   */
  RABBIT("sdb-logging/log/sdb-rabbit.log");
  
  private final String logFilePath;
  
  LogTag(String logFilePath) {
    this.logFilePath = logFilePath;
  }
  
  /**
   * Получить путь к файлу лога для данного тега
   * @return путь к файлу лога
   */
  public String getLogFilePath() {
    return logFilePath;
  }
}
