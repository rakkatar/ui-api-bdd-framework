package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.regex.Pattern;

public class BasketPage extends BasePage {
    private static final Pattern PRICE_PATTERN = Pattern.compile("(\\d[\\d\\s]*)\\s*zł");
    private static final String BASKET_URL = "https://www.t-mobile.pl/sklep/basket";
    private static final By DEVICE_PRICE = By.xpath("//*[@data-qa='BKT_Activation']");
    private static final By START_PAYMENT_PRICE = By.xpath("//*[@data-qa='BKT_ItemlupFront']");
    private static final By MONTHLY_PAYMENT_PRICE = By.xpath("//*[@data-qa='BKT_ItemMonthly']");
    private static final By ITEM_TITLE = By.xpath("//div[@data-qa='BKT_ItemTitle0']");

    public BasketPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPageOpen() {
        getWait().until(ExpectedConditions.urlContains("/sklep/basket"));
        return driver.getCurrentUrl().startsWith(BASKET_URL);
    }

    public String readDevicePrice() {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(DEVICE_PRICE));
        String price = extractPrice(element.getText());
        if (price == null) {
            throw new IllegalStateException("Nie udało się odczytać pełnej ceny urządzenia z koszyka");
        }
        return price;
    }

    public String readStartPaymentPrice() {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(START_PAYMENT_PRICE));
        return extractDigits(element.getText(), "kwoty na start");
    }

    public String readMonthlyPaymentPrice() {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(MONTHLY_PAYMENT_PRICE));
        return extractDigits(element.getText(), "kwoty miesięcznej");
    }

    public String readItemTitle() {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(ITEM_TITLE));
        return element.getText().trim();
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

    private String extractDigits(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new IllegalStateException("Nie udało się odczytać " + fieldName + " z koszyka");
        }

        String digits = text.replaceAll("\\D+", "");
        if (digits.isEmpty()) {
            throw new IllegalStateException("Nie udało się odczytać " + fieldName + " z koszyka");
        }

        return digits;
    }
}
