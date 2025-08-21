Feature: Locations functionality


  @ui @smoke @regression
  Scenario: User should be able to see locations
    Given the user is the home page
    When user goes to Location Page
    And clicks on Book Table
    Then user should see Booking Page
