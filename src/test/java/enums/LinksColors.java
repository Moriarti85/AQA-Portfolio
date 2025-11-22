package enums;

import org.openqa.selenium.support.Color;

public enum LinksColors {
    NOT_VISITED ("#0645ad");

    private final String hex;

    LinksColors(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }

    public static String getNotVisitedAsRGBA() {
        return Color.fromString(NOT_VISITED.getHex()).asRgba();
    }
}
