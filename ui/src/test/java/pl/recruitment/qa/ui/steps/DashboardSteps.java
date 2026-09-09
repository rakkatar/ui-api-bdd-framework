package pl.recruitment.qa.ui.steps;

import io.cucumber.java.en.Given;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.recruitment.qa.ui.browser.DriverManager;
import pl.recruitment.qa.core.ConfigProvider;

public class DashboardSteps {

    @Given("Przejdź na stronę portalu")
    public void goToPortal() {
        String base = ConfigProvider.get("ui.baseUrl");
        if (base == null || base.isEmpty()) {
            throw new IllegalStateException("ui.baseUrl is not configured (check core/src/main/resources/config.yml or env/ JVM properties)");
        }
        DriverManager.getDriver().get(base);

        WebDriverWait wait = DriverManager.getWait();
        // 1) wait for header home link
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[aria-label='Strona główna T-Mobile']")));

        // 2) verify title contains expected token
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
    }
}
