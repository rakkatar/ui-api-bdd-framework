Feature: Kursy walut

  Scenario Outline: Kursy walut
    When Pobierz kursy walut
    Then Wyświetl kurs dla waluty o kodzie: <kod_waluty>
    And Wyświetl kurs dla waluty o nazwie: <nazwa_waluty>
    And Wyświetl waluty o kursie powyżej: <kurs_powyzej>
    And Wyświetl waluty o kursie poniżej: <kurs_ponizej>

    Examples:
      | kod_waluty | nazwa_waluty      | kurs_powyzej | kurs_ponizej |
      | USD        | dolar amerykański | 5            | 3            |
