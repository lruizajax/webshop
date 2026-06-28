package com.shop.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private static final String URL = "https://demowebshop.tricentis.com/cart";

    private By cartItems = By.cssSelector(".cart-item-row");
    private By subtotal = By.cssSelector(".product-subtotal");
    private By cartTotal = By.cssSelector(".order-total strong");
    private By emptyCartMessage = By.cssSelector(".order-summary-content");
    private By termsCheckbox = By.id("termsofservice");
    private By checkoutButton = By.id("checkout");
    private By couponCodeInput = By.id("discountcouponcode");
    private By applyCouponButton = By.cssSelector("input.button-2.apply-discount-coupon-code-button");
    private By updateCartButton = By.cssSelector("input.button-2.update-cart-button");
    private By removeCheckbox = By.cssSelector("input[name*='removefromcart']");
    private By cartQty = By.cssSelector("span.cart-qty");
    private By unitPrice = By.cssSelector(".product-unit-price");
    private By discountAmount = By.cssSelector(".discount-amount");
    private By couponMessage = By.cssSelector(".coupon-code .message");
    private By productNameLinks = By.cssSelector("a.product-name");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        driver.get(URL);
    }

    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    public boolean isProductInCart(String productName) {
        return isDisplayed(By.xpath("//a[@class='product-name' and contains(text(),'" + productName + "')]"));
    }

    public String getSubtotalText() {
        try {
            wait.until(d -> {
                List<WebElement> items = d.findElements(subtotal);
                return !items.isEmpty() && !items.get(0).getText().isEmpty();
            });
        } catch (Exception e) {
            System.out.println("  [cart] Subtotal did not update within timeout");
        }
        List<WebElement> items = driver.findElements(subtotal);
        if (!items.isEmpty()) {
            return items.get(0).getText();
        }
        return "";
    }

    public boolean isCartEmpty() {
        return isDisplayed(emptyCartMessage);
    }

    public String getEmptyCartMessage() {
        return getText(emptyCartMessage);
    }

    public void acceptTerms() {
        click(termsCheckbox);
    }

    public void proceedToCheckout() {
        click(checkoutButton);
    }

    public void applyCoupon(String couponCode) {
        type(couponCodeInput, couponCode);
        click(applyCouponButton);
    }

    public void updateQuantity(String quantity) {
        List<WebElement> qtyInputs = driver.findElements(By.cssSelector(".qty-input"));
        if (!qtyInputs.isEmpty()) {
            qtyInputs.get(0).clear();
            qtyInputs.get(0).sendKeys(quantity);
        }
        click(updateCartButton);
    }

    public void removeAllItems() {
        List<WebElement> checkboxes = driver.findElements(removeCheckbox);
        for (WebElement cb : checkboxes) {
            if (!cb.isSelected()) {
                cb.click();
            }
        }
        click(updateCartButton);
    }

    public String getUnitPrice() {
        List<WebElement> items = driver.findElements(unitPrice);
        if (!items.isEmpty()) return items.get(0).getText();
        return "";
    }

    public int getTotalQuantity() {
        List<WebElement> qtyInputs = driver.findElements(By.cssSelector(".qty-input"));
        int total = 0;
        for (WebElement input : qtyInputs) {
            try {
                total += Integer.parseInt(input.getAttribute("value"));
            } catch (NumberFormatException e) {
            }
        }
        return total;
    }

    public String getDiscountAmount() {
        if (isDisplayed(discountAmount)) {
            return getText(discountAmount);
        }
        return "";
    }

    public boolean isDiscountApplied() {
        return isDisplayed(discountAmount);
    }
}
