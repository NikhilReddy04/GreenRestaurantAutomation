Feature: Dishes Functionality

@api @smoke
  Scenario: Successfully retrieve popular dishes
    Given The user wants to fetch popular dishes
    When The user sends a GET request to "/dishes/popular"
    Then The response status code should be 200
    And The response should contain a list of dishes

@api @smoke
  Scenario Outline: Successfully retrieve dishes
    Given The user wants to fetch dishes of type "<type>" and sorted by "<sort>"
    When The user sends a GET request to "/dishes"
    Then The response status code should be 200
    And The response should contain a list of dishes sorted by "<sort>"
    Examples:
      | type      | sort     |
      |main courses |price,desc|
      |main courses |price,asc |
      |appetizers  |price,desc|
      |appetizers  |price,asc |
      |desserts    |price,desc|
      |desserts    |price,asc |

@api @regression
  Scenario Outline: Unsuccessful retrieval of dishes with invalid parameters
    Given The user wants to fetch dishes of type "<type>" and sorted by "<sort>"
    When The user sends a GET request to "/dishes"
    Then The response status code should be 400
    And The response should contain a message "<message>"
    Examples:
      | type      | sort     |message|
      |invalid    |price,desc|   DishType must be one of : Appetizers, Main Courses, Desserts  |

@api @regression
  Scenario Outline: Successfully retrieve a particular dish
    Given The user wants to fetch a particular dish
    When The user sends a GET request to "/dishes/<id>"
    Then The response status code should be 200
    And The response should contain dish details
    Examples:
      | id |
      |dish-001|
      |dish-002|
      |dish-003|
      |dish-004|
