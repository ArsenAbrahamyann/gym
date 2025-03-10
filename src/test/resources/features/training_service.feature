Feature: Training Service Operations

  Scenario: Add a new training session successfully
    Given the trainee "john_doe" exists
    And the trainer "jane_smith" exists
    When I add a new training session with trainee "john_doe" and trainer "jane_smith"
    Then the training session should be saved
    And the trainer's workload should be updated

  Scenario: Add a new training session with missing trainee
    Given the trainee "john_doe" does not exist
    When I try to add a new training session with trainee "john_doe" and trainer "jane_smith"
    Then I should receive an error that the trainee is not found

  Scenario: Delete a training session successfully
    Given the training session with ID 1 exists
    When I delete the training session with ID 1
    Then the training session should be removed
    And the trainer's workload should be updated

  Scenario: Delete a training session that does not exist
    Given the training session with ID "999" does not exist
    When I try to delete the training session with ID "999"
    Then I should receive an error that the training session was not found

  Scenario: Retrieve trainings for a specific trainee
    Given the trainee "john_doe" has multiple training sessions
    When I request the trainings for trainee "john_doe"
    Then I should receive a list of training sessions for trainee "john_doe"

  Scenario: Retrieve trainings for a specific trainer
    Given the trainer "jane_smith" has conducted multiple training sessions
    When I request the trainings for trainer "jane_smith"
    Then I should receive a list of training sessions conducted by "jane_smith"
