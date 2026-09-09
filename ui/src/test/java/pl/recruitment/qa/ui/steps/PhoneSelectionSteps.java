package pl.recruitment.qa.ui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pl.recruitment.qa.ui.pages.DashboardPage;
import pl.recruitment.qa.ui.pages.OfferPage;
import pl.recruitment.qa.ui.browser.DriverManager;

import static org.junit.Assert.assertTrue;

public class PhoneSelectionSteps {

    @When("wybieram pierwszy telefon z listy ofert")
    public void wybieramPierwszyTelefonZListyOfert() {
        // ensure phones category is selected (Dashboard helper will try clicking if present)
        new DashboardPage(DriverManager.getDriver()).clickPhonesCategory();

        OfferPage offerPage = new OfferPage(DriverManager.getDriver());
        offerPage.selectFirstOffer();
    }

    @Then("widoczny jest widok szczegółów wybranego telefonu")
    public void widocznyJestWidokSzczegolowWybranegoTelefonu() {
        OfferPage offerPage = new OfferPage(DriverManager.getDriver());
        boolean visible = offerPage.isDetailsVisible();
        assertTrue("Po kliknięciu w produkt nie otworzył się widok szczegółów", visible);
    }
}
