Feature: Change user password
  As a user
  I want to change my password
  So that I can keep my account secure

  Scenario: Successful password change
    Given a user exists with username "testUser"
    When I request to change password for "testUser" from "oldPassword123" to "newPassword123"
    Then the password should be changed successfully

  Scenario: Failed password change due to invalid username
    Given a user does not exist with username "unknownUser"
    When I  change password for "unknownUser" to "newPassword123"
    Then the password change should fail with a 400 error