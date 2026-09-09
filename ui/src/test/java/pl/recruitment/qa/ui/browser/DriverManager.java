package pl.recruitment.qa.ui.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class DriverManager {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final long DEFAULT_WAIT_SECONDS = 15L;

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    public static WebDriverWait getWait() {
        return getWait(Duration.ofSeconds(DEFAULT_WAIT_SECONDS));
    }

    public static WebDriverWait getWait(Duration timeout) {
        return new WebDriverWait(getDriver(), timeout);
    }
}
