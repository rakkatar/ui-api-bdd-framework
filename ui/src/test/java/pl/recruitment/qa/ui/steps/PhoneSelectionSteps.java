package pl.recruitment.qa.ui.steps;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.recruitment.qa.ui.browser.DriverFactory;
import pl.recruitment.qa.ui.browser.DriverManager;
import pl.recruitment.qa.core.ConfigProvider;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PhoneSelectionSteps {

    private WebDriver driver;

    @Before
    public void setUp() {
        if (DriverManager.getDriver() == null) {
            WebDriver newDriver = DriverFactory.create(System.getProperty("browser", "chrome"));
            DriverManager.setDriver(newDriver);
        }
        driver = DriverManager.getDriver();
    }

    @Given("Przejdź na stronę portalu")
    public void goToPortal() {
        String base = ConfigProvider.get("ui.baseUrl");
        if (base == null || base.isEmpty()) {
            throw new IllegalStateException("ui.baseUrl is not configured (check core/src/main/resources/config.yml or env/ JVM properties)");
        }
        driver.get(base);

        WebDriverWait wait = DriverManager.getWait();
        // 1) wait for header home link
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[aria-label='Strona główna T-Mobile']")));

        // 2) verify title contains expected token
        wait.until(ExpectedConditions.titleContains("T-Mobile"));
    }

    @When("wybieram pierwszy telefon z listy ofert")
    public void wybieramPierwszyTelefonZListyOfert() {
        WebDriverWait wait = DriverManager.getWait();
        // ensure phone category is selected (site loads items dynamically)
        try {
            WebElement phonesLink = driver.findElement(By.xpath("//a[normalize-space() = 'Phones']"));
            phonesLink.click();
        } catch (Exception ignored) {
            // link not present or already selected - continue
        }

        // wait until the tbody container has children (items are injected by JS)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#tbodyid > div")));

        List<WebElement> offerLinks = driver.findElements(By.cssSelector("#tbodyid a"));
        assertTrue("Lista ofert jest pusta", !offerLinks.isEmpty());
        offerLinks.get(0).click();
    }

    @Then("widoczny jest widok szczegółów wybranego telefonu")
    public void widocznyJestWidokSzczegolowWybranegoTelefonu() {
        WebDriverWait wait = DriverManager.getWait(Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("prod.html"));
        String currentUrl = driver.getCurrentUrl();
        assertTrue("Po kliknięciu w produkt nie otworzył się widok szczegółów", currentUrl.contains("prod.html"));
    }

    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
