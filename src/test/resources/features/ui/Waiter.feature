Feature: Waiter reservation

  @ui @smoke @regression
  Scenario:Successfully book a reservation in ui for a customer as a waiter
    Given The waiter is logged in and is in reservation page
    When the waiter creates a reservation for a customer
    Then The reservation confirmed message should be visible
    And The reservation should be available in the reservations

  @ui @regression
  Scenario:Successfully cancel a reservation of a customer in ui as a waiter
    Given Waiter created a reservation for a customer and wants to cancel it
    When the waiter cancels the reservation
    Then The reservation cancelled message should be visible
    And The reservation should not be available in the reservations

    @ui @regression
  Scenario:Successfully edit a reservation of a customer in ui as a waiter
    Given Waiter created a reservation for a customer and wants to edit it
    When the waiter edits the reservation
    Then The reservation postponed message should be visible
    And The reservation should be postponed


    @ui @regression
    Scenario: Unsuccessful reservation due to missing required credentials
      Given The waiter is logged in and is in reservation page
      When The waiter attempts to create a reservation without filling all required details
      Then The reservation should not be made

    @ui @regression
    Scenario: Unsuccessful reservation due to unregistered customer email
      Given The waiter is logged in and is in reservation page
      When The waiter enters an unregistered email for the customer while creating a reservation
      Then A validation error for customer non-existence should be shown and the reservation should not be created

    @ui @regression
    Scenario: Unsuccessful reservation due to invalid customer email
      Given The waiter is logged in and is in reservation page
      When The waiter enters an invalid email for the customer while creating a reservation
      Then A validation error for invalid email should be shown and the reservation should not be created


