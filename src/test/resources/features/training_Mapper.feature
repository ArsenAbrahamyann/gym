Feature: TrainingMapper

  Scenario: Successfully map a list of TrainingEntity to TrainingResponseDto
    Given I have a list of TrainingEntity objects
    When I convert them to TrainingResponseDto objects
    Then I should receive a list of TrainingResponseDto objects matching the TrainingEntity objects details

  Scenario: Attempt to map an empty list of TrainingEntity to TrainingResponseDto
    Given I have an empty list of TrainingEntity objects
    When I convert them to TrainingResponseDto objects
    Then I should receive an empty list of TrainingResponseDto objects

  Scenario: Successfully map AddTrainingRequestDto to a new TrainingEntity
    Given I have a valid AddTrainingRequestDto object
    When I convert it to a TrainingEntity object
    Then I should receive a TrainingEntity object matching the AddTrainingRequestDto details
