package pl.recruitment.qa.api.steps;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CurrencyRatesSteps {
    private static final String NBP_TABLE_A_URL = "http://api.nbp.pl/api/exchangerates/tables/A";

    private Scenario scenario;
    private Response response;
    private List<Map<String, Object>> rates;

    @Before
    public void captureScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    @When("Pobierz kursy walut")
    public void pobierzKursyWalut() {
        response = given()
                .accept("application/json")
                .when()
                .get(NBP_TABLE_A_URL);

        scenario.log("Kod odpowiedzi API NBP: " + response.statusCode());
        String responseJson = response.asPrettyString();
        scenario.attach(responseJson.getBytes(), "application/json", "Odpowiedz-API-NBP-JSON");

        assertTrue("API NBP zwróciło niepoprawny status: " + response.statusCode(),
                response.statusCode() >= 200 && response.statusCode() < 300);

        List<Map<String, Object>> tables = response.jsonPath().getList("$");
        assertNotNull("Brak danych tabel kursów w odpowiedzi", tables);
        assertFalse("Odpowiedź tabel kursów jest pusta", tables.isEmpty());

        rates = response.jsonPath().getList("[0].rates");
        assertNotNull("Brak pola rates w tabeli A", rates);
        assertFalse("Lista kursów w tabeli A jest pusta", rates.isEmpty());
    }

    @Then("^Wyświetl kurs dla waluty o kodzie: (.+)$")
    public void wyswietlKursDlaWalutyOKodzie(String currencyCode) {
        BigDecimal rate = findRateByCode(currencyCode);
        assertNotNull("Nie znaleziono waluty o kodzie: " + currencyCode, rate);
        scenario.log("Kurs dla waluty o kodzie " + currencyCode + ": " + rate);
    }

    @And("^Wyświetl kurs dla waluty o nazwie: (.+)$")
    public void wyswietlKursDlaWalutyONazwie(String currencyName) {
        BigDecimal rate = findRateByName(currencyName);
        assertNotNull("Nie znaleziono waluty o nazwie: " + currencyName, rate);
        scenario.log("Kurs dla waluty o nazwie " + currencyName + ": " + rate);
    }

    @And("^Wyświetl waluty o kursie powyżej: (.+)$")
    public void wyswietlWalutyOKursiePowyzej(String thresholdText) {
        BigDecimal threshold = new BigDecimal(thresholdText);
        List<String> currencies = findCurrenciesAbove(threshold);
        assertFalse("Brak walut o kursie powyżej: " + threshold, currencies.isEmpty());
        scenario.log("Waluty o kursie powyżej " + threshold + ":\n" + String.join(",\n", currencies));
    }

    @And("^Wyświetl waluty o kursie poniżej: (.+)$")
    public void wyswietlWalutyOKursiePonizej(String thresholdText) {
        BigDecimal threshold = new BigDecimal(thresholdText);
        List<String> currencies = findCurrenciesBelow(threshold);
        assertFalse("Brak walut o kursie poniżej: " + threshold, currencies.isEmpty());
        scenario.log("Waluty o kursie poniżej " + threshold + ":\n" + String.join(",\n", currencies));
    }

    private BigDecimal findRateByCode(String currencyCode) {
        for (Map<String, Object> rateEntry : rates) {
            String code = String.valueOf(rateEntry.get("code"));
            if (currencyCode.equalsIgnoreCase(code)) {
                return toBigDecimal(rateEntry.get("mid"));
            }
        }
        return null;
    }

    private BigDecimal findRateByName(String currencyName) {
        for (Map<String, Object> rateEntry : rates) {
            String name = String.valueOf(rateEntry.get("currency"));
            if (currencyName.equalsIgnoreCase(name)) {
                return toBigDecimal(rateEntry.get("mid"));
            }
        }
        return null;
    }

    private List<String> findCurrenciesAbove(BigDecimal threshold) {
        List<String> result = new ArrayList<>();
        for (Map<String, Object> rateEntry : rates) {
            BigDecimal mid = toBigDecimal(rateEntry.get("mid"));
            if (mid.compareTo(threshold) > 0) {
                result.add(rateEntry.get("code") + " (" + rateEntry.get("currency") + "): " + mid);
            }
        }
        return result;
    }

    private List<String> findCurrenciesBelow(BigDecimal threshold) {
        List<String> result = new ArrayList<>();
        for (Map<String, Object> rateEntry : rates) {
            BigDecimal mid = toBigDecimal(rateEntry.get("mid"));
            if (mid.compareTo(threshold) < 0) {
                result.add(rateEntry.get("code") + " (" + rateEntry.get("currency") + "): " + mid);
            }
        }
        return result;
    }

    private static BigDecimal toBigDecimal(Object value) {
        return new BigDecimal(String.valueOf(value));
    }
}
