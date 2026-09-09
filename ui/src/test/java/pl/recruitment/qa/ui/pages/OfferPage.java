package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page object for offers listing and product details navigation.
 */
public class OfferPage extends BasePage {

    private static final By OFFERS_CONTAINER_CHILD = By.cssSelector("#tbodyid > div");
    private static final By OFFERS_LINKS = By.cssSelector("#tbodyid a");

    public OfferPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Clicks the first offer in the list.
     */
    public void selectFirstOffer() {
        // wait until offers are injected
        getWait().until(ExpectedConditions.presenceOfElementLocated(OFFERS_CONTAINER_CHILD));

        List<WebElement> links = driver.findElements(OFFERS_LINKS);
        if (links.isEmpty()) {
            throw new IllegalStateException("Lista ofert jest pusta");
        }
        links.get(0).click();
    }

    /**
     * Returns true when details view is visible (simple check by URL contains prod.html).
     */
    public boolean isDetailsVisible() {
        try {
            getWait().until(ExpectedConditions.urlContains("prod.html"));
            return driver.getCurrentUrl().contains("prod.html");
        } catch (Exception e) {
            return false;
        }
    }
}
