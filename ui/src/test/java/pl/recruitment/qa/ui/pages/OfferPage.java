package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class OfferPage {
    private final WebDriver driver;

    public OfferPage(WebDriver driver) {
        this.driver = driver;
    }

    public void selectPhone(String phoneName) {
        By locator = By.xpath("//*[self::a or self::button or self::div or self::li][contains(normalize-space(.), '" + phoneName + "')]");
        List<WebElement> elements = driver.findElements(locator);

        if (elements.isEmpty()) {
            throw new IllegalStateException("Phone not found in offer list: " + phoneName);
        }

        elements.get(0).click();
    }

    public boolean isPhoneDetailsVisible(String phoneName) {
        String pageText = driver.getPageSource();
        return pageText.toLowerCase().contains(phoneName.toLowerCase());
    }
}
