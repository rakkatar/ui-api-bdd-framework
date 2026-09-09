package pl.recruitment.qa.ui.steps;

import io.cucumber.java.en.Given;
import pl.recruitment.qa.ui.browser.DriverManager;
import pl.recruitment.qa.core.ConfigProvider;
import pl.recruitment.qa.ui.pages.DashboardPage;

public class DashboardSteps {

    @Given("Przejdź na stronę portalu")
    public void goToPortal() {
        String base = ConfigProvider.get("ui.baseUrl");
        if (base == null || base.isEmpty()) {
            throw new IllegalStateException("ui.baseUrl is not configured (check core/src/main/resources/config.yml or env/ JVM properties)");
        }
        DriverManager.getDriver().get(base);

        new DashboardPage(DriverManager.getDriver()).waitForHomePage();
    }
}
