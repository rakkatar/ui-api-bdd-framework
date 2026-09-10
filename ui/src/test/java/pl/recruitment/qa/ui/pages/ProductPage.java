package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Locale;
import java.util.regex.Pattern;

public class ProductPage extends BasePage {
    private static final Pattern PRICE_PATTERN = Pattern.compile("(\\d[\\d\\s]*)\\s*zł");
    private static final By DEVICE_PRICE_VALUE = By.xpath(
            "//*[contains(@class,'StyledPriceInfo-sc')]//span[contains(@class,'variant_h4')]"
    );
    private static final By START_PAYMENT_VALUE = By.xpath("//aside//*[@data-qa='PRD_TotalUpfront']");
    private static final By MONTHLY_PAYMENT_VALUE = By.xpath("//aside//div[@class='priceRightSection']//div[@id='dyt_priceValue']");
    private static final By ADD_TO_BASKET_BUTTON = By.xpath(
            "//aside//button[@data-qa='PRD_AddToBasket']"
    );

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageOpen(String productName) {
        String currentUrl = driver.getCurrentUrl().toLowerCase(Locale.ROOT);
        String pageText = driver.getPageSource().toLowerCase(Locale.ROOT);
        String normalizedProductName = normalize(productName);

        boolean isPlaceholderName = normalizedProductName.contains("nazwa_")
                || normalizedProductName.contains("device")
                || normalizedProductName.contains("urządzenia")
                || normalizedProductName.contains("urzadzenia");

        if (isPlaceholderName) {
            return currentUrl.contains("produkt")
                    || currentUrl.contains("product")
                    || !(currentUrl.contains("/telefony/") || currentUrl.contains("/lista/produkty"))
                    || pageText.contains("kup teraz")
                    || pageText.contains("dodaj do koszyka")
                    || pageText.contains("specyfikacja");
        }

        return currentUrl.contains("/urzadzenie/")
                || currentUrl.contains("/device/")
                || currentUrl.contains("/produkt/")
                || currentUrl.contains("/product/")
                || currentUrl.contains("produkt")
                || currentUrl.contains("product")
                || pageText.contains(normalizedProductName)
                || pageText.contains("kup teraz")
                || pageText.contains("dodaj do koszyka")
                || pageText.contains("specyfikacja");
    }

    private String normalize(String value) {
        return value.toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").trim();
    }

    public String readDevicePrice() {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(DEVICE_PRICE_VALUE));
        String price = extractPrice(element.getText());
        if (price != null) {
            return price;
        }

        throw new IllegalStateException("Nie udało się odczytać ceny urządzenia ze strony produktu");
    }

    public String readStartPaymentPrice() {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(START_PAYMENT_VALUE));
        String price = extractPrice(element.getText());
        if (price != null) {
            return price;
        }
        throw new IllegalStateException("Nie udało się odczytać kwoty na start ze strony produktu");
    }

    public String readMonthlyPaymentPrice() {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(MONTHLY_PAYMENT_VALUE));
        String price = extractPrice(element.getText());
        if (price != null) {
            return price;
        }
        throw new IllegalStateException("Nie udało się odczytać kwoty miesięcznej ze strony produktu");
    }

    public void addToBasket() {
        WebElement button = getWait().until(ExpectedConditions.elementToBeClickable(ADD_TO_BASKET_BUTTON));
        try {
            button.click();
        } catch (RuntimeException ignored) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    private String extractPrice(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        java.util.regex.Matcher matcher = PRICE_PATTERN.matcher(text.replace('\u00A0', ' '));
        if (!matcher.find()) {
            return null;
        }

        return matcher.group(1).replaceAll("\\s+", "");
    }
}
