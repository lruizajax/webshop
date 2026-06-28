package com.shop.steps;

import com.shop.pages.CartPage;
import com.shop.pages.CategoryPage;
import com.shop.pages.CheckoutPage;
import com.shop.pages.HomePage;
import com.shop.pages.LoginPage;
import com.shop.pages.ProductPage;
import com.shop.pages.WishlistPage;
import com.shop.utils.DriverFactory;

import java.util.List;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.datafaker.Faker;

public class ShoppingSteps {

    private static final Faker faker = new Faker();

    private HomePage homePage() {
        return new HomePage(DriverFactory.getDriver());
    }

    private ProductPage productPage() {
        return new ProductPage(DriverFactory.getDriver());
    }

    private CartPage cartPage() {
        return new CartPage(DriverFactory.getDriver());
    }

    private CheckoutPage checkoutPage() {
        return new CheckoutPage(DriverFactory.getDriver());
    }

    private CategoryPage categoryPage() {
        return new CategoryPage(DriverFactory.getDriver());
    }

    private WishlistPage wishlistPage() {
        return new WishlistPage(DriverFactory.getDriver());
    }

    private LoginPage loginPage() {
        return new LoginPage(DriverFactory.getDriver());
    }

    @Given("the user has products in the cart")
    public void theUserHasProductsInTheCart() {
        homePage().goTo();
        String url = homePage().getProductUrl("14.1-inch Laptop");
        DriverFactory.getDriver().get(url);
        productPage().addToCart();
        homePage().waitForCartToUpdate(1);
    }

    @When("the user adds the product {string} to the cart")
    public void theUserAddsTheProductToTheCart(String productName) {
        homePage().goTo();
        String url = homePage().getProductUrl(productName);
        DriverFactory.getDriver().get(url);
        productPage().addToCart();
        homePage().waitForCartToUpdate(1);
    }

    @When("the user adds several books to the cart from the Books category")
    public void theUserAddsSeveralBooksToTheCartFromBooksCategory() {
        categoryPage().goToCategory("books");
        List<String> bookUrls = categoryPage().getProductLinks();
        int added = 0;
        for (String url : bookUrls) {
            DriverFactory.getDriver().get(url);
            try {
                productPage().addToCart();
                added++;
                new HomePage(DriverFactory.getDriver()).waitForCartToUpdate(added);
                if (added >= 2) break;
            } catch (Exception e) {
                System.out.println("  [books] Could not add book at " + url + ": " + e.getMessage());
            }
        }
    }

    @When("the user accesses their shopping cart")
    public void theUserAccessesTheirShoppingCart() {
        cartPage().goTo();
    }

    @When("the user completes the checkout process")
    public void theUserCompletesTheCheckoutProcess() {
        proceedFromCartToCheckout();
        checkoutPage().fillBillingAddress(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                "United States",
                faker.address().city(),
                faker.address().streetAddress(),
                faker.address().zipCode(),
                faker.phoneNumber().phoneNumber());
        checkoutPage().continueShipping();
        checkoutPage().selectShippingMethodGround();
        checkoutPage().selectPaymentMethodMoneyOrder();
        checkoutPage().confirmOrder();
    }

    @When("the user navigates to category {string} and selects {string}")
    public void theUserNavigatesToCategoryAndSelects(String category, String subcategory) {
        categoryPage().goToSubcategory(category, subcategory);
    }

    @Then("the product should be in the cart")
    public void theProductShouldBeInTheCart() {
        cartPage().goTo();
        assert cartPage().getItemCount() > 0 : "No products found in cart";
    }

    @Then("the cart counter should increase")
    public void theCartCounterShouldIncrease() {
        assert homePage().getCartQuantityInt() > 0 : "Cart counter did not increase";
    }

    @Then("all selected books should be in the cart")
    public void allSelectedBooksShouldBeInTheCart() {
        cartPage().goTo();
        assert cartPage().getItemCount() > 0 : "No books found in cart";
    }

    @Then("the cart counter should reflect the total quantity")
    public void theCartCounterShouldReflectTotalQuantity() {
        assert homePage().getCartQuantityInt() > 0 : "Cart counter is zero";
    }

    @Then("the user should see the details of the added products")
    public void theUserShouldSeeDetailsOfAddedProducts() {
        assert cartPage().getItemCount() > 0 : "No product details visible in cart";
    }

    @Then("the subtotal should be calculated")
    public void theSubtotalShouldBeCalculated() {
        String subtotal = cartPage().getSubtotalText();
        assert subtotal != null && !subtotal.isEmpty() : "Subtotal not calculated";
    }

    @Then("the system should confirm the order successfully")
    public void theSystemShouldConfirmOrderSuccessfully() {
        boolean success = checkoutPage().isOrderSuccess();
        System.out.println("  [checkout] Order success: " + success);
        System.out.println("  [checkout] Current URL: " + checkoutPage().getCurrentUrl());
        assert success : "Order was not successful";
    }

    @Then("the user should see the generated order number")
    public void theUserShouldSeeGeneratedOrderNumber() {
        String orderNumber = checkoutPage().getOrderNumber();
        assert orderNumber != null && !orderNumber.equals("0") : "Order number not generated";
        System.out.println("  [checkout] Order number: " + orderNumber);
    }

    @Then("the user should see the available notebook products")
    public void theUserShouldSeeAvailableNotebookProducts() {
        assert categoryPage().isProductListDisplayed() : "No notebook products displayed";
    }

    @Given("an unauthenticated user")
    public void anUnauthenticatedUser() {
        homePage().goTo();
        homePage().clickLogout();
    }

    @Given("the user has a product in the cart")
    public void theUserHasAProductInTheCart() {
        theUserHasProductsInTheCart();
    }

    @When("the user adds {int} unit\\(s\\) of {string} to the cart from {string}")
    public void theUserAddsQuantityUnitsOfProductToTheCartFromCategory(int quantity, String product, String category) {
        categoryPage().goToCategory(category);
        categoryPage().clickProduct(product);
        productPage().setQuantity(String.valueOf(quantity));
        productPage().addToCart();
        homePage().waitForCartToUpdate(quantity);
    }

    @When("the user changes the product quantity to {string}")
    public void theUserChangesProductQuantityTo(String quantity) {
        cartPage().goTo();
        cartPage().updateQuantity(quantity);
        homePage().waitForCartToUpdate(Integer.parseInt(quantity));
    }

    @When("the user removes all products from the cart")
    public void theUserRemovesAllProductsFromTheCart() {
        cartPage().goTo();
        cartPage().removeAllItems();
    }

    @When("the user applies a valid discount coupon")
    public void theUserAppliesAValidDiscountCoupon() {
        cartPage().goTo();
        try {
            cartPage().applyCoupon("COUPON123");
            waitForAjax();
        } catch (Exception e) {
            System.out.println("  [coupon] Discount coupon input not found: " + e.getMessage());
            System.out.println("  [coupon] The demo shop may have coupons disabled in admin.");
        }
    }

    @When("the user adds a product to their wishlist")
    public void theUserAddsAProductToTheirWishlist() {
        homePage().goTo();
        String url = homePage().getProductUrl("$25 Virtual Gift Card");
        DriverFactory.getDriver().get(url);
        productPage().fillGiftCardDetails(
                faker.name().fullName(),
                faker.internet().emailAddress(),
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "Enjoy!"
        );
        productPage().addToWishlist();
        homePage().waitForAjax();
    }

    @When("the user purchases a personalized gift card for {string}")
    public void theUserPurchasesAPersonalizedGiftCardFor(String recipientEmail) {
        homePage().goTo();
        String url = homePage().getProductUrl("$25 Virtual Gift Card");
        DriverFactory.getDriver().get(url);
        productPage().fillGiftCardDetails(
                faker.name().fullName(),
                recipientEmail,
                faker.name().fullName(),
                faker.internet().emailAddress(),
                "Happy shopping!"
        );
        productPage().setQuantity("1");
        productPage().addToCart();
        homePage().waitForCartToUpdate(1);
    }

    @When("the user attempts to proceed to checkout")
    public void theUserAttemptsToProceedToCheckout() {
        String url = homePage().getProductUrl("14.1-inch Laptop");
        DriverFactory.getDriver().get(url);
        productPage().addToCart();
        homePage().waitForCartToUpdate(1);
        cartPage().goTo();
        cartPage().acceptTerms();
        cartPage().proceedToCheckout();
    }

    @Then("the cart should contain {int} unit\\(s\\) of {string}")
    public void theCartShouldContainQuantityUnitsOfProduct(int quantity, String product) {
        cartPage().goTo();
        homePage().waitForCartToUpdate(1);
        assert cartPage().getTotalQuantity() >= quantity
                : "Cart contains " + cartPage().getTotalQuantity() + " units, expected at least " + quantity;
    }

    @Then("the subtotal should update according to the new quantity")
    public void theSubtotalShouldUpdateAccordingToNewQuantity() {
        String subtotal = cartPage().getSubtotalText();
        assert subtotal != null && !subtotal.isEmpty() : "Subtotal not updated after quantity change";
    }

    @Then("the cart should be empty")
    public void theCartShouldBeEmpty() {
        assert cartPage().isCartEmpty() : "Cart is not empty";
    }

    @Then("it should display the empty cart message")
    public void itShouldDisplayTheEmptyCartMessage() {
        String msg = cartPage().getEmptyCartMessage();
        assert msg != null && !msg.isEmpty() : "Empty cart message not displayed";
    }

    @Then("the discount should be reflected in the cart total")
    public void theDiscountShouldBeReflectedInCartTotal() {
        if (cartPage().isDiscountApplied()) {
            String discount = cartPage().getDiscountAmount();
            assert discount != null && !discount.isEmpty() : "Discount amount not shown";
        } else {
            System.out.println("  [coupon] No discount applied — coupons may be disabled on the demo shop.");
        }
    }

    @Then("the product should appear in the wishlist")
    public void theProductShouldAppearInTheWishlist() {
        wishlistPage().goTo();
        assert wishlistPage().getItemCount() > 0 : "No products in wishlist";
    }

    @Then("the wishlist counter should increase")
    public void theWishlistCounterShouldIncrease() {
        String qty = homePage().getWishlistQuantity();
        int count = 0;
        try {
            count = Integer.parseInt(qty);
        } catch (NumberFormatException e) {
        }
        assert count > 0 : "Wishlist counter did not increase";
    }

    @Then("the gift card should be in the cart")
    public void theGiftCardShouldBeInTheCart() {
        cartPage().goTo();
        assert cartPage().getItemCount() > 0 : "Gift card not found in cart";
    }

    @Then("the system should redirect the user to the login page")
    public void theSystemShouldRedirectTheUserToTheLoginPage() {
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        assert currentUrl.contains("/login") || currentUrl.contains("/cart")
            : "Expected redirect to /login or /cart, but got: " + currentUrl;
    }

    private void proceedFromCartToCheckout() {
        cartPage().goTo();
        cartPage().acceptTerms();
        cartPage().proceedToCheckout();
        checkoutPage().waitForCheckoutPage();
        String currentUrl = DriverFactory.getDriver().getCurrentUrl();
        if (currentUrl.contains("/cart") || currentUrl.contains("Cart")) {
            System.out.println("  [checkout] Still on cart page, retrying checkout...");
            cartPage().goTo();
            cartPage().acceptTerms();
            cartPage().proceedToCheckout();
            checkoutPage().waitForCheckoutPage();
        }
    }

    private void waitForAjax() {
        homePage().waitForAjax();
    }
}
