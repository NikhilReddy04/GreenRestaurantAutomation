Feature: SignUp Functionality

  @api @smoke
  Scenario: Successful signUp with valid details
    Given User has a valid signUp payload
    When User sends a POST request to "/auth/sign-up"
    Then The response status code should be 201
    And The response should contain a message "User registered successfully!"

  @api @regression
  Scenario: Unsuccessful signUp with already registered email
    Given User has a signUp payload with existing email
    When User sends a POST request to "/auth/sign-up"
    Then The response status code should be 409
    And The response should contain an error message "A user with this email address already exists."


  @api @regression
  Scenario: Unsuccessful signup with empty firstname
    Given User has a signup payload with empty firstname
    When User sends a POST request to "/auth/sign-up"
    Then The response status code should be 400
    And The response should contain an error message "Invalid first name"

  @api @regression
  Scenario: Unsuccessful login with empty lastname
    Given User has a signup payload with empty lastname
    When User sends a POST request to "/auth/sign-up"
    Then The response status code should be 400
    And The response should contain an error message "Invalid last name"

  @api @regression
  Scenario Outline: Unsuccessful login with invalid password
    Given User has a signup payload with invalid password "<invalidPassword>"
    When User sends a POST request to "/auth/sign-up"
    Then The response status code should be 400
    And The response should contain an error message "Password must be 8-16 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
    Examples:
      | invalidPassword    | Reason                     |
      | password1!         | No uppercase               |
      | PASSWORD1!         | No lowercase               |
      | Password!          | No number                  |
      | Password1          | No special character       |
      | Pass1!             | Too short (< 8 characters) |
      | Password123456789! | Too long (> 16 characters) |

  @api @regression
  Scenario: Unsuccessful login with invalid email
    Given User has a signup payload with invalid email
    When User sends a POST request to "/auth/sign-up"
    Then The response status code should be 400
    And The response should contain an error message "Invalid email"








