Feature: Training Controller
  As a user of the Gym system
  I want to manage trainings
  So that I can add, delete, and retrieve trainings for a trainee or trainer

  Background:
    Given the service knows about the following trainings
      | id | trainingDate         |
      | 1  | 2023-10-18T10:15:30  |
      | 2  | 2023-10-19T11:00:00  |

  @GetTrainingsForTrainee
  Scenario: Retrieve training list for a specific trainee
    Given the following trainings exist for the trainee
      | id | trainingDate         |
      | 1  | 2023-10-18T10:15:30  |
      | 2  | 2023-10-20T11:00:00  |
    When I request the training list for trainee "trainee1"
    Then the response status should be 200
    And the response should contain 2 training records

  @GetTrainingsForTraineeNoResults
  Scenario: Retrieve training list for a trainee with no trainings
    Given no trainings exist for the trainee
    When I request the training list for trainee "trainee2"
    Then the response status should be 404

  @DeleteTraining
  Scenario: Delete a training session
    Given a training with ID 1 exists
    When I request to delete a training with ID 1
    Then the delete response status should be 200

  @DeleteTrainingNonExistent
  Scenario: Attempt to delete a non-existent training session
    Given no trainings exist for the trainee
    When I request to delete a training with ID 999
    Then the delete response status should be 404

  @AddTraining
  Scenario: Add a new training session
    When I add a training with details
      | parameter        | value               |
      | traineeUsername  | trainee1            |
      | trainerUsername  | trainerA            |
      | trainingDate     | 2023-10-18T10:15:30 |
      | trainingDuration | 60                  |
    Then the add training response status should be 201

  @AddTrainingInvalidData
  Scenario: Attempt to add a training session with invalid data
    When I add a training with details
      | parameter        | value               |
      | traineeUsername  |                     |
      | trainerUsername  | trainerA            |
      | trainingDate     | 2023-10-18T10:15:30 |
      | trainingDuration | 60                  |
    Then the add training response status should be 400