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

        if (isRunningInDocker) {
            Configuration.remote = "http://selenium-hub:4444/wd/hub";

            Configuration.remoteConnectionTimeout = 120000;
            Configuration.remoteReadTimeout = 120000;
            Configuration.timeout = 120000;
            Configuration.pageLoadTimeout = 120000;

            ChromeOptions options = new ChromeOptions();
            options.setCapability("se:cdp", false);
            options.setCapability("se:cdpVersion", false);
            options.setCapability("se:vnc", false);
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation", "enable-logging"});
            Configuration.browserCapabilities = options;
        } else {
            Configuration.remote = null;
            Configuration.timeout = 10000;
            Configuration.pageLoadTimeout = 30000;
        }

        Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

        Configuration.browser = "chrome";
        Configuration.browserSize = "1920x1080";
        Configuration.screenshots = true;
        Configuration.savePageSource = false;

        // Открытие wikipedia.org и настройка браузера
        Selenide.open("https://ru.wikipedia.org/");
        Selenide.webdriver().driver().getWebDriver().manage().window().maximize();

        log.info("Браузер запущен, страница Wikipedia загружена");
    }

    @AfterEach
    protected void closeBrowser() {
        Configuration.browserCapabilities = new ChromeOptions();
        Selenide.closeWebDriver();
    }

    protected static void takeScreenshot(String name) {
        String timestamp = LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Selenide.screenshot(String.format( "%s_%s",name, timestamp));
    }

    protected void reopenWikiPageFromIPhone(String mobileBrand) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-agent=Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");

        Configuration.browserCapabilities = options;
        Configuration.browserSize = "414x896";

        Selenide.closeWebDriver();
        Selenide.open("https://ru.wikipedia.org/");
        log.info("Открыта мобильная версия с User-Agent: {}", mobileBrand);
    }
}
