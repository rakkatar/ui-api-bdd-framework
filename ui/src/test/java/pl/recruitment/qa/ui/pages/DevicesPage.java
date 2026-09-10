package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Locale;

import static pl.recruitment.qa.ui.browser.DriverManager.getWait;

public class DevicesPage extends CategoryPage {
    private final By devicesHeading = By.xpath(
            "//*[self::h1 or self::h2 or self::h3 or self::span]" +
                    "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'urządzenia') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'urzadzenia')]"
    );
    private final By smartphoneList = By.xpath(
            "//*[self::section or self::div or self::ul or self::ol or self::article]" +
                    "[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'smartfon') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'smartfony')]"
    );

    public DevicesPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isVisible() {
        WebDriverWait wait = getWait();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("smartfon"),
                ExpectedConditions.urlContains("urzadzenia"),
                ExpectedConditions.presenceOfElementLocated(devicesHeading)
        ));

        return isElementVisible(devicesHeading) || isElementVisible(smartphoneList);
    }

    @Override
    public boolean isPageOpen(String sectionOptionName) {
        String normalizedOption = normalize(sectionOptionName);
        String currentUrl = driver.getCurrentUrl().toLowerCase(Locale.ROOT);

        switch (normalizedOption) {
            case "bez abonamentu":
                return currentUrl.contains("hardwareonlysale=true")
                        && areDevicesPageContentVisible();
            case "z abonamentem":
                return !currentUrl.contains("hardwareonlysale=true")
                        && areDevicesPageContentVisible();
            case "zestawy":
                return (currentUrl.contains("bp=acquisition") || currentUrl.contains("filter.categorydevice"))
                        && areDevicesPageContentVisible();
            default:
                return areDevicesPageContentVisible();
        }
    }

    private boolean areDevicesPageContentVisible() {
        return isElementVisible(devicesHeading)
                && isElementVisible(smartphoneList);
    }
}
