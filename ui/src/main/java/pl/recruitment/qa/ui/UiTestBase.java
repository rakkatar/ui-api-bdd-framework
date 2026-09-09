package pl.recruitment.qa.ui;

import pl.recruitment.qa.core.ConfigProvider;

public class UiTestBase {
    protected String baseUrl() {
        String v = ConfigProvider.get("ui.baseUrl");
        return v != null ? v : "https://example.com";
    }
}
