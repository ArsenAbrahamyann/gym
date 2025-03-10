Feature: Trainer Service Operations

  Scenario: Successfully create a new trainer profile
    Given a new trainer data with valid details
    When I attempt to create the trainer profile
    Then the trainer profile should be created successfully

  Scenario: Successfully toggle trainer's active status
    Given an existing trainer with a specified username
    When I toggle the trainer's active status
    Then the trainer's status should be updated successfully

  Scenario: Fail to toggle active status due to non-existent trainer
    Given a non-existent trainer username
    When I toggle the trainer's active status
    Then the status toggling should fail with an error

  Scenario: Fail to update a trainer's profile for a non-existent user
    Given update details for a non-existent trainer
    When I try to update the trainer's profile
    Then the update should fail with an error message