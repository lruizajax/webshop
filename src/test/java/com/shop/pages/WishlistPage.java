package com.shop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WishlistPage extends BasePage {

    private static final String URL = "https://demowebshop.tricentis.com/wishlist";
    private By wishlistItems = By.cssSelector(".cart-item-row");
    private By wishlistQty = By.cssSelector(".qty-input");
    private By shareLink = By.cssSelector(".share-label");

    public WishlistPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        driver.get(URL);
    }

    public int getItemCount() {
        return driver.findElements(wishlistItems).size();
    }

    public boolean isProductInWishlist(String productName) {
        return isDisplayed(By.xpath("//a[@class='product-name' and contains(text(),'" + productName + "')]"));
    }

    public String getQuantityText() {
        return getText(wishlistQty);
    }
}
