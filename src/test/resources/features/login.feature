@regression @smoke @login
Feature: User Login
  As a registered user of Demo Web Shop
  I want to log into my account
  In order to access my orders and make purchases

  Background:
    Given an unauthenticated user on the login page

  @smoke @happy-path @login
  Scenario: Successful login with valid credentials
    When the user authenticates with valid credentials
    Then the user should see their email in the account area
    And the user should see the logout link

  @negative @invalid-login
  Scenario Outline: Login with invalid credentials
    When the user attempts to authenticate with email "<email>" and password "<password>"
    Then the system should reject the authentication
    And the user should remain on the login page
    Examples:
      | email                 | password        |
      | nonexistent@test.com  | WrongPass1      |
      | user@test.com         |                 |
      |                       | secret123       |

  @negative @empty-credentials
  Scenario: Login with empty credentials
    When the user attempts to authenticate without providing credentials
    Then the system should reject the authentication
    And the user should remain on the login page

  @negative @malformed-email
  Scenario: Login with malformed email
    When the user attempts to authenticate with an invalid email
    Then the system should reject the authentication

  @regression @password-recovery
  Scenario: Password recovery
    When the user requests to recover their password
    Then the user should be redirected to the recovery page

  @smoke @happy-path @logout
  Scenario: Successful logout
    Given an authenticated user in the system
    When the user logs out
    Then the user should be redirected to the home page
    And the user should see the login link
