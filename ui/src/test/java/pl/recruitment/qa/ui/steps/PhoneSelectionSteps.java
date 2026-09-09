package pl.recruitment.qa.ui.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pl.recruitment.qa.ui.pages.DashboardPage;
import pl.recruitment.qa.ui.pages.OfferPage;

import static org.junit.Assert.assertTrue;

public class PhoneSelectionSteps extends StepsBase {

    @When("wybieram pierwszy telefon z listy ofert")
    public void wybieramPierwszyTelefonZListyOfert() {
        // ensure phones category is selected (Dashboard helper will try clicking if present)
        new DashboardPage(driver()).clickPhonesCategory();

        OfferPage offerPage = new OfferPage(driver());
        offerPage.selectFirstOffer();
    }

    @Then("widoczny jest widok szczegółów wybranego telefonu")
    public void widocznyJestWidokSzczegolowWybranegoTelefonu() {
        OfferPage offerPage = new OfferPage(driver());
        boolean visible = offerPage.isDetailsVisible();
        assertTrue("Po kliknięciu w produkt nie otworzył się widok szczegółów", visible);
    }
}
