package com.example.demo.config;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

enum LocalConfig implements Config {
    INSTANCE;

    Map<String, Object> data;
    Map<String, String> sqlConfig;

    LocalConfig() {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("config.yaml");

        data = yaml.load(inputStream);
        sqlConfig = (Map<String, String>) data.get("sql");
    }

    @Override
    public String postgresUrl() {
        return sqlConfig.get("postgres-url");
    }

    @Override
    public String postgresUsername() {
        return sqlConfig.get("postgres-username");
    }

    @Override
    public String postgresPassword() {
        return sqlConfig.get("postgres-password");
    }
}