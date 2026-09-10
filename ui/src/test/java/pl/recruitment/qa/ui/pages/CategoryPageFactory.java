package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.WebDriver;

import java.util.Locale;

public final class CategoryPageFactory {
    private CategoryPageFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static CategoryPage createForCurrentPage(WebDriver driver) {
        String currentUrl = driver.getCurrentUrl().toLowerCase(Locale.ROOT);

        if (currentUrl.contains("/telefony/") || currentUrl.contains("smartfon")) {
            return new DevicesPage(driver);
        }

        if (currentUrl.contains("/laptopy") || currentUrl.contains("/tablety") || currentUrl.contains("tablet")) {
            return new LaptopsPage(driver);
        }

        return new CategoryPage(driver);
    }
}
