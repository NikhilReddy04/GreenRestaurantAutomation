Feature: reservation

  @ui @regression
  Scenario:Successfully cancel a booked reservation in ui
    Given The user booked a reservation and is in the reservations page
    When user cancels the reservation
    Then The reservation cancelled message should be displayed
    And The status of reservation should be "Cancelled"

  @ui @regression
  Scenario:Successfully edit a booked reservation in ui
    Given The user booked a reservation and is in the reservations page
    When user edits the reservation
    Then The reservation updated message should be displayed
    And The reservation should be edited
