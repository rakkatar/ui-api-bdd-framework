package pl.recruitment.qa.ui.steps;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import pl.recruitment.qa.ui.browser.DriverFactory;
import pl.recruitment.qa.ui.browser.DriverManager;

import org.openqa.selenium.WebDriver;
import java.time.Duration;

public class TestHooks {

    @Before
    public void beforeScenario() {
        if (DriverManager.getDriver() == null) {
            WebDriver newDriver = DriverFactory.create(System.getProperty("browser", "chrome"));
            DriverManager.setDriver(newDriver);
        }
        // maximize and set implicit wait
        DriverManager.getDriver().manage().window().maximize();
        DriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @After
    public void afterScenario() {
        DriverManager.quitDriver();
    }
}
