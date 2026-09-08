package com.propfind.steps;

import com.propfind.context.LoginTestContext;
import com.propfind.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for Login.feature (TC01–TC05).
 * All Selenium interactions are delegated to LoginPage — no driver calls here.
 * The shared LoginPage instance is provided via LoginTestContext.
 */
public class LoginSteps {

    private final LoginPage loginPage;

    public LoginSteps(LoginTestContext ctx) {
        this.loginPage = ctx.getLoginPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        loginPage.navigateTo(LoginTestContext.getBaseUrl());
    }

    // ── Setup (pre-conditions) ────────────────────────────────────────────────

    @Given("a user already exists with username {string} and password {string}")
    public void aUserAlreadyExistsWith(String username, String password) {
        loginPage.seedUser(username, password);
    }

    // ── Tab navigation ────────────────────────────────────────────────────────

    @When("I switch to the Register tab")
    public void iSwitchToRegisterTab() {
        loginPage.switchToRegisterTab();
    }

    // ── Register actions ──────────────────────────────────────────────────────

    @And("I enter full name {string}")
    public void iEnterFullName(String name) {
        loginPage.enterRegisterName(name);
    }

    @And("I enter registration username {string}")
    public void iEnterRegistrationUsername(String username) {
        loginPage.enterRegisterUsername(username);
    }

    @And("I enter registration password {string}")
    public void iEnterRegistrationPassword(String password) {
        loginPage.enterRegisterPassword(password);
    }

    @And("I enter confirm password {string}")
    public void iEnterConfirmPassword(String password) {
        loginPage.enterConfirmPassword(password);
    }

    @And("I submit the registration form")
    public void iSubmitRegistrationForm() {
        loginPage.submitRegistrationForm();
    }

    @And("I submit the registration form without filling any fields")
    public void iSubmitRegistrationFormEmpty() {
        loginPage.submitRegistrationFormEmpty();
    }

    // ── Login actions ─────────────────────────────────────────────────────────

    @When("I enter login username {string}")
    public void iEnterLoginUsername(String username) {
        loginPage.enterLoginUsername(username);
    }

    @And("I enter login password {string}")
    public void iEnterLoginPassword(String password) {
        loginPage.enterLoginPassword(password);
    }

    @And("I submit the login form")
    public void iSubmitLoginForm() {
        loginPage.submitLoginForm();
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("the account should be created successfully")
    public void theAccountShouldBeCreatedSuccessfully() {
        // Successful registration redirects to index.html
        String url = loginPage.getCurrentUrl();
        Assert.assertTrue(url.contains("index.html"),
                "Expected redirect to index.html after successful registration but got: " + url);
    }

    @Then("registration validation messages should be displayed")
    public void registrationValidationMessagesShouldBeDisplayed() {
        Assert.assertTrue(loginPage.areRequiredFieldsInvalid(),
                "Expected HTML5 validation to flag empty required fields on the register form");
    }

    @Then("a duplicate account error should be displayed")
    public void aDuplicateAccountErrorShouldBeDisplayed() {
        Assert.assertTrue(loginPage.isRegAlertVisible(),
                "Expected a duplicate-account error alert to be visible");
        Assert.assertTrue(loginPage.getRegAlertText().toLowerCase().contains("already exists"),
                "Expected alert to mention 'already exists' but got: " + loginPage.getRegAlertText());
    }

    @Then("the user should be logged in successfully")
    public void theUserShouldBeLoggedInSuccessfully() {
        String url = loginPage.getCurrentUrl();
        Assert.assertTrue(url.contains("index.html"),
                "Expected redirect to index.html after successful login but got: " + url);
    }

    @Then("a login error message should be displayed")
    public void aLoginErrorMessageShouldBeDisplayed() {
        Assert.assertTrue(loginPage.isLoginAlertVisible(),
                "Expected a login error alert to be visible");
        Assert.assertTrue(loginPage.getLoginAlertText().toLowerCase().contains("invalid"),
                "Expected alert to mention 'invalid' but got: " + loginPage.getLoginAlertText());
    }
}
