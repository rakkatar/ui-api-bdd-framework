# Framework UI i API BDD

Ten projekt to wielomodulowy framework Maven do automatyzacji testów UI i API z wykorzystaniem Cucumber i Selenium.

## Wersja Javy

Używaj Javy 21.

## Struktura projektu

Projekt jest zbudowany w architekturze modułowej:

- `core` - wspólne konfiguracje, utilsy i helpery; tu trzymamy kod wspólny dla wszystkich modułów
- `ui` - testy UI z Selenium i Cucumber; odpowiedzialny za scenariusze użytkownika w przeglądarce
- `api` - testy API dla endpointów REST (np. NBP); odpowiedzialny za wywołania HTTP i walidację odpowiedzi

## Uruchomienie wszystkich testów

```bash
mvn clean test
```

## Uruchomienie testów API (NBP)

```bash
mvn -q -pl api -am test
```

## Generowanie raportu Allure dla API

Po uruchomieniu testów API wygeneruj raport:

```bash
mvn -q -pl api -DskipTests allure:report
```

Raport HTML zostanie zapisany w:

- `api/target/site/allure-maven-plugin/allure-maven.html`

Na Windows możesz otworzyć raport komendą:

```bash
start api\target\site\allure-maven-plugin\allure-maven.html
```

## Uruchomienie tylko testów modułu UI

```bash
mvn -q -pl ui -am clean test -Dbrowser=chrome
```

## Uruchomienie w trybie headless

```bash
mvn -q -pl ui -am clean test -Dbrowser=chrome -Dheadless=true
```

## Ważne informacje

- `-pl ui` wybiera moduł `ui`
- `-am` buduje również zależne moduły, np. `core`
- `-Dbrowser=chrome` wskazuje przeglądarkę
- `-Dheadless=true` uruchamia przeglądarkę bez widocznego okna

## Przykładowe feature

W module UI znajduje się przykładowy feature:

- `ui/src/test/resources/features/wybor-telefonu-z-listy-ofert.feature`

W module API znajduje się przykładowy feature:

- `api/src/test/resources/features/kursy-walut.feature`
