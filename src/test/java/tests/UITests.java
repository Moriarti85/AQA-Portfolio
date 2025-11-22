package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import enums.LinksColors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.WikipediaPage;

import java.time.Duration;
import java.util.Map;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class UITests extends UIBaseTest {
    private static final Duration DURATION = Duration.ofSeconds(5);

    @Test
    void openWikiWithScreenshotTest() {
        WikipediaPage.LOGO.shouldBe(visible, DURATION);
        WikipediaPage.SEARCH_FORM.shouldBe(visible);
        WikipediaPage.CONTENT.shouldBe(visible);

        var logoLink = WikipediaPage.LOGO.$(By.cssSelector("[class='mw-wiki-logo']"));
        logoLink.shouldBe(clickable)
                .shouldHave(tagName("a"))
                .shouldHave(attribute("href", getExpectedMainPageUrl()));

        WikipediaPage.FOOTER.scrollTo().shouldBe(visible, DURATION);

        takeScreenshot("WikiCommonPage");
    }

    @Test
    void checkSideBarLinksColorTest() {
        var allLinksHaveCorrectColors = WikipediaPage.SIDE_BAR_LINKS
                .asFixedIterable()
                .stream()
                .allMatch(link -> link.getCssValue("color").equals(LinksColors.getNotVisitedAsRGBA()));

        assertThat(allLinksHaveCorrectColors).as("Не все ссылки имеют правильный цвет").isTrue();
    }

    @Test
    void adaptivityTest() {
        reopenWikiFromIPhone("iPhone 14 Pro Max");
        var expectedFontFamily = "-apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, Inter, Helvetica, Arial, sans-serif";
        $(".minerva-header").shouldHave(cssValue("font-family", expectedFontFamily));
    }

    private void reopenWikiFromIPhone(String mobileModel) {
        var mobileEmulation = Map.of("deviceName", mobileModel);
        var chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
        Configuration.browserCapabilities = chromeOptions;

        Selenide.closeWindow();
        Selenide.open("https://ru.wikipedia.org");

        var mainMenu = $("#main-menu-input");
        assertThat(mainMenu.exists()).as("Нет элемента с id=main-menu-input. Возможно не применились настройки браузера").isTrue();
        mainMenu.shouldBe(hidden).shouldHave(attribute("role", "button"));
        $("[for='main-menu-input']").shouldHave(attribute("aria-hidden", "true"));
        log.info("Эмуляция открытия wiki на {} успешно выполнена", mobileModel);
    }

    private String getExpectedMainPageUrl() {
        return """
                https://ru.wikipedia.org/wiki/%D0%97%D0%B0%D0%B3%D0%BB%D0%B0%D0%B2%D0%BD%D0%B0%D1%8F_%D1%81%D1%82%D1%80%D0%B0%D0%BD%D0%B8%D1%86%D0%B0""";
    }
}
