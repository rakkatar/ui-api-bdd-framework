package pl.recruitment.qa.ui.steps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.recruitment.qa.ui.browser.DriverManager;

import java.time.Duration;

/**
 * Base class for step definitions to simplify access to WebDriver and waits.
 */
public abstract class StepsBase {

    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected WebDriverWait driverWait() {
        return DriverManager.getWait();
    }

    protected WebDriverWait driverWait(Duration timeout) {
        return DriverManager.getWait(timeout);
    }
}
