@regression @smoke @shopping
Feature: Shopping Process
  As a registered user of Demo Web Shop
  I want to browse, select products and complete a purchase
  In order to receive my products at home

  Background:
    Given an authenticated user in the system

  @smoke @happy-path-shopping
  Scenario: Add a product to the cart
    When the user adds the product "14.1-inch Laptop" to the cart
    Then the product should be in the cart
    And the cart counter should increase

  @smoke @happy-path-shopping
  Scenario: Add multiple products to the cart
    When the user adds several books to the cart from the Books category
    Then all selected books should be in the cart
    And the cart counter should reflect the total quantity

  @regression
  Scenario Outline: Add product with custom quantity
    When the user adds <quantity> unit(s) of "<product>" to the cart from "<category>"
    Then the cart should contain <quantity> unit(s) of "<product>"
    Examples:
      | category | product               | quantity |
      | Books    | Fiction                | 2        |
      | Books    | Fiction                | 1        |
      | Books    | Fiction                | 3        |

  @smoke @happy-path-shopping
  Scenario: View shopping cart
    Given the user has products in the cart
    When the user accesses their shopping cart
    Then the user should see the details of the added products
    And the subtotal should be calculated

  @regression
  Scenario: Modify product quantity in the cart
    Given the user has a product in the cart
    When the user changes the product quantity to "3"
    Then the subtotal should update according to the new quantity

  @negative @empty-cart
  Scenario: Empty the shopping cart
    Given the user has products in the cart
    When the user removes all products from the cart
    Then the cart should be empty
    And it should display the empty cart message

  @smoke @happy-path-shopping
  Scenario: Complete an order successfully
    Given the user has products in the cart
    When the user completes the checkout process
    Then the system should confirm the order successfully
    And the user should see the generated order number

  @regression @discount-coupon
  Scenario: Apply a discount coupon
    Given the user has products in the cart
    When the user applies a valid discount coupon
    Then the discount should be reflected in the cart total

  @regression @wishlist
  Scenario: Add product to wishlist
    When the user adds a product to their wishlist
    Then the product should appear in the wishlist
    And the wishlist counter should increase

  @smoke @happy-path-shopping
  Scenario: Browse product categories
    When the user navigates to category "Computers" and selects "Notebooks"
    Then the user should see the available notebook products

  @regression @gift-card
  Scenario: Purchase a personalized gift card
    When the user purchases a personalized gift card for "recipient@test.com"
    Then the gift card should be in the cart

  @negative @unauth-checkout
  Scenario: Attempt purchase without authentication
    Given an unauthenticated user
    When the user attempts to proceed to checkout
    Then the system should redirect the user to the login page
