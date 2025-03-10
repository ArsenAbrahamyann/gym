Feature: Trainer Controller
  As a user of the Gym system
  I want to manage trainers
  So that I can register, update profiles, toggle status, and retrieve information

  Background:
    Given the system knows about the following trainer
      | username   | firstName | lastName | password | isActive |
      | trainerA   | John      | Doe      | password | true     |

  @RegisterTrainer
  Scenario: Register a new trainer
    When I register a trainer with details
      | firstName     | lastName | trainingTypeId |
      | John          | Doe      | 1              |
    Then the trainer registration response status should be 201

  @RegisterTrainerInvalidData
  Scenario: Register a trainer with invalid data
    When I register a trainer with invalid details
      | firstName | lastName | trainingTypeId |
      |           | Doe      |                |
    Then the trainer registration response status should be 400

  @GetTrainerProfile
  Scenario: Retrieve trainer profile
    When I request the profile for trainer "trainerA"
    Then the trainer profile response status should be 200

  @GetTrainerProfileNotFound
  Scenario: Retrieve non-existent trainer profile
    When I request the profile for non-existent trainer "unknownTrainer"
    Then the trainer profile response status should be 404

  @UpdateTrainerProfile
  Scenario: Update trainer profile
    When I update the profile for trainer with details
      | username | firstName | lastName | trainingTypeId |
      | trainerA | John      | Smith    | 2              |
    Then the update trainer profile response status should be 200

  @UpdateTrainerProfileInvalidData
  Scenario: Update trainer profile with invalid data
    When I update the profile for trainer with invalid details
      | username | firstName | lastName | trainingTypeId |
      | trainerA | John      |          | 2              |
    Then the update trainer profile response status should be 400

  @ToggleTrainerStatus
  Scenario: Toggle trainer status
    When I toggle the status for trainer with details
      | username | isActive |
      | trainerA | false    |
    Then the toggle trainer status response status should be 200

  @TrainerWorkload
  Scenario: Retrieve trainer workload
    When I request the workload for trainer "trainerA" for month 1
    Then the trainer workload response status should be 200

  @TrainerWorkloadTimeout
  Scenario: Retrieve trainer workload with service timeout
    When I request the workload for trainer "trainerA" for month 1 with timeout
    Then the trainer workload response status should be 408