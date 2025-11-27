package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
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

        // Отключаем логи Selenium
        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

        if (isRunningInDocker) {
            configureForDocker();
        } else {
            configureForLocal();
        }

        // Открытие wikipedia.org и настройка браузера
        Selenide.open("https://ru.wikipedia.org/");
        Selenide.webdriver().driver().getWebDriver().manage().window().maximize();

        log.info("Браузер запущен, страница Wikipedia загружена");
    }

    private void configureForDocker() {
        Configuration.remote = "http://selenium-hub:4444/wd/hub";

        // Увеличиваем таймауты для Docker
        Configuration.remoteConnectionTimeout = 120000;
        Configuration.remoteReadTimeout = 120000;
        Configuration.timeout = 30000;
        Configuration.pageLoadTimeout = 60000;

        ChromeOptions options = new ChromeOptions();

        // Критически важные опции для работы в Docker
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--window-size=1920,1080");

        // Отключаем автоматизацию и логирование
        options.setExperimentalOption("excludeSwitches", new String[]{
                "enable-automation",
                "enable-logging"
        });

        // Отключаем DevTools для избежания ошибок WebSocket
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        // Явно отключаем CDP
        options.setCapability("se:cdp", false);
        options.setCapability("se:cdpVersion", false);
        options.setCapability("se:vnc", false);

        Configuration.browserCapabilities = options;
    }

    private void configureForLocal() {
        Configuration.remote = null;
        Configuration.timeout = 10000;
        Configuration.pageLoadTimeout = 30000;

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        Configuration.browserCapabilities = options;
    }

    @AfterEach
    protected void closeBrowser() {
        // Сбрасываем настройки перед закрытием
        Configuration.browserCapabilities = new ChromeOptions();
        Selenide.closeWebDriver();
    }

    protected static void takeScreenshot(String name) {
        String timestamp = LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Selenide.screenshot(String.format("%s_%s", name, timestamp));
    }

    protected void reopenWikiPageFromIPhone(String mobileBrand) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        Configuration.browserCapabilities = options;
        Configuration.browserSize = "414x896";

        Selenide.closeWebDriver();
        Selenide.open("https://ru.wikipedia.org/");
        log.info("Открыта мобильная версия с User-Agent: {}", mobileBrand);
    }
}