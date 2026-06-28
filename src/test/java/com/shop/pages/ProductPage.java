package com.shop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {

    private static final String BASE_URL = "https://demowebshop.tricentis.com";

    private By addToCartButton = By.cssSelector("input.button-1.add-to-cart-button");
    private By addToWishlistButton = By.cssSelector("input.button-2.add-to-wishlist-button");
    private By productTitle = By.cssSelector(".product-title h1");
    private By quantityInput = By.cssSelector("input.qty-input");
    private By cartLink = By.cssSelector("a.cart-label");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void goToProduct(String productName) {
        String productSlug = productName.toLowerCase()
                .replace(" ", "-")
                .replace("$", "")
                .replace(".", "")
                .replace("--", "-");
        driver.get(BASE_URL + "/" + productSlug);
    }

    public void clickProductByName(String productName) {
        click(By.linkText(productName));
    }

    public void addToCart() {
        click(addToCartButton);
    }

    public void addToWishlist() {
        click(addToWishlistButton);
    }

    public void setQuantity(String quantity) {
        type(quantityInput, quantity);
    }

    public void fillGiftCardDetails(String recipientName, String recipientEmail,
                                      String senderName, String senderEmail, String message) {
        try {
            type(By.cssSelector("input.recipient-name"), recipientName);
            type(By.cssSelector("input.recipient-email"), recipientEmail);
            type(By.cssSelector("input.sender-name"), senderName);
            type(By.cssSelector("input.sender-email"), senderEmail);
            if (message != null && !message.isEmpty()) {
                type(By.cssSelector("textarea.message"), message);
            }
        } catch (Exception e) {
            System.out.println("  [giftcard] Could not fill gift card fields: " + e.getMessage());
        }
    }
}
