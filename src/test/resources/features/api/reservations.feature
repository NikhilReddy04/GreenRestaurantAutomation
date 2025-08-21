Feature: Reservations Functionality
  Scenario: Successfully retrieve reservations
    Given The user wants to fetch reservations
    When The user sends a GET request to "/reservations"
    Then The response status code should be 200
    And The response should contain a list of reservations

  Scenario: Successfully cancel a booked reservation
    Given The user has their own reservation id
    When The user sends a DELETE reservations request to "/reservations"
    Then The response status code should be 200
    And The reservation should be cancelled

  Scenario: Successfully edit a booked reservation of an user
    Given The user wants to edit their booking
    When The user sends a PUT request to "/reservations/"
    Then The response status code should be 200
    And The response should contain a message "Reservation updated successfully"

  Scenario: Unsuccessful cancellation of a booked reservation from another user
    Given The user has another users reservation id
    When The user sends a DELETE reservations request to "/reservations"
    Then The response status code should be 403
    And The response should contain a message "You are not authorized to delete this reservation."

  Scenario: Unsuccessful cancellation of a booked reservation from logged out user
    Given The user has a reservation id and is not logged in
    When The user sends a DELETE reservations request to "/reservations"
    Then The response status code should be 401
   And The response should contain a message "Missing Authorization token"


  Scenario: Unsuccessful edit of a booked reservation done by another user
    Given The user wants to edit another persons booking
    When The user sends a PUT request to "/reservations/"
    Then The response status code should be 403
    And The response should contain a message "You are not authorized to modify this reservation."

#  Scenario Outline: Unsuccessful edit of a booked reservation by invalid entries
#    Given The user wants to edit a booking with invalid "<date>","<guestsCount>","<locationId>"
#    When The user sends a PUT request to "/reservations/"
#    Then The response status code should be 400
#    And The response should contain a message "<errorMessage>"
#    Examples:
#      | date     | guestsCount | locationId |errorMessage  |
#      |2024-04-01|     2       |loc123      |  date cannot be modified  |
#      |sameDay  |     -4      |loc123      |Guest number must be greater than 0|
#      |sameDay  |     2       |err123      |  location cannot be modified        |
#      |sameDay  |     10      |loc123      |number of guests cannot be more than the table capacity |
#      |sameDay  |     2       |loc124      | location cannot be modified |

#  Scenario: CleanUp
#      Given The user wants to cleanup all the reservations
