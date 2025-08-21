Feature: Feedbacks Functionality
  @ui @smoke @regression
  Scenario: User should be able to give feedback
    Given the user is logged in and is in reservations page
    When The user gives feedback to a reservation
 #   Then the user should see feedback updated message
    And The feedback should be visible in restaurant's feedbacks