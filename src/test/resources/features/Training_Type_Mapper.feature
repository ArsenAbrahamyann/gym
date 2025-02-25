Feature: TrainingTypeMapper

  Scenario: Map a list of TrainingTypeEntity to TrainingTypesResponseDto successfully
    Given a list of training type entities
    When I map these entities to response DTOs
    Then I should get a list of training types response DTOs

