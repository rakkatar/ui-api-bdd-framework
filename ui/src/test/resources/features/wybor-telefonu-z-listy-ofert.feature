Feature: Dodanie elementu do koszyka

  Scenario Outline: Wybranie telefonu z listy ofert
    Given Przejdź na stronę główną T-Mobile
    When Z górnej belki wybierz "Sklep"
    And Kliknij "Bez abonamentu" z sekcji "Smartfony"
    And Kliknij element z listy o nazwie "<nazwa_urzadzenia>"
    And Kliknij "Dodaj do koszyka"
    Then Ceny w koszyku zgadzają się z ekranem produktu
    When Przejdź na stronę główną T-Mobile
    When Kliknij "Koszyk"
    Then W koszyku znajduje się urządzenie "<nazwa_urzadzenia>"


    Examples:
      | nazwa_urzadzenia |
      | Samsung Galaxy S26 5G |
      | Xiaomi Redmi 15C 5G |

