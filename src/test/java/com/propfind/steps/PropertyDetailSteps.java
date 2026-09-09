package com.propfind.steps;

import com.propfind.context.PropertyDetailTestContext;
import com.propfind.pages.PropertyDetailPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for PropertyDetail.feature (TC21–TC30).
 * All Selenium interactions are delegated to PropertyDetailPage — no driver calls here.
 * The shared PropertyDetailPage instance is provided via PropertyDetailTestContext.
 */
public class PropertyDetailSteps {

    private final PropertyDetailPage page;

    public PropertyDetailSteps(PropertyDetailTestContext ctx) {
        this.page = ctx.getPropertyDetailPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the property detail page for property {int}")
    public void iAmOnThePropertyDetailPage(int propertyId) {
        page.navigateTo(PropertyDetailTestContext.getBaseUrl(), propertyId);
    }

    // ── TC21 — Open property details page ────────────────────────────────────

    @Then("the property detail content should be displayed")
    public void thePropertyDetailContentShouldBeDisplayed() {
        Assert.assertTrue(page.isDetailContentDisplayed(),
                "Expected the property detail content to be rendered inside #detail-content");
    }

    // ── TC22 — Verify price, location, area and BHK ──────────────────────────

    @Then("the property price should be displayed")
    public void thePropertyPriceShouldBeDisplayed() {
        String content = page.getDetailContentText();
        Assert.assertTrue(content.contains("₹"),
                "Expected property price (₹) to be visible in the detail page content");
    }

    @And("the property location should be displayed")
    public void thePropertyLocationShouldBeDisplayed() {
        String content = page.getDetailContentText();
        Assert.assertFalse(content.isBlank(),
                "Expected property location information to be present in the detail page content");
    }

    @And("the property area should be displayed")
    public void thePropertyAreaShouldBeDisplayed() {
        String content = page.getDetailContentText();
        Assert.assertTrue(content.contains("sq.ft"),
                "Expected property area (sq.ft) to be visible in the detail page content");
    }

    @And("the property BHK should be displayed")
    public void thePropertyBhkShouldBeDisplayed() {
        String content = page.getDetailContentText();
        Assert.assertTrue(content.contains("BHK"),
                "Expected BHK information to be visible in the detail page content");
    }

    // ── TC23 — View property images ───────────────────────────────────────────

    @Then("the main property image should be visible")
    public void theMainPropertyImageShouldBeVisible() {
        Assert.assertTrue(page.isMainImageVisible(),
                "Expected the main property image (#main-img) to be visible");
    }

    // ── TC24 — View virtual tour ──────────────────────────────────────────────

    @When("I click the virtual tour section")
    public void iClickTheVirtualTourSection() {
        page.clickVirtualTour();
    }

    @Then("the virtual tour content should be displayed")
    public void theVirtualTourContentShouldBeDisplayed() {
        Assert.assertTrue(page.isVirtualTourDisplayed(),
                "Expected the virtual tour section to display tour content after clicking");
    }

    // ── TC25 — View owner/agent information ──────────────────────────────────

    @Then("the contact owner button should be visible")
    public void theContactOwnerButtonShouldBeVisible() {
        Assert.assertTrue(page.isContactOwnerButtonVisible(),
                "Expected the 'Contact Owner' button (#btn-contact-owner) to be visible in the sidebar");
    }

    // ── TC26 — View nearby schools and hospitals ──────────────────────────────

    @Then("the nearby facilities section should display schools")
    public void theNearbyFacilitiesShouldDisplaySchools() {
        Assert.assertTrue(page.isSchoolDisplayed(),
                "Expected at least one school to appear in the nearby facilities section");
    }

    @And("the nearby facilities section should display hospitals")
    public void theNearbyFacilitiesShouldDisplayHospitals() {
        Assert.assertTrue(page.isHospitalDisplayed(),
                "Expected at least one hospital to appear in the nearby facilities section");
    }

    // ── TC27 — View police and fire stations ─────────────────────────────────

    @Then("the nearby facilities section should display a police station")
    public void theNearbyFacilitiesShouldDisplayPoliceStation() {
        Assert.assertTrue(page.isPoliceStationDisplayed(),
                "Expected 'Police Station' to appear in the nearby facilities section");
    }

    @And("the nearby facilities section should display a fire station")
    public void theNearbyFacilitiesShouldDisplayFireStation() {
        Assert.assertTrue(page.isFireStationDisplayed(),
                "Expected 'Fire Station' to appear in the nearby facilities section");
    }

    // ── TC28 — View supermarkets and metro ───────────────────────────────────

    @Then("the nearby facilities section should display supermarkets")
    public void theNearbyFacilitiesShouldDisplaySupermarkets() {
        Assert.assertTrue(page.isSupermarketDisplayed(),
                "Expected at least one supermarket to appear in the nearby facilities section");
    }

    @And("the nearby facilities section should display metro stations")
    public void theNearbyFacilitiesShouldDisplayMetroStations() {
        Assert.assertTrue(page.isMetroDisplayed(),
                "Expected at least one metro station to appear in the nearby facilities section");
    }

    // ── TC29 — View locality insights ────────────────────────────────────────

    @Then("the locality insights section should be displayed")
    public void theLocalityInsightsShouldBeDisplayed() {
        Assert.assertTrue(page.isLocalityInsightsDisplayed(),
                "Expected locality insight score rows (.score-row) to be rendered on the page");
    }

    // ── TC30 — Access emergency support ──────────────────────────────────────

    @Then("the nearby facilities section should display emergency facilities")
    public void theNearbyFacilitiesShouldDisplayEmergencyFacilities() {
        Assert.assertTrue(page.isEmergencyFacilitiesDisplayed(),
                "Expected both 'Police Station' and 'Fire Station' to appear as emergency facilities");
    }
}
