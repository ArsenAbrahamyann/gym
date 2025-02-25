Feature: Trainee Service

  Scenario: Successfully create a new trainee
    Given a trainee with username "john_doe" and password "securePass123"
    When the trainee is saved
    Then the trainee should be persisted in the repository

  Scenario: Toggle trainee status to active
    Given a trainee with username "john_doe" exists and is inactive
    When trainee status is toggled to active
    Then the trainee should be active

  Scenario: Fail to toggle status of non-existing trainee
    Given a trainee with username "non_existing_user" does not exist
    When trainee status is toggled
    Then an error should be thrown with message "Trainee not found for username: non_existing_user"

  Scenario: Delete a trainee successfully
    Given a trainee with username "john_doe" exists
    When trainee is deleted
    Then the trainee should no longer exist in the repository
