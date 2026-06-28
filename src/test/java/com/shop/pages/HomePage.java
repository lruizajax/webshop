package com.shop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private static final String URL = "https://demowebshop.tricentis.com/";

    private By accountLink = By.cssSelector(".header-links .account");
    private By logoutLink = By.linkText("Log out");
    private By loginLink = By.linkText("Log in");
    private By cartCounter = By.cssSelector("span.cart-qty");
    private By wishlistCounter = By.cssSelector("span.wishlist-qty");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        driver.get(URL);
    }

    public void clickProductByName(String productName) {
        click(By.linkText(productName));
    }

    public String getProductUrl(String productName) {
        return driver.findElement(By.linkText(productName)).getAttribute("href");
    }

    public String getLoggedInEmail() {
        return getText(accountLink);
    }

    public boolean isEmailDisplayed(String email) {
        return waitForElement(accountLink).getText().equals(email);
    }

    public boolean isLogoutLinkDisplayed() {
        return isDisplayed(logoutLink);
    }

    public boolean isLoginLinkDisplayed() {
        return isDisplayed(loginLink);
    }

    public void clickLogout() {
        click(logoutLink);
    }

    public String getCartQuantity() {
        return getText(cartCounter).replaceAll("[()]", "");
    }

    public String getWishlistQuantity() {
        return getText(wishlistCounter).replaceAll("[()]", "");
    }

    public int getCartQuantityInt() {
        try {
            return Integer.parseInt(getCartQuantity());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void waitForCartToUpdate(int expectedQuantity) {
        try {
            wait.until(d -> {
                String text = d.findElement(cartCounter).getText().replaceAll("[()]", "");
                try {
                    return Integer.parseInt(text) >= expectedQuantity;
                } catch (NumberFormatException e) {
                    return false;
                }
            });
        } catch (Exception e) {
            System.out.println("  [cart] Timed out waiting for cart to reach " + expectedQuantity);
        }
    }
}
