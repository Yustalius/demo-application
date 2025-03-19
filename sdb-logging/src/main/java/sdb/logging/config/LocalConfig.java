package sdb.logging.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

enum LocalConfig implements Config {
    INSTANCE;

    Map<String, Object> data;
    Map<String, String> sqlConfig;
    Map<String, String> logConfig;

    LocalConfig() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("config.yaml");

        data = yaml.load(inputStream);
        sqlConfig = (Map<String, String>) data.get("sql");
        logConfig = (Map<String, String>) data.get("logging");
    }

    @Override
    public String logFilePath() {
        return logConfig.get("log-file-path");
    }

    @Override
    public String coreLogFilePath() {
        return logConfig.get("log-file-path");
    }

    @Override
    public String whLogFilePath() {
        return logConfig.get("log-file-path");
    }

    @Override
    public String rabbitLogFilePath() {
        return logConfig.get("log-file-path");
    }
}