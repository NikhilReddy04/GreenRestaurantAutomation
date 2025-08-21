Feature: Bookings Functionality

  Scenario: Successfully retrieve available tables for booking
    Given The user wants to fetch available tables
    When The user sends a GET request to "/bookings/tables"
    Then The response status code should be 200
    And The response should contain a list of tables

  Scenario: Successfully retrieve available tables for booking of current day
    Given The user wants to fetch available tables of current day
    When The user sends a GET request to "/bookings/tables"
    Then The response status code should be 200
    And The response should contain a list of tables with valid current day slots

  Scenario Outline: Validate error messages and while fetching bookings
    Given The user sets locationId "<locationId>", date "<date>", time "<time>", and guests "<guests>"
    When The user sends a GET request to "/bookings/tables"
    Then The response status code should be <statusCode>
    And The response should contain a message "<expectedMessage>"

    Examples:
      | locationId | date       | time   | guests | statusCode | expectedMessage                                 |
      | loc123     | 2024-04-01 | 12:00  | 2      | 400        | Sorry, can't travel to the past!                           |
      | loc123     | 2025-09-08 | 12:00  | -3     | 400        | Invalid number of guests                             |
      | locXYZ     | 2025-09-08 | 12:00  | 2      | 404        | sorry no location with this id available        |
      | loc123     | 2025-09-08 | 25:61  | 2      | 400        | Invalid time format. Expected format is HH:mm between 00:00 and 23:59                             |

  Scenario: Unsuccessful booking of a table without authorization token
    Given User wants to book a table without authorization token
    When User sends a POST request to "/bookings/client"
    Then The response status code should be 401
    And The response should contain a message "Missing Authorization token"

  Scenario: Unsuccessful booking of a table with invalid authorization token
    Given User wants to book a table with invalid authorization token
    When User sends a POST request to "/bookings/client"
    Then The response status code should be 401
    And The response should contain a message "Invalid or expired access token"


  Scenario Outline: Unsuccessful booking of a table with invalid body parameters
    Given User wants to book a table with valid authorization token and sets invalid "<invalidParam>"
    When User sends a POST request to "/bookings/client"
    Then The response status code should be <statusCode>
    And The response should contain a message "<message>"

    Examples:
      | invalidParam   | message                                 | statusCode |
      | locationId     | locationId doesn't exist!               | 400        |
      | date           | Can't book in the past date             | 400        |
      | guestsNumber   | Guest numbers must be greater than 0    | 400        |
      | tableNumber    | Table not found for location: loc123    | 400        |

  Scenario Outline: Successful booking of a table with valid authorization token and valid parameters
    Given User wants to book a table with valid authorization token and sets valid "<locationId>","<date>","<guestsNumber>"
    When User sends a POST request to "/bookings/client"
    Then The response status code should be <statusCode>
    And The response should contain a successful booking message
    When The User tries to book the table again
    Then The response should contain a message "Table already Reserved!"
    And User should not get the booked slot in the available slots
    Examples:
      | locationId | date   | guestsNumber | statusCode |
      |loc123      |tomorrow| 2            |200         |

  Scenario Outline: Unsuccessful booking of a table with valid authorization token and valid parameters
    Given User wants to book a table with valid authorization token and sets valid "<locationId>","<date>","<guestsNumber>"
    When User sends a POST request to "/bookings/client"
    Then The response status code should be <statusCode>
    And The response should contain a successful booking message
    When The User tries to book the table again
    Then The response should contain a message "Table already Reserved!"
    And User should not get the booked slot in the available slots
    Examples:
      | locationId | date   | guestsNumber | statusCode |
      |loc123      |tomorrow| 2            |200         |