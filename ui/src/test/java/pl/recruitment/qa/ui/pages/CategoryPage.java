package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Locale;

public class CategoryPage {
    protected final WebDriver driver;

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public boolean isVisible() {
        String pageText = driver.getPageSource().toLowerCase(Locale.ROOT);
        return pageText.contains("smartfony")
                || pageText.contains("smartfon")
                || pageText.contains("laptopy")
                || pageText.contains("tablety")
                || pageText.contains("urządzenia")
                || pageText.contains("urzadzenia");
    }

    public boolean isPageOpen(String sectionOptionName) {
        String normalizedOption = normalize(sectionOptionName);
        String pageText = driver.getPageSource().toLowerCase(Locale.ROOT);
        return pageText.contains(normalizedOption) || isVisible();
    }

    protected boolean isElementVisible(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    protected String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
    }
}
