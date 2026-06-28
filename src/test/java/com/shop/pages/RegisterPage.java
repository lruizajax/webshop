package com.shop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    private static final String URL = "https://demowebshop.tricentis.com/register";

    private By genderMale = By.id("gender-male");
    private By genderFemale = By.id("gender-female");
    private By firstNameInput = By.id("FirstName");
    private By lastNameInput = By.id("LastName");
    private By emailInput = By.id("Email");
    private By passwordInput = By.id("Password");
    private By confirmPasswordInput = By.id("ConfirmPassword");
    private By registerButton = By.id("register-button");
    private By successMessage = By.cssSelector(".result");
    private By continueButton = By.cssSelector("input.button-1.register-continue-button");
    private By errorSummary = By.cssSelector(".validation-summary-errors");
    private By fieldValidationErrors = By.cssSelector(".field-validation-error");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void goTo() {
        driver.get(URL);
    }

    public void selectGender(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            click(genderMale);
        } else {
            click(genderFemale);
        }
    }

    public void registerWithDetails(String gender, String firstName, String lastName,
                                    String email, String password, String confirmPassword) {
        selectGender(gender);
        type(firstNameInput, firstName);
        type(lastNameInput, lastName);
        type(emailInput, email);
        type(passwordInput, password);
        type(confirmPasswordInput, confirmPassword);
        click(registerButton);
    }

    public void clickRegister() {
        click(registerButton);
    }

    public void typeFirstName(String value) {
        type(firstNameInput, value);
    }

    public void typeLastName(String value) {
        type(lastNameInput, value);
    }

    public void typeEmail(String value) {
        type(emailInput, value);
    }

    public void typePassword(String value) {
        type(passwordInput, value);
    }

    public void typeConfirmPassword(String value) {
        type(confirmPasswordInput, value);
    }

    public boolean isRegistrationSuccessMessageDisplayed() {
        try {
            waitForElement(successMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSuccessMessage() {
        return getText(successMessage);
    }

    public void clickContinue() {
        click(continueButton);
    }

    public boolean isErrorMessageDisplayed() {
        return isDisplayed(errorSummary);
    }

    public boolean isValidationErrorDisplayed() {
        return isDisplayed(fieldValidationErrors) || isDisplayed(errorSummary);
    }

    public boolean isOnRegistrationPage() {
        return driver.getCurrentUrl().contains("/register");
    }
}
