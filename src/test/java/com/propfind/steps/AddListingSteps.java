package com.propfind.steps;

import com.propfind.context.AddListingTestContext;
import com.propfind.pages.AddListingPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.File;

/**
 * Step definitions for AddListing.feature (TC61–TC65).
 * All Selenium interactions are delegated to AddListingPage — no driver calls here.
 * The shared AddListingPage instance is provided via AddListingTestContext.
 */
public class AddListingSteps {

    private final AddListingPage page;

    public AddListingSteps(AddListingTestContext ctx) {
        this.page = ctx.getAddListingPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the add listing page")
    public void iAmOnTheAddListingPage() {
        page.navigateTo(AddListingTestContext.getBaseUrl());
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I fill in the listing title {string}")
    public void iFillInTheListingTitle(String title) {
        page.enterTitle(title);
    }

    @And("I select the listing purpose {string}")
    public void iSelectTheListingPurpose(String purpose) {
        page.selectPurpose(purpose);
    }

    @And("I select the listing type {string}")
    public void iSelectTheListingType(String type) {
        page.selectType(type);
    }

    @And("I enter the listing price {string}")
    public void iEnterTheListingPrice(String price) {
        page.enterPrice(price);
    }

    @And("I select the listing BHK {string}")
    public void iSelectTheListingBhk(String bhk) {
        page.selectBhk(bhk);
    }

    @And("I select the listing city {string}")
    public void iSelectTheListingCity(String city) {
        page.selectCity(city);
    }

    @And("I enter the listing locality {string}")
    public void iEnterTheListingLocality(String locality) {
        page.enterLocality(locality);
    }

    @And("I enter the listing pincode {string}")
    public void iEnterTheListingPincode(String pincode) {
        page.enterPincode(pincode);
    }

    @And("I enter the listing area {string}")
    public void iEnterTheListingArea(String area) {
        page.enterArea(area);
    }

    @And("I enter the owner name {string}")
    public void iEnterTheOwnerName(String ownerName) {
        page.enterOwnerName(ownerName);
    }

    @And("I accept the listing terms")
    public void iAcceptTheListingTerms() {
        page.acceptTerms();
    }

    @And("I submit the listing form")
    public void iSubmitTheListingForm() {
        page.submitListingForm();
    }

    @When("I submit the listing form without filling any fields")
    public void iSubmitTheListingFormEmpty() {
        page.submitListingFormEmpty();
    }

    @And("I upload a property photo")
    public void iUploadAPropertyPhoto() {
        // Use a small valid image file bundled with the project for testing
        File samplePhoto = new File("src/test/resources/sample-photo.jpg");
        page.uploadPhoto(samplePhoto.getAbsolutePath());
    }

    @And("I navigate back to the add listing page")
    public void iNavigateBackToTheAddListingPage() {
        page.navigateTo(AddListingTestContext.getBaseUrl());
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("the listing should be created successfully")
    public void theListingShouldBeCreatedSuccessfully() {
        Assert.assertTrue(page.isSuccessModalVisible(),
                "Expected the success modal to be visible after successful listing submission");
    }

    @Then("listing validation messages should be displayed")
    public void listingValidationMessagesShouldBeDisplayed() {
        Assert.assertTrue(page.areRequiredFieldsInvalid(),
                "Expected HTML5 validation to flag empty required fields on the listing form");
    }

    @Then("the photo preview should be displayed")
    public void thePhotoPreviewShouldBeDisplayed() {
        Assert.assertTrue(page.isPhotoPreviewPopulated(),
                "Expected at least one thumbnail to appear in the photo preview strip after upload");
    }

    @Then("the success modal should be visible")
    public void theSuccessModalShouldBeVisible() {
        Assert.assertTrue(page.isSuccessModalVisible(),
                "Expected the success confirmation modal to be visible");
    }
}
