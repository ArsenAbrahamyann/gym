Feature: Trainee Controller
  As a user of the Gym system
  I want to manage trainees
  So that I can register, update profiles, toggle status, and retrieve information

  Background:
    Given the system knows about the following trainee
      | username | firstName | lastName | password | isActive |
      | trainee1 | John      | Doe      | password | true     |

  @RegisterTrainee
  Scenario: Register a new trainee
    When I register a trainee with details
      | firstName | lastName | dateOfBrith           | address       |
      | John      | Doe      | 2000-01-01T00:00:00   | 123 Main St   |
    Then the trainee registration response status should be 201

  @RegisterTraineeInvalidData
  Scenario: Register a trainee with invalid data
    When I register a trainee with invalid details
      | firstName | lastName |
      |           |          |
    Then the trainee registration response status should be 400

  @GetTraineeProfile
  Scenario: Retrieve trainee profile
    When I request the profile for trainee "trainee1"
    Then the trainee profile response status should be 200

  @GetTraineeProfileNotFound
  Scenario: Retrieve non-existent trainee profile
    When I request the profile for non-existent trainee "unknownTrainee"
    Then the trainee profile response status should be 404

  @UpdateTraineeProfile
  Scenario: Update trainee profile
    When I update the profile for trainee with details
      | username | firstName | lastName | dateOfBrith         | address       |
      | trainee1 | John      | Smith    | 2000-01-01T00:00:00 | 456 Elm St    |
    Then the update trainee profile response status should be 200

  @UpdateTraineeProfileInvalidData
  Scenario: Update trainee profile with invalid data
    When I update the profile for trainee with invalid details
      | username | firstName | lastName |
      | trainee1 | John      |          |
    Then the update trainee profile response status should be 400

  @ToggleTraineeStatus
  Scenario: Toggle trainee status
    When I toggle the status for trainee with details
      | username | isActive |
      | trainee1 | false    |
    Then the toggle trainee status response status should be 404

  @GetUnassignedTrainers
  Scenario: Retrieve unassigned trainers for trainee
    When I request the unassigned trainers for trainee "trainee1"
    Then the unassigned trainers response status should be 200

  @UpdateTrainerList
  Scenario: Update trainer list for trainee
    When I update the trainer list for trainee with details
      | traineeUsername | trainerUsername |
      | trainee1        | trainer1        |
    Then the update trainer list response status should be 200