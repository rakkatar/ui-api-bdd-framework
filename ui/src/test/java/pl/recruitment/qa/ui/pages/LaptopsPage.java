package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.WebDriver;

public class LaptopsPage extends CategoryPage {
    public LaptopsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageOpen(String sectionOptionName) {
        String normalizedOption = normalize(sectionOptionName);
        switch (normalizedOption) {
            case "laptopy":
            case "tablety":
                return isVisible();
            default:
                return super.isPageOpen(sectionOptionName);
        }
    }
}
