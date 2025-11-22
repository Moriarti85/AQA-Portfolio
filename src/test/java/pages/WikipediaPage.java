package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class WikipediaPage {
    public static final SelenideElement LOGO = $("#mw-panel");
    public static final SelenideElement SEARCH_FORM = $("#searchform");
    public static final SelenideElement CONTENT = $("#content");
    public static final SelenideElement FOOTER = $("#footer");
    public static final ElementsCollection SIDE_BAR_LINKS = $("#mw-panel").$$("a:not([href=''] span");
}
