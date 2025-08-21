Feature: Locations Functionality

@api @smoke
  Scenario: Successfully retrieve locations
    Given The user wants to fetch locations
    When The user sends a GET request to "/locations"
    Then The response status code should be 200
    And The response should contain a list of locations

@api @smoke
  Scenario Outline: Successfully retrieve speciality dishes of particular location
    Given The user wants to fetch speciality dishes of a location
    When The user sends a GET request to "/locations/<locationId>/speciality-dishes"
    Then The response status code should be 200
    And The response should contain a list of speciality dishes
    Examples:
      | locationId |
      | loc123     |
      | loc124     |
      | loc125     |
      | loc126     |

@api @regression
  Scenario Outline: Successfully retrieve feedbacks of particular location
    Given The user wants to fetch "<type>" feedbacks of a location
    When The user sends a GET request to "/locations/<locationId>/feedbacks"
    Then The response status code should be 200
    And The response should contain a list of feedbacks
    Examples:
      | type  | locationId |
      |cuisine|loc123     |
      |service|loc124     |
      |cuisine|loc125     |
      |service|loc126     |
      |cuisine|loc123     |
      |service|loc126     |
      |cuisine|loc124     |
      |service|loc126     |

@api @regression
  Scenario Outline: Unsuccessful retrieval feedbacks of particular location with invalid parameters
    Given The user wants to fetch "<type>" feedbacks of a location
    When The user sends a GET request to "/locations/<locationId>/feedbacks"
    Then The response status code should be <statusCode>
    And The response should contain a message "<message>"
    Examples:
      | type  | locationId | statusCode | message |
      |invalid   |loc123     |   400    |    type must be either 'service' or 'cuisine'|
      |service   |err123     |   404    | Location not found with id: err123 |

    @api @smoke
  Scenario: Successfully retrieve locations select options
    Given The user wants to fetch locations select options
    When The user sends a GET request to "/locations"
    Then The response status code should be 200
    And The response should contain a list of locations select options
