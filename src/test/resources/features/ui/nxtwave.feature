@ui
Feature:Nxtwave login
  Scenario:Login functionality
    Given user is on Nxtwave LoginPage
    When User submits valid credentials
    Then User should login

  Scenario:Login functionality
    Given user is on Nxtwave LoginPage
    When User submits invalid credentials
    Then User should not login