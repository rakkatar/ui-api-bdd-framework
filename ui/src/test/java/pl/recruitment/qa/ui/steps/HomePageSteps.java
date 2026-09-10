package pl.recruitment.qa.ui.steps;

import io.cucumber.java.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pl.recruitment.qa.core.ConfigProvider;
import pl.recruitment.qa.ui.context.ScenarioContext;
import pl.recruitment.qa.ui.pages.BasketPage;
import pl.recruitment.qa.ui.pages.CategoryPage;
import pl.recruitment.qa.ui.pages.HomePage;
import pl.recruitment.qa.ui.pages.ProductPage;

import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.recruitment.qa.ui.browser.DriverManager.getWait;

public class HomePageSteps extends StepsBase {
    public static final String DEVICE_PRICE_KEY = "devicePrice";
    public static final String START_PAYMENT_PRICE_KEY = "startPaymentPrice";
    public static final String MONTHLY_PAYMENT_PRICE_KEY = "monthlyPaymentPrice";
    private static final By LOAD_MORE_LOCATOR = By.xpath("//div[contains(@class,'LoadMoreWrapper')]/a");
    private static final By UNAVAILABLE_LOCATOR = By.xpath(
            "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'chwilowo niedostepne') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZĄĆĘŁŃÓŚŹŻ', 'abcdefghijklmnopqrstuvwxyząćęłńóśźż'), 'chwilowo niedostępne')]"
    );

    private CategoryPage categoryPage;
    private ProductPage productPage;
    private BasketPage basketPage;
    private Scenario scenario;

    @Before
    public void captureScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @Given("Przejdź na stronę główną T-Mobile")
    public void goToPortal() {
        String base = ConfigProvider.get("ui.baseUrl");
        HomePage homePage = new HomePage(driver());
        if (base == null || base.isEmpty()) {
            throw new IllegalStateException(
                    "ui.baseUrl is not configured (check core/src/main/resources/config.yml " +
                            "or env/ JVM properties)"
            );
        }

        driver().get(base);
        homePage.acceptCookieConsent();
        homePage.waitForHomePage();
    }

    @When("^Z górnej belki wybierz \"([^\"]+)\"$")
    public void chooseFromTopBar(String itemName) {
        HomePage homePage = new HomePage(driver());
        boolean dropdownVisible = homePage.openTopBarMenuItem(itemName);
        assertTrue(
                "Po kliknięciu w '" + itemName + "' nie pojawiła się rozwijana lista",
                dropdownVisible
        );
    }

    @When("^Kliknij \"([^\"]+)\" z sekcji \"([^\"]+)\"$")
    public void selectItemFromSection(String sectionOptionName, String sectionName) {
        HomePage homePage = new HomePage(driver());
        categoryPage = homePage.selectItemFromSection(sectionOptionName, sectionName);
        assertTrue(
                "Po kliknięciu w '" + sectionOptionName + "' z sekcji '" + sectionName +
                        "' nie została wybrana odpowiednia kategoria",
                categoryPage.isPageOpen(sectionOptionName)
        );
    }

    @When("^Kliknij element z listy o nazwie \"([^\"]+)\"$")
    public void clickItemFromList(String productName) {
        ensureProductIsAvailableInList(productName);

        String normalizedProductName = productName.trim();
        String beforeUrl = driver().getCurrentUrl();

        WebElement product = findClickableProductByName(normalizedProductName);
        HomePage homePage = new HomePage(driver());
        homePage.acceptCookieConsent();
        clickSafely(product);

        getWait().until(driver -> {
            String currentUrl = driver.getCurrentUrl();
            return !currentUrl.equals(beforeUrl) && isProductUrl(currentUrl);
        });

        homePage.acceptCookieConsentIfPresent();
        productPage = new ProductPage(driver());
        assertTrue(
                "Po kliknięciu w '" + productName + "' nie otworzyła się strona produktu",
                productPage.isPageOpen(productName)
                        || isProductUrl(driver().getCurrentUrl())
        );
        ScenarioContext.put(DEVICE_PRICE_KEY, productPage.readDevicePrice());
        ScenarioContext.put(START_PAYMENT_PRICE_KEY, productPage.readStartPaymentPrice());
        ScenarioContext.put(MONTHLY_PAYMENT_PRICE_KEY, productPage.readMonthlyPaymentPrice());
    }

    @When("^Kliknij \"Dodaj do koszyka\"$")
    public void addProductToBasket() {
        productPage.addToBasket();

        basketPage = new BasketPage(driver());
        assertTrue("Po kliknięciu 'Dodaj do koszyka' nie otworzył się koszyk", basketPage.isPageOpen());
    }

    @Then("^Ceny w koszyku zgadzają się z ekranem produktu$")
    public void verifyBasketPricesMatchProductPage() {
        String devicePrice = ScenarioContext.getString(DEVICE_PRICE_KEY);
        String startPaymentPrice = ScenarioContext.getString(START_PAYMENT_PRICE_KEY);
        String monthlyPaymentPrice = ScenarioContext.getString(MONTHLY_PAYMENT_PRICE_KEY);

        scenario.attach((
                "Cena urządzenia: " + devicePrice + " zł\n" +
                "Kwota na start: " + startPaymentPrice + " zł\n" +
                "Kwota miesięczna: " + monthlyPaymentPrice + " zł"
        ).getBytes(), "text/plain", "Porownanie-cen-produkt");

        assertEquals("Nie zgadza się pełna cena urządzenia w koszyku",
                devicePrice,
                basketPage.readDevicePrice());
        assertEquals("Nie zgadza się kwota na start w koszyku",
                startPaymentPrice,
                basketPage.readStartPaymentPrice());
        assertEquals("Nie zgadza się kwota miesięczna w koszyku",
                monthlyPaymentPrice,
                basketPage.readMonthlyPaymentPrice());
    }

    @And("^Kliknij \"Koszyk\"$")
    public void clickBasket() {
        HomePage homePage = new HomePage(driver());
        homePage.openBasket();
        basketPage = new BasketPage(driver());
        assertTrue("Po kliknięciu 'Koszyk' nie otworzył się koszyk", basketPage.isPageOpen());
    }

    @Then("^W koszyku znajduje się urządzenie \"([^\"]+)\"$")
    public void verifyDeviceExistsInBasket(String productName) {
        assertEquals("W koszyku jest inne urządzenie niż dodane",
                productName,
                basketPage.readItemTitle());
    }

    private void ensureProductIsAvailableInList(String productName) {
        By productLocator = productByName(productName);

        for (int i = 0; i < 20; i++) {
            List<WebElement> matchingProducts = driver().findElements(productLocator);
            WebElement matchingProduct = null;

            for (WebElement product : matchingProducts) {
                if (!product.isDisplayed()) {
                    continue;
                }
                String productAriaLabel = product.getAttribute("aria-label");
                if (productAriaLabel != null && productAriaLabel.contains(productName + ",")) {
                    matchingProduct = product;
                    break;
                }
            }

            if (matchingProduct != null) {
                String productText = (matchingProduct.getText() + " " + matchingProduct.getAttribute("aria-label") + " " + matchingProduct.getAttribute("alt") + " " + matchingProduct.getAttribute("title")).toLowerCase(Locale.ROOT);
                if (!productText.contains("chwilowo niedostepne") && !productText.contains("chwilowo niedostępne")) {
                    return;
                }
            }

            List<WebElement> loadMoreButtons = driver().findElements(LOAD_MORE_LOCATOR);
            if (!loadMoreButtons.isEmpty()) {
                WebElement button = loadMoreButtons.get(0);
                try {
                    String label = button.getText();
                    if (label != null && label.toLowerCase().contains("ładowanie")) {
                        getWait().until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(button, "Ładowanie")));
                        continue;
                    }
                    clickSafely(button);
                    continue;
                } catch (RuntimeException ignored) {
                    continue;
                }
            }

            throw new AssertionError("Nie znaleziono urządzenia '" + productName + "' na liście i przycisk 'Załaduj więcej' zniknął");
        }

        throw new AssertionError("Nie znaleziono urządzenia '" + productName + "' po maksymalnej liczbie prób");
    }

    private static By productByName(String productName) {
        String escapedProductName = productName.replace("'", "\\'");
        return By.xpath("//a[contains(@aria-label,'" + escapedProductName + ",')]");
    }

    private static boolean isProductUrl(String currentUrl) {
        return currentUrl.contains("/urzadzenie/")
                || currentUrl.contains("/device/")
                || currentUrl.contains("/produkt/")
                || currentUrl.contains("/product/");
    }

    private static boolean isAnyVisible(List<WebElement> elements) {
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    private WebElement findClickableProductByName(String productName) {
        List<WebElement> candidates = driver().findElements(productByName(productName));

        WebElement fallback = null;
        for (WebElement candidate : candidates) {
            if (!candidate.isDisplayed()) {
                continue;
            }

            WebElement productLink = findProductDetailLink(candidate);
            if (productLink != null) {
                return productLink;
            }

            if (fallback == null && isClickableTarget(candidate)) {
                fallback = candidate;
            }
        }

        if (fallback != null) {
            return fallback;
        }

        throw new AssertionError("Nie znaleziono klikalnego elementu z nazwą '" + productName + "'");
    }

    private static boolean isClickableTarget(WebElement element) {
        String tag = element.getTagName();
        return "a".equalsIgnoreCase(tag) || "button".equalsIgnoreCase(tag);
    }

    private static WebElement findProductDetailLink(WebElement candidate) {
        if (isClickableTarget(candidate) && looksLikeProductDetailLink(candidate.getAttribute("href"))) {
            return candidate;
        }

        List<WebElement> nestedLinks = candidate.findElements(By.xpath(".//*[self::a or self::button][@href]"));
        for (WebElement link : nestedLinks) {
            if (looksLikeProductDetailLink(link.getAttribute("href"))) {
                return link;
            }
        }

        return null;
    }

    private static boolean looksLikeProductDetailLink(String href) {
        if (href == null) {
            return false;
        }
        return href.contains("/urzadzenie/")
                || href.contains("/device/")
                || href.contains("/produkt/")
                || href.contains("/product/")
                || href.contains("/sklep/p/");
    }

    private void clickSafely(WebElement element) {
        try {
            element.click();
        } catch (RuntimeException ignored) {
            ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", element);
        }
    }
}
