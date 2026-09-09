# Framework UI i API BDD

Ten projekt to wielomodulowy framework Maven do automatyzacji testów UI i API z wykorzystaniem Cucumber i Selenium.

## Wersja Javy

Używaj Javy 21.

## Struktura projektu

Projekt jest zbudowany w architekturze modułowej:

- `core` - wspólne konfiguracje, utilsy i helpery; tu trzymamy kod wspólny dla wszystkich modułów
- `ui` - testy UI z Selenium i Cucumber; odpowiedzialny za scenariusze użytkownika w przeglądarce
- `api` - testy API dla endpointów REST (np. NBP); odpowiedzialny za wywołania HTTP i walidację odpowiedzi

Najprostsza zasada jest taka:

- `core` dostarcza wspólne narzędzia i ustawienia
- `ui` obsługuje interakcje z przeglądarką
- `api` obsługuje wywołania do serwisów zewnętrznych

Dzięki temu testy są czytelne, łatwiej jest je utrzymywać i nie mieszamy logiki wspólnej z testami konkretnego typu.

## Uruchomienie wszystkich testów

```bash
mvn clean test
```

## Uruchomienie tylko testów modułu UI

```bash
mvn -q -pl ui -am clean test -Dbrowser=chrome
```

## Uruchomienie testów UI w przeglądarce Firefox

```bash
mvn -q -pl ui -am clean test -Dbrowser=firefox
```

## Uruchomienie w trybie headless

```bash
mvn -q -pl ui -am clean test -Dbrowser=chrome -Dheadless=true
```

## Ważne informacje

- `-pl ui` wybiera moduł `ui`
- `-am` buduje również zależne moduły, np. `core`
- `-Dbrowser=chrome` lub `-Dbrowser=firefox` wybiera przeglądarkę
- `-Dheadless=true` uruchamia przeglądarkę bez widocznego okna

## Przykładowy feature

W module UI znajduje się przykładowy feature:

- `ui/src/test/resources/features/browser_selection.feature`

Test otwiera stronę i sprawdza tytuł strony.
