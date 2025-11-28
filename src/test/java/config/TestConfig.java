package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class TestConfig {
    private static final Logger log = Logger.getLogger(TestConfig.class.getName());
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
            log.warning("Error loading config.properties: " + e.getMessage());
        }
    }

    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? value : defaultValue;
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public static long getLongProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                log.warning("Invalid number format for property " + key + ": " + value);
            }
        }
        return defaultValue;
    }

    // Детекция Docker через переменную окружения
    public static boolean isRunningInDocker() {
        return System.getenv("SELENIDE_REMOTE") != null;
    }
}