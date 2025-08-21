Feature: API SignIn Functionality

  @signin @smoke
  Scenario: Successful signIn with valid credentials
    Given User has a valid signIn payload
    When User sends a POST request to "/auth/sign-in"
    Then The response status code should be 200
    And The response should contain a token


#  @signin @regression
#  Scenario Outline: Unsuccessful signIn attempts with invalid or incomplete credentials
#    Given User has a signIn payload with "<email>" and "<password>"
#    When User sends a POST request to "/auth/sign-in"
#    Then The response status code should be <statusCode>
#    And The response should contain an error message "<errorMessage>"
#
#    Examples:
#      | email                  | password     | statusCode | errorMessage                    |
#      | invalid@example.com    | wrongpass    | 403        | Invalid email or password       |
#      |                        | somepassword | 400        | email is not valid              |
#      | invalid-email-format   | somepassword | 400        | email is not valid              |
#      | valid@example.com      |              | 400        | Password is empty!              |
#      | notfound@example.com   | somepassword | 403        | Invalid email or password       |
