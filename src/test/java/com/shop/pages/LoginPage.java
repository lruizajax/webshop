package com.shop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final String URL = "https://demowebshop.tricentis.com/login";

    private By emailInput = By.id("Email");
    private By passwordInput = By.id("Password");
    private By loginButton = By.cssSelector("input.button-1.login-button");
    private By errorMessage = By.cssSelector(".validation-summary-errors");
    private By forgotPasswordLink = By.linkText("Forgot password?");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        driver.get(URL);
    }

    public void loginWithCredentials(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        click(loginButton);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorMessage);
    }

    public void clickForgotPassword() {
        click(forgotPasswordLink);
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/login");
    }
}
