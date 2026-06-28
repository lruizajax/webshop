@regression @smoke @register
Feature: User Registration
  As a visitor of Demo Web Shop
  I want to create a new account
  In order to make purchases and manage my orders

  Background:
    Given a visitor on the registration page

  @smoke @happy-path
  Scenario: Successful registration with random data
    When the visitor registers with random valid details
    Then the system should confirm the successful registration
    And the user should see their registered email in the account area
    And the user should see the logout link

  @regression
  Scenario Outline: Successful registration of a new user with predefined data
    When the visitor registers with the following details:
      | gender   | <gender>   |
      | firstname| <firstname>|
      | lastname | <lastname> |
      | email    | <email>    |
      | password | <password> |
    Then the system should confirm the successful registration
    And the user should see the logout link
    Examples:
      | gender | firstname | lastname | email                    | password   |
      | Male   | John      | Doe      | john.doe@test.com        | Pass1234   |
      | Female | Jane      | Smith    | jane.smith@test.com      | Secure99!  |
      | Male   | Carlos    | Lopez    | carlos.lopez@demo.com    | Demo2024#  |

  @negative @already-registered
  Scenario: Registration with an already registered email
    Given an existing user registered with email "existing@test.com"
    When the visitor attempts to register with the email "existing@test.com"
    Then the system should indicate the email already exists

  @negative @password-mismatch
  Scenario: Registration with mismatched passwords
    When the visitor attempts to register with mismatched passwords
    Then the system should reject the registration due to password mismatch

  @negative @missing-fields-register
  Scenario: Registration without completing required fields
    When the visitor attempts to register without completing the required fields
    Then the system should display validation errors
    And the visitor should remain on the registration page

  @negative @missing-fields-register
  Scenario Outline: Registration with individual missing fields
    When the visitor attempts to register without providing "<missing_field>"
    Then the system should display validation errors

    Examples:
      | missing_field |
      | first name    |
      | last name     |
      | email         |
      | password      |

  @regression
  Scenario: Registration with password too short
    When the visitor attempts to register with a password that is too short
    Then the system should reject the password for not meeting length requirements

  @smoke @happy-path
  Scenario: Successful registration and continuation
    When the visitor completes a successful registration
    And the visitor clicks continue
    Then the user should be redirected to the home page
