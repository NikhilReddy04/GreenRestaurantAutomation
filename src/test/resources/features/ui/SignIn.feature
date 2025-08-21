Feature: Login Page

  @ui @smoke @regression
    Scenario: Successful login with valid credentials
      Given the user is on the login page
      When the user enters valid username and password
      And the user clicks on the login button
      Then the user should be redirected to the homepage

  @ui @regression
    Scenario: UnSuccessful login with invalid credentials
      Given the user is on the login page
      When the user enters invalid username and password
      And the user clicks on the login button
      Then a temporary notification should appear with message "Invalid email or password"

