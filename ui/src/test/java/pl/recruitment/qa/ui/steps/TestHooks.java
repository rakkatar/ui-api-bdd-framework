package pl.recruitment.qa.ui.steps;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import pl.recruitment.qa.ui.browser.DriverFactory;
import pl.recruitment.qa.ui.browser.DriverManager;

import org.openqa.selenium.WebDriver;

public class TestHooks {

    @Before
    public void beforeScenario() {
        if (DriverManager.getDriver() == null) {
            WebDriver newDriver = DriverFactory.create(System.getProperty("browser", "chrome"));
            DriverManager.setDriver(newDriver);
        }
    }

    @After
    public void afterScenario() {
        DriverManager.quitDriver();
    }
}
