package pl.recruitment.qa.ui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pl.recruitment.qa.ui.browser.DriverManager;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PhoneSelectionSteps {

    @When("wybieram pierwszy telefon z listy ofert")
    public void wybieramPierwszyTelefonZListyOfert() {
        WebDriverWait wait = DriverManager.getWait();
        // ensure phone category is selected (site loads items dynamically)
        try {
            WebElement phonesLink = DriverManager.getDriver().findElement(By.xpath("//a[normalize-space() = 'Phones']"));
            phonesLink.click();
        } catch (Exception ignored) {
            // link not present or already selected - continue
        }

        // wait until the tbody container has children (items are injected by JS)
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#tbodyid > div")));

        List<WebElement> offerLinks = DriverManager.getDriver().findElements(By.cssSelector("#tbodyid a"));
        assertTrue("Lista ofert jest pusta", !offerLinks.isEmpty());
        offerLinks.get(0).click();
    }

    @Then("widoczny jest widok szczegółów wybranego telefonu")
    public void widocznyJestWidokSzczegolowWybranegoTelefonu() {
        WebDriverWait wait = DriverManager.getWait(Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("prod.html"));
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        assertTrue("Po kliknięciu w produkt nie otworzył się widok szczegółów", currentUrl.contains("prod.html"));
    }
}
