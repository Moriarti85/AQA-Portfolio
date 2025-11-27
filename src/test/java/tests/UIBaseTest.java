package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
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
        var isRunningInDocker = System.getenv("SELENIDE_REMOTE") != null;

        // Общие настройки
        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;
        Configuration.reportsFolder = "target/screenshots";

        // Отключаем логи Selenium для уменьшения шума
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);
        Logger.getLogger("com.codeborne.selenide").setLevel(Level.WARNING);

        if (isRunningInDocker) {
            configureForDocker();
        } else {
            configureForLocal();
        }

        // Открытие wikipedia.org
        Selenide.open("https://ru.wikipedia.org/");

        // Максимизируем окно только в локальном режиме
        if (!isRunningInDocker) {
            Selenide.webdriver().driver().getWebDriver().manage().window().maximize();
        }

        log.info("Браузер запущен, страница Wikipedia загружена");
    }

    private void configureForDocker() {
        log.info("Настройка для Docker окружения");

        Configuration.remote = "http://selenium-hub:4444/wd/hub";

        // Стандартные таймауты
        Configuration.remoteConnectionTimeout = 60000;
        Configuration.remoteReadTimeout = 60000;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;

        ChromeOptions options = new ChromeOptions();

        // Только обязательные опции для Chrome в Docker
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");

        Configuration.browserCapabilities = options;

        log.info("Docker конфигурация применена");
    }

    private void configureForLocal() {
        log.info("Настройка для локального окружения");

        Configuration.remote = null;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;
    }

    @AfterEach
    protected void closeBrowser() {
        log.info("Закрытие браузера");
        Selenide.closeWebDriver();
    }

    protected static void takeScreenshot(String name) {
        String timestamp = LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Selenide.screenshot(String.format("%s_%s", name, timestamp));
        log.info("Скриншот сохранен: {}_{}", name, timestamp);
    }

    protected void reopenWikiPageFromIPhone(String mobileBrand) {
        log.info("Перезапуск браузера с мобильным User-Agent: {}", mobileBrand);

        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--headless=new"
        );

        Configuration.browserCapabilities = options;
        Configuration.browserSize = "414x896";

        Selenide.closeWebDriver();
        Selenide.open("https://ru.wikipedia.org/");

        log.info("Мобильная версия открыта с User-Agent: {}", mobileBrand);
    }
}