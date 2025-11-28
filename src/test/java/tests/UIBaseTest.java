package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class UIBaseTest {
    @BeforeEach
    public void setUp() {
        var isRunningInDocker = TestConfig.isRunningInDocker();
        setupCommonConfiguration();
        if (isRunningInDocker) {
            configureForDocker();
        } else {
            configureForLocal();
        }

        // Открытие wikipedia.org
        Selenide.open("https://ru.wikipedia.org/");

        if (!isRunningInDocker) {
            Selenide.webdriver().driver().getWebDriver().manage().window().maximize();
        }

        log.info("Браузер запущен, страница Wikipedia загружена");
    }

    private void setupCommonConfiguration() {
        Configuration.browser = TestConfig.getProperty("browser.name", "chrome");
        Configuration.browserSize = TestConfig.getProperty("browser.size", "1920x1080");
        Configuration.screenshots = TestConfig.getBooleanProperty("screenshots", true);
        Configuration.savePageSource = TestConfig.getBooleanProperty("savePageSource", false);
        Configuration.reportsFolder = TestConfig.getProperty("reportsFolder", "target/screenshots");
        Configuration.timeout = TestConfig.getLongProperty("timeout", 10000);
        Configuration.pageLoadTimeout = TestConfig.getLongProperty("pageLoadTimeout", 30000);

        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        Logger.getLogger("com.codeborne.selenide").setLevel(Level.WARNING);
    }

    private void configureForDocker() {
        log.info("Настройка для Docker окружения");

        Configuration.remote = TestConfig.getProperty("docker.remote.url", "http://selenium-hub:4444/wd/hub");

        Configuration.remoteConnectionTimeout = TestConfig.getLongProperty("remoteConnectionTimeout", 60000);
        Configuration.remoteReadTimeout = TestConfig.getLongProperty("remoteReadTimeout", 60000);

        var options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");

        Configuration.browserCapabilities = options;

        log.info("Docker конфигурация применена. Remote URL: {}", Configuration.remote);
    }

    private void configureForLocal() {
        log.info("Настройка для локального окружения");

        Configuration.remote = null;

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");

        Configuration.browserCapabilities = options;

        Configuration.headless = false;

        log.info("Локальная конфигурация применена");
    }

    @AfterEach
    protected void closeBrowser() {
        log.info("Закрытие браузера");
        Selenide.closeWebDriver();
    }

    protected static void takeScreenshot(String name) {
        var timestamp = LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        var screenshot = Selenide.screenshot(String.format("%s_%s", name, timestamp));

        log.info("Скриншот сохранен: {}", screenshot);
    }

    protected void reopenWikiPageFromIPhone(String mobileBrand) {
        log.info("Перезапуск браузера с мобильным User-Agent: {}", mobileBrand);

        var options = new ChromeOptions();
        options.addArguments(
                "--user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
                "--no-sandbox",
                "--disable-dev-shm-usage"
        );

        Configuration.browserCapabilities = options;
        Configuration.browserSize = "414x896";

        Selenide.closeWebDriver();
        Selenide.open("https://ru.wikipedia.org/");

        log.info("Мобильная версия открыта с User-Agent: {}", mobileBrand);
    }
}