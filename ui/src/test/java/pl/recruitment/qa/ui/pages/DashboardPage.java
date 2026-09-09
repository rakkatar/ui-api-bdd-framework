package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.recruitment.qa.ui.browser.DriverManager;

/**
 * Page object for the application dashboard / home page.
 * Encapsulates common actions and checks performed on the homepage.
 */
public class DashboardPage {
    private final WebDriver driver;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Wait until the homepage is visible: header home link + title contains "T-Mobile".
     */
    public DashboardPage waitForHomePage() {
        WebDriverWait wait = DriverManager.getWait();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[aria-label='Strona główna T-Mobile']")));
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
        return this;
    }

    /**
     * Returns true if the header home link is present on the page.
     */
    public boolean isHeaderHomeLinkPresent() {
        return !driver.findElements(By.cssSelector("a[aria-label='Strona główna T-Mobile']")).isEmpty();
    }

    /**
     * Clicks the Phones category link in the navigation (if present).
     */
    public void clickPhonesCategory() {
        try {
            WebElement phones = driver.findElement(By.xpath("//a[normalize-space() = 'Phones']"));
            phones.click();
        } catch (Exception ignored) {
            // element might not be present — caller handles absence
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }
}
