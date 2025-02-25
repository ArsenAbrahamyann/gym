Feature: User Service

  Scenario: Failing to authenticate a user with incorrect password
    Given the user credentials are invalid
    When the user tries to log in with username "testuser" and password "wrongpassword"
    Then the authentication should fail

  Scenario: Successfully changing a user's password
    Given a registered user with username "testuser" and password "password123"
    When the user changes the password to "newPassword123"
    Then the password should be updated successfully

  Scenario: Failing to change the password of a non-existing user
    Given no user exists with username "nonexistinguser"
    When the user tries to change the password for "nonexistinguser"
    Then a "User not found" exception should be thrown

  Scenario: Checking if a user with username "testuser" exists
    Given a registered user with username "testuser"
    When I check if the user exists with username "testuser"
    Then the result should be true

  Scenario: Checking if a user with username "nonexistinguser" exists
    Given no user exists with username "nonexistinguser"
    When I check if the user exists with username "nonexistinguser"
    Then the result should be false
