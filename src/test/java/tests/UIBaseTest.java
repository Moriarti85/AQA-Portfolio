package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDateTime;

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
            Configuration.browserCapabilities = new ChromeOptions();
            Configuration.browserCapabilities.setCapability("se:cdp", false);
            Configuration.browserCapabilities.setCapability("se:cdpVersion", false);
            Configuration.browserCapabilities.setCapability("se:vnc", false);
        } else {
            Configuration.remote = null;
            Configuration.timeout = 10000;
            Configuration.pageLoadTimeout = 30000;
        }

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
}
