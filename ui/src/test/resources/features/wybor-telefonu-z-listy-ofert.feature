Feature: Portal - Dodawanie elementów do koszyka - użytkownik niezalogowany

  Scenario: Wybranie telefonu z listy ofert
    Given Przejdź na stronę portalu
    When wybieram pierwszy telefon z listy ofert
    Then widoczny jest widok szczegółów wybranego telefonu
