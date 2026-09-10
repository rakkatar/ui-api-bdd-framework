package pl.recruitment.qa.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.By.xpath;
import static pl.recruitment.qa.ui.browser.DriverManager.getWait;
import static pl.recruitment.qa.ui.constants.UiConstants.HOME_PAGE_TITLE;

public class HomePage {
    private static final By CONSENT_BUTTON_LOCATOR = xpath(
            "//*[self::button or self::a or self::input][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'akceptuj') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'accept') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'zgadzam się') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'agree') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'zaakceptuj') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'accept all') or " +
                    "contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'zgoda')]"
    );
    private static final By COOKIE_POPUP_LOCATOR = By.cssSelector(
            "#didomi-popup, .didomi-popup-backdrop, .didomi-notice-popup, [id*=didomi], .cookie-banner, .cookies-banner, .cookie-consent, [class*=cookie], [class*=modal]"
    );
    private static final By BASKET_LINK = xpath("//a[contains(., 'Koszyk') or @href='/sklep/basket' or contains(@href, '/sklep/basket')]");
    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage waitForHomePage() {
        WebDriverWait wait = getWait();
        wait.until(ExpectedConditions.titleContains(HOME_PAGE_TITLE));
        return this;
    }

    /**
     * Actively clicks an "accept"/"allow" cookie consent button if present.
     * Falls back to no-op if no consent UI is detected.
     */
    public void acceptCookieConsent() {
        FluentWait<WebDriver> shortWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(6))
                .pollingEvery(Duration.ofMillis(250))
                .ignoring(ElementClickInterceptedException.class);

        for (int attempt = 0; attempt < 3; attempt++) {
            WebElement button = findFirstVisibleElement(CONSENT_BUTTON_LOCATOR);
            if (button != null) {
                clickConsent(button);
            }

            if (!isAnyVisible(driver.findElements(COOKIE_POPUP_LOCATOR))) {
                return;
            }

            try {
                shortWait.until(currentDriver -> !isAnyVisible(currentDriver.findElements(COOKIE_POPUP_LOCATOR)));
                return;
            } catch (RuntimeException ignored) {
                // retry because banner can re-render after the first click
            }
        }
    }

    public void acceptCookieConsentIfPresent() {
        if (isAnyVisible(driver.findElements(COOKIE_POPUP_LOCATOR)) || findFirstVisibleElement(CONSENT_BUTTON_LOCATOR) != null) {
            acceptCookieConsent();
        }
    }

    private WebElement findFirstVisibleElement(By locator) {
        List<WebElement> elements = driver.findElements(locator);
        for (WebElement element : elements) {
            if (element.isDisplayed() && element.isEnabled()) {
                return element;
            }
        }
        return null;
    }

    private void clickConsent(WebElement button) {
        try {
            button.click();
        } catch (RuntimeException ignored) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }

    private boolean isAnyVisible(List<WebElement> elements) {
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    public boolean openTopBarMenuItem(String itemName) {
        By menuTrigger = xpath(
                "//a[contains(normalize-space(.), '" + itemName + "')] | " +
                        "//button[contains(normalize-space(.), '" + itemName + "')]"
        );

        WebDriverWait wait = getWait();
        for (int attempt = 0; attempt < 3; attempt++) {
            acceptCookieConsentIfPresent();
            WebElement menuItem = wait.until(ExpectedConditions.elementToBeClickable(menuTrigger));
            try {
                menuItem.click();
                wait.until(ExpectedConditions.attributeContains(menuTrigger, "aria-expanded", "true"));
                return "true".equalsIgnoreCase(menuItem.getAttribute("aria-expanded"));
            } catch (ElementClickInterceptedException ignored) {
                acceptCookieConsentIfPresent();
            }
        }

        throw new IllegalStateException("Nie udało się kliknąć elementu menu '" + itemName + "'");
    }

    public CategoryPage selectItemFromSection(String sectionOptionName, String sectionName) {
        By sectionLocator = xpath(
                "//*[self::section or self::div or self::li or self::article or self::nav]" +
                        "[contains(normalize-space(.), '" + sectionName + "')]"
        );
        By itemLocator = xpath(
                "(//*[self::section or self::div or self::li or self::article or self::nav]" +
                        "[contains(normalize-space(.), '" + sectionName + "')])" +
                        "//*[self::a or self::button or self::span or self::div]" +
                        "[normalize-space(.) = '" + sectionOptionName + "']"
        );

        WebDriverWait wait = getWait();
        wait.until(ExpectedConditions.visibilityOfElementLocated(sectionLocator));
        WebElement item = wait.until(ExpectedConditions.elementToBeClickable(itemLocator));
        item.click();

        return CategoryPageFactory.createForCurrentPage(driver);
    }

    public void openBasket() {
        acceptCookieConsentIfPresent();
        WebElement basketLink = getWait().until(ExpectedConditions.elementToBeClickable(BASKET_LINK));
        basketLink.click();
    }

}
