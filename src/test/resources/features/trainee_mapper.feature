Feature: TraineeMapper Functionality

  Scenario: Map TraineeRegistrationRequestDto to TraineeEntity
    Given a valid TraineeRegistrationRequestDto
    When the request is mapped to a TraineeEntity
    Then the TraineeEntity should have correct values

  Scenario: Map invalid TraineeRegistrationRequestDto to TraineeEntity
    Given an invalid TraineeRegistrationRequestDto
    When the request is mapped to a TraineeEntity
    Then an exception should be thrown