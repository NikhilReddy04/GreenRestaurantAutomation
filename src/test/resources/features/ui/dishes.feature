Feature: Dishes Functionality

@ui @smoke @regression
  Scenario: User should see available dishes of a restaurant
    Given the user is on the home page
    When The user clicks on view menu button
    Then the user should see the available dishes on the page