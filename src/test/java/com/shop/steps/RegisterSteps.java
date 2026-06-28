package com.shop.steps;

import java.util.Map;

import com.shop.pages.HomePage;
import com.shop.pages.RegisterPage;
import com.shop.utils.DriverFactory;
import com.shop.utils.Variables;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.datafaker.Faker;

public class RegisterSteps {

    private static final Faker faker = new Faker();

    private RegisterPage registerPage() {
        return new RegisterPage(DriverFactory.getDriver());
    }

    private HomePage homePage() {
        return new HomePage(DriverFactory.getDriver());
    }

    @Given("a visitor on the registration page")
    public void aVisitorOnTheRegistrationPage() {
        registerPage().goTo();
    }

    @When("the visitor registers with random valid details")
    public void theVisitorRegistersWithRandomValidDetails() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase()
                + faker.number().numberBetween(1, 99999) + "@test.com";
        String password = faker.internet().password(8, 16, true, true, true);
        String gender = faker.bool().bool() ? "Male" : "Female";

        Variables.setRegisteredEmail(email);
        Variables.setRegisteredPassword(password);

        System.out.println("=== New Registration ===");
        System.out.println("Email:    " + email);
        System.out.println("Password: " + password);
        System.out.println("========================");

        registerPage().registerWithDetails(gender, firstName, lastName, email, password, password);
    }

    @When("the visitor registers with the following details:")
    public void theVisitorRegistersWithTheFollowingDetails(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        String email = data.get("email");
        String uniqueEmail = email.replace("@", "." + System.currentTimeMillis() + "@");
        registerPage().registerWithDetails(
                data.get("gender"),
                data.get("firstname"),
                data.get("lastname"),
                uniqueEmail,
                data.get("password"),
                data.get("password"));
    }

    @Given("an existing user registered with email {string}")
    public void anExistingUserRegisteredWithEmail(String email) {
        String uniqueEmail = email.replace("@", "." + System.currentTimeMillis() + "@");
        String password = faker.internet().password(8, 16, true, true, true);
        Variables.setRegisteredEmail(uniqueEmail);
        Variables.setRegisteredPassword(password);
        registerPage().goTo();
        registerPage().registerWithDetails("Male", faker.name().firstName(), faker.name().lastName(),
                uniqueEmail, password, password);
        if (registerPage().isRegistrationSuccessMessageDisplayed()) {
            registerPage().clickContinue();
            homePage().clickLogout();
        }
        registerPage().goTo();
    }

    @When("the visitor attempts to register with the email {string}")
    public void theVisitorAttemptsToRegisterWithEmail(String email) {
        String targetEmail = Variables.getRegisteredEmail();
        if (targetEmail == null) {
            targetEmail = email;
        }
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String password = faker.internet().password(8, 16, true, true, true);
        registerPage().registerWithDetails("Female", firstName, lastName, targetEmail, password, password);
    }

    @When("the visitor attempts to register with mismatched passwords")
    public void theVisitorAttemptsToRegisterWithMismatchedPasswords() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 16, true, true, true);
        registerPage().registerWithDetails("Male", firstName, lastName, email, password, password + "Mismatch");
    }

    @When("the visitor attempts to register without completing the required fields")
    public void theVisitorAttemptsToRegisterWithoutCompletingRequiredFields() {
        registerPage().clickRegister();
    }

    @When("the visitor attempts to register without providing {string}")
    public void theVisitorAttemptsToRegisterWithoutProviding(String missingField) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password(8, 16, true, true, true);

        registerPage().selectGender("Male");
        if (!missingField.equalsIgnoreCase("first name")) {
            registerPage().typeFirstName(firstName);
        }
        if (!missingField.equalsIgnoreCase("last name")) {
            registerPage().typeLastName(lastName);
        }
        if (!missingField.equalsIgnoreCase("email")) {
            registerPage().typeEmail(email);
        }
        if (!missingField.equalsIgnoreCase("password")) {
            registerPage().typePassword(password);
            registerPage().typeConfirmPassword(password);
        }
        registerPage().clickRegister();
    }

    @When("the visitor attempts to register with a password that is too short")
    public void theVisitorAttemptsToRegisterWithPasswordTooShort() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = faker.internet().emailAddress();
        registerPage().registerWithDetails("Female", firstName, lastName, email, "ab", "ab");
    }

    @When("the visitor completes a successful registration")
    public void theVisitorCompletesSuccessfulRegistration() {
        theVisitorRegistersWithRandomValidDetails();
    }

    @When("the visitor clicks continue")
    public void theVisitorClicksContinue() {
        registerPage().clickContinue();
    }

    @Then("the system should confirm the successful registration")
    public void theSystemShouldConfirmSuccessfulRegistration() {
        assert registerPage().isRegistrationSuccessMessageDisplayed()
                : "Registration success message not displayed";
    }

    @Then("the system should indicate the email already exists")
    public void theSystemShouldIndicateEmailAlreadyExists() {
        assert registerPage().isErrorMessageDisplayed()
                : "Error message not displayed for existing email";
    }

    @Then("the system should reject the registration due to password mismatch")
    public void theSystemShouldRejectRegistrationDueToPasswordMismatch() {
        assert registerPage().isValidationErrorDisplayed()
                : "Error message not displayed for password mismatch";
    }

    @Then("the system should display validation errors")
    public void theSystemShouldDisplayValidationErrors() {
        assert registerPage().isValidationErrorDisplayed()
                : "Validation errors not displayed";
    }

    @Then("the system should reject the password for not meeting length requirements")
    public void theSystemShouldRejectPasswordForNotMeetingLengthRequirements() {
        assert registerPage().isValidationErrorDisplayed()
                : "Validation error not displayed for short password";
    }

    @Then("the visitor should remain on the registration page")
    public void theVisitorShouldRemainOnTheRegistrationPage() {
        assert registerPage().isOnRegistrationPage()
                : "Not on the registration page";
    }

    @Then("the user should see their registered email in the account area")
    public void theUserShouldSeeTheirRegisteredEmailInTheAccountArea() {
        String expectedEmail = Variables.getRegisteredEmail();
        String actualEmail = homePage().getLoggedInEmail();
        assert actualEmail.equals(expectedEmail)
                : "Expected email '" + expectedEmail + "' but found '" + actualEmail + "'";
    }
}
