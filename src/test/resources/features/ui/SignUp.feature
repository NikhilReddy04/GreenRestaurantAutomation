Feature: Sign up feature

@ui @smoke @regression
  Scenario: Successful Sign Up with valid data
    Given the user is on the signup page
      When the user enters "John" as first name, "Doe" as last name, "john.doe@example.com" as email, "Password@123" as password, and "Password@123" as confirm password
      And the user clicks the Sign Up button
    Then the user should be redirected to the homepage

  @ui @regression
  Scenario Outline: Unsuccessful Sign Up with invalid or missing data
    Given the user is on the signup page
    When the user enters "<firstName>" as first name, "<lastName>" as last name, "<email>" as email, "<password>" as password, and "<confirmPassword>" as confirm password
    And the user clicks the Sign Up button
    Then a temporary notification should appear with message "Please fix the errors in the form." on existing page

    Examples:
      | firstName | lastName | email             | password     | confirmPassword |
      | John      | Doe      | invalidemail      | Password@123 | Password@123     |
      | Alice     | Smith    | alice@example.com | Pass@1234    | Mismatch@1234    |
      |           | Smith    | alice@example.com | Pass@1234    | Pass@1234        |
      | John      |          | alice@example.com | Pass@1234    | Pass@1234        |
      | John      | Doe      |                   | Pass@1234    | Pass@1234        |
      | John      | Doe      | alice@example.com |              | Pass@1234        |
      | John      | Doe      | alice@example.com | Pass@1234    |                  |

#  @regression
#  Scenario Outline: Edge case sign up validation
#    When the user enters "<firstName>" as first name, "<lastName>" as last name, "<email>" as email, "<password>" as password, and "<confirmPassword>" as confirm password
#    And the user clicks the Sign Up button
#    Then a temporary notification should appear with message "Please fix the errors in the form." on existing page
#
#    Examples:expectedError             |
#      | "   "         | Doe           | john@example.com    | Pass@1234    | Pass@1234        |
#      | John          | "   "         | john@example.com    | Pass@1234    | Pass@1234        |
#      | John          | Doe           | verylongemailaddressverylongemailaddress@example.com | Pass@1234 | Pass@1234 |
