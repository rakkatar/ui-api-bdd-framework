package pl.recruitment.qa.ui.browser;

public enum BrowserType {
    CHROME,
    FIREFOX;

    public static BrowserType from(String browserName) {
        if (browserName == null) {
            return CHROME;
        }

        switch (browserName.trim().toLowerCase()) {
            case "chrome":
                return CHROME;
            case "firefox":
                return FIREFOX;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName + ". Use chrome or firefox.");
        }
    }
}
