Feature: Feedbacks Functionality
  @api @smoke
  Scenario: Successfully leave a feedback for a particular reservation
    Given The user wants to leave feedback
    When User sends a POST request to "/feedbacks"
    Then The response status code should be 200
    And The response should contain a message "Feedback updated successfully"

@api @regression
  Scenario: Unsuccessful feedback by another user
    Given The user wants to leave feedback for another user's reservation
    When User sends a POST request to "/feedbacks"
    Then The response status code should be 401
    And The response should contain a message "Unauthorized to make feedback"

@api @regression
  Scenario Outline: Unsuccessful feedback by invalid parameters
    Given The user wants to leave feedback with invalid "<parameter>"
    When User sends a POST request to "/feedbacks"
    Then The response status code should be <statusCode>
    And The response should contain a message "<message>"
    Examples:
      | parameter            | statusCode  | message                              |
      | emptyCuisineComment  | 200         | Feedback updated successfully        |
      | cuisineRating        | 400         | Rating Should be within 1 to 5       |
      | reservationId        | 400         | Reservation doesn't exist            |
      | serviceRating        | 400         | Rating Should be within 1 to 5       |
      | emptyServiceComment  | 200         | Feedback updated successfully        |
      | emptyCuisineRating   | 400         | Must provide ratings                 |
      | emptyServiceRating   | 400         | Must provide ratings                 |
      | emptyReservationId   | 400         | Missing field: reservationId         |





