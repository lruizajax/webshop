package com.shop.steps;

import com.shop.pages.HomePage;
import com.shop.pages.LoginPage;
import com.shop.utils.DriverFactory;
import com.shop.utils.Variables;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginSteps {

    private static final String VALID_EMAIL = "mruizg@gmail.com";
    private static final String VALID_PASSWORD = "*9Q>:ir7Tnb@V&d";

    private LoginPage loginPage() {
        return new LoginPage(DriverFactory.getDriver());
    }

    private HomePage homePage() {
        return new HomePage(DriverFactory.getDriver());
    }

    @Given("an unauthenticated user on the login page")
    public void anUnauthenticatedUserOnTheLoginPage() {
        loginPage().goTo();
    }

    @When("the user authenticates with valid credentials")
    public void theUserAuthenticatesWithValidCredentials() {
        loginPage().loginWithCredentials(VALID_EMAIL, VALID_PASSWORD);
    }

    @Then("the user should see their email in the account area")
    public void theUserShouldSeeTheirEmailInTheAccountArea() {
        String actualEmail = homePage().getLoggedInEmail();
        assert actualEmail.equals(VALID_EMAIL)
                : "Expected email '" + VALID_EMAIL + "' but found '" + actualEmail + "'";
    }

    @Then("the user should see the logout link")
    public void theUserShouldSeeTheLogoutLink() {
        assert homePage().isLogoutLinkDisplayed() : "Logout link is not displayed";
    }

    @When("the user logs out")
    public void theUserLogsOut() {
        homePage().clickLogout();
    }

    @When("the user authenticates with the registered credentials")
    public void theUserAuthenticatesWithRegisteredCredentials() {
        String email = Variables.getRegisteredEmail();
        String password = Variables.getRegisteredPassword();
        loginPage().loginWithCredentials(email, password);
    }

    @Then("the user should see the login link")
    public void theUserShouldSeeTheLoginLink() {
        assert homePage().isLoginLinkDisplayed() : "Login link is not displayed";
    }

    @Then("the user should be redirected to the home page")
    public void theUserShouldBeRedirectedToTheHomePage() {
        String currentUrl = homePage().getCurrentUrl();
        assert currentUrl.equals("https://demowebshop.tricentis.com/")
                || currentUrl.equals("https://demowebshop.tricentis.com")
                : "Not redirected to home page. Current URL: " + currentUrl;
    }

    @Given("an authenticated user in the system")
    public void anAuthenticatedUserInTheSystem() {
        loginPage().goTo();
        loginPage().loginWithCredentials(VALID_EMAIL, VALID_PASSWORD);
    }

    @When("the user attempts to authenticate with email {string} and password {string}")
    public void theUserAttemptsToAuthenticateWithEmailAndPassword(String email, String password) {
        loginPage().loginWithCredentials(email, password);
    }

    @Then("the system should reject the authentication")
    public void theSystemShouldRejectTheAuthentication() {
        boolean hasError = loginPage().isErrorMessageDisplayed();
        boolean onLoginPage = loginPage().isOnLoginPage();
        assert hasError || onLoginPage
                : "Expected authentication to be rejected but no error or redirect detected";
    }

    @Then("the user should remain on the login page")
    public void theUserShouldRemainOnTheLoginPage() {
        assert loginPage().isOnLoginPage() : "Expected to remain on login page";
    }

    @When("the user attempts to authenticate without providing credentials")
    public void theUserAttemptsToAuthenticateWithoutProvidingCredentials() {
        loginPage().loginWithCredentials("", "");
    }

    @When("the user attempts to authenticate with an invalid email")
    public void theUserAttemptsToAuthenticateWithAnInvalidEmail() {
        loginPage().loginWithCredentials("abc", "Password123");
    }

    @When("the user requests to recover their password")
    public void theUserRequestsToRecoverTheirPassword() {
        loginPage().clickForgotPassword();
    }

    @Then("the user should be redirected to the recovery page")
    public void theUserShouldBeRedirectedToTheRecoveryPage() {
        String currentUrl = loginPage().getCurrentUrl();
        assert currentUrl.contains("/passwordrecovery")
                : "Expected to be on password recovery page, but got: " + currentUrl;
    }
}
