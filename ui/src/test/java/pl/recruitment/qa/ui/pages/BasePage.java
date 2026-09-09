package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.recruitment.qa.ui.browser.DriverManager;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebDriverWait getWait() {
        return DriverManager.getWait();
    }

    protected WebDriverWait getWait(Duration timeout) {
        return DriverManager.getWait(timeout);
    }

    protected WebElement clickWhenVisible(By locator) {
        WebElement el = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        el.click();
        return el;
    }

    protected WebElement findWhenVisible(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }
}
