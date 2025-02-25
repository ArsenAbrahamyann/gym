Feature: Training Type Management

  Scenario: Successful retrieval of training types
    Given the system knows about the following training types
      | id | type     |
      | 1  | Aerobics |
      | 2  | Yoga     |
    When the client requests for some specific training type
    Then the response should contain the following training types
      | type     |
      | Aerobics |
      | Yoga     |

  Scenario: Internal server error when retrieving training types
    Given the training type service is down
    When the client requests for all training types
    Then the response should be an internal server error