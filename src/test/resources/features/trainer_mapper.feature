Feature: Trainer Mapper

  Scenario: Successfully map TrainerRegistrationRequestDto to TrainerEntity
    Given a trainer registration request with first name "John" and last name "Doe"
    When the request is mapped to a TrainerEntity
    Then the mapped entity should have username "john.doe"
    And the mapped entity should have an active status

  Scenario: Map TrainerEntity to GetTrainerProfileResponseDto
    Given a TrainerEntity with first name "John" and last name "Doe"
    When it is mapped to GetTrainerProfileResponseDto
    Then the response should contain first name "John"
    And should contain trainees list
