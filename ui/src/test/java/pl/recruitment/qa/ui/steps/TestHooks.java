package pl.recruitment.qa.ui.steps;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import pl.recruitment.qa.ui.browser.DriverFactory;
import pl.recruitment.qa.ui.browser.DriverManager;
import pl.recruitment.qa.ui.context.ScenarioContext;

import org.openqa.selenium.WebDriver;
import java.time.Duration;

public class TestHooks {

    @Before
    public void beforeScenario() {
        if (DriverManager.getDriver() == null) {
            WebDriver newDriver = DriverFactory.create(System.getProperty("browser", "chrome"));
            DriverManager.setDriver(newDriver);
        }
        // maximize and (implicit waits disabled) — prefer explicit waits via DriverManager.getWait()
        DriverManager.getDriver().manage().window().maximize();
    }

    @After
    public void afterScenario() {
        DriverManager.quitDriver();
        ScenarioContext.clear();
    }
}
