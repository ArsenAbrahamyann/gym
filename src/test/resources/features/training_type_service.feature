Feature: Training Type Service

  Scenario: Retrieve an existing training type by name
    Given I have a training type named "Yoga"
    When I search for the training type by the name "Yoga"
    Then I should find the training type

  Scenario: Attempt to retrieve a non-existing training type by name
    Given I have no training type named "Pilates"
    When I search for the training type by the name "Pilates"
    Then I should not find any training type

  Scenario: Retrieve an existing training type by ID
    Given I have a training type with ID 101
    When I retrieve the training type by the ID 101
    Then I should get the training type details

  Scenario: Attempt to retrieve a non-existing training type by ID
    Given there is no training type with ID 102
    When I try to retrieve the training type by the ID 102
    Then I should receive a "TrainingType not found for ID: 102" error

  Scenario: Retrieve all training types when there are multiple types
    Given I have the following training types:
      | name  |
      | Yoga  |
      | Zumba |
    When I retrieve all training types
    Then I should get a list containing all the training types

  Scenario: Retrieve all training types when there are no types
    Given there are no training types available
    When I retrieve all training types
    Then I should receive an empty list