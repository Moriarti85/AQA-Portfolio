package config;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class TestConfig {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            log.warn("Ошибка при загрузке config.properties: {}", e.getMessage());
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static boolean getProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public static long getProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                log.warn("Недопустимый числовой формат для свойства {}: {}", key, value);
            }
        }
        return defaultValue;
    }

    // Детекция Docker через переменную окружения
    public static boolean isRunningInDocker() {
        return System.getenv("SELENIDE_REMOTE") != null;
    }
}