Feature: Waiter Functionality
  Scenario: Successful retrieval of reservations by waiter
    Given Waiter books a table for a customer
    Then The user sends a GET request to "/waiter/reservations"
    Then The response status code should be 200
    And The response should contain a list of reservations created by waiter

  Scenario Outline: Unsuccessful retrieval of reservations by waiter with invalid parameters
    Given Waiter books a table for a customer and wants to fetch with invalid "<parameter>"
    Then The user sends a GET request to "/waiter/reservations"
    Then The response status code should be 400
    And The response should contain a message "<message>"
    Examples:
      | parameter | message|
      | date      |  can't travel in past  |
      |table      | this table doesn't belong to the location    |
      |   time    |  Invalid time format      |

  Scenario: Successful booking of a table by a waiter for a client
    Given Waiter wants to book a table for the client
    When User sends a POST request to "/bookings/waiter"
    Then The response status code should be 200
    And The response should contain a successful booking message

  Scenario: Successful booking of a table by a waiter for a visitor
    Given Waiter wants to book a table for a visitor
    When User sends a POST request to "/bookings/waiter"
    Then The response status code should be 200
    And The response should contain a successful booking message

#  Scenario Outline: Unsuccessful booking of a table by a waiter for a client
#    Given Waiter wants to book a table for the client with invalid "<parameter>"
#    When User sends a POST request to "/bookings/waiter"
#    Then The response status code should be <statusCode>
#    And The response should contain a message "<message>"
#    Examples:
#      | parameter       | message                                 | statusCode |
#      | date            | Invalid date format. Expected yyyy-MM-dd| 400        |
#      | past date       | Can't book in the past date             | 400        |
#      | guestNumber     | Guest numbers must be greater than 0    | 400        |
#      | locationId      | locationId doesn't exists!              | 400        |
#      | tableNumber     | Table not found for location: loc123    | 400        |
#      | incorrect email | Customer doesn't exist                  | 404        |
#      | incorrect type  | client type can be either existing or visitor| 400        |




  Scenario: Successful edit of a reservation by waiter
    Given Waiter books a table for a customer and has its reservation id
    When The user sends a PUT request to "/bookings/waiter/"
    Then The response status code should be 200
    And The response should contain a message "Reservation updated successfully"

#  Scenario Outline: Unsuccessful edit of a reservation by waiter with invalid parameters
#    Given Waiter books a table for a customer and has its reservation id
#    And Waiter sets invalid "<parameter>"
#    When The user sends a PUT request to "/bookings/waiter/"
#    Then The response status code should be 400
#    And The response should contain a message "<message>"
#    Examples:
#      | parameter | message |
#      | date | Cannot postpone to a past date |
#      | timeslot | timeFrom and timeTo do not match any valid slot |
#      |table  |  Table does not exist for the location             |
#      |reservation id|  no reservation exists  |

#  Scenario: CleanUp
#    Given The user wants to cleanup all the reservations
