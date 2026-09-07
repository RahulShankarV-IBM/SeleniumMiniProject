import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for PropertyDetail.feature (TC21–TC30).
 * All Selenium interactions delegated to PropertyDetailPage.
 */
public class PropertyDetailSteps {

    private final PropertyDetailPage page;

    public PropertyDetailSteps(PropertyDetailTestContext ctx) {
        this.page = ctx.getPropertyDetailPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the property detail page for property {int}")
    public void iAmOnThePropertyDetailPageFor(int propertyId) {
        page.navigateTo(PropertyDetailTestContext.getBaseUrl(), propertyId);
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I click on the virtual tour section")
    public void iClickOnTheVirtualTourSection() {
        page.clickVirtualTour();
    }

    // ── TC21 — Open property details page ────────────────────────────────────

    @Then("the property detail content should be displayed")
    public void thePropertyDetailContentShouldBeDisplayed() {
        Assert.assertTrue(page.isDetailContentDisplayed(),
                "Expected #detail-content to be visible and populated");
    }

    @And("the property title should be visible")
    public void thePropertyTitleShouldBeVisible() {
        Assert.assertTrue(page.isTitleVisible(),
                "Expected property title (h1) to be visible in #detail-content");
    }

    @And("the property location should be visible")
    public void thePropertyLocationShouldBeVisible() {
        Assert.assertTrue(page.isLocationVisible(),
                "Expected property location to be visible");
    }

    // ── TC22 — Verify price, location, area and BHK ──────────────────────────

    @Then("the property price should be displayed")
    public void thePropertyPriceShouldBeDisplayed() {
        Assert.assertTrue(page.isPriceDisplayed(),
                "Expected property price (.price-big) to be displayed");
    }

    @And("the property area should be displayed")
    public void thePropertyAreaShouldBeDisplayed() {
        Assert.assertTrue(page.isAreaDisplayed(),
                "Expected area in sq.ft to be shown in property details");
    }

    @And("the property BHK count should be displayed")
    public void thePropertyBhkCountShouldBeDisplayed() {
        Assert.assertTrue(page.isBhkDisplayed(),
                "Expected BHK count to be shown in property details");
    }

    // ── TC23 — View property images ───────────────────────────────────────────

    @Then("the main property image should be loaded")
    public void theMainPropertyImageShouldBeLoaded() {
        Assert.assertTrue(page.isMainImageLoaded(),
                "Expected #main-img to be visible with a non-empty src");
    }

    @And("the image gallery thumbnails should be visible")
    public void theImageGalleryThumbnailsShouldBeVisible() {
        Assert.assertTrue(page.areGalleryThumbsVisible(),
                "Expected gallery thumbnail images to be rendered");
    }

    // ── TC24 — Virtual tour ───────────────────────────────────────────────────

    @Then("the virtual tour content should be displayed")
    public void theVirtualTourContentShouldBeDisplayed() {
        Assert.assertTrue(page.isVirtualTourContentLoaded(),
                "Expected virtual tour section to show iframe or tour heading after click");
    }

    // ── TC25 — Owner/agent information ───────────────────────────────────────

    @Then("the owner name should be displayed")
    public void theOwnerNameShouldBeDisplayed() {
        Assert.assertTrue(page.isOwnerNameDisplayed(),
                "Expected owner name 'Rajesh Kumar' to appear in the property details");
    }

    @And("the owner contact button should be visible")
    public void theOwnerContactButtonShouldBeVisible() {
        Assert.assertTrue(page.isContactOwnerButtonVisible(),
                "Expected #btn-contact-owner to be visible");
    }

    // ── TC26 — Nearby schools and hospitals ──────────────────────────────────

    @Then("nearby schools should be listed")
    public void nearbySchoolsShouldBeListed() {
        Assert.assertTrue(page.areNearbySchoolsListed(),
                "Expected nearby school 'Delhi Public School' to be listed");
    }

    @And("nearby hospitals should be listed")
    public void nearbyHospitalsShouldBeListed() {
        Assert.assertTrue(page.areNearbyHospitalsListed(),
                "Expected nearby hospital 'Manipal Hospital' to be listed");
    }

    // ── TC27 — Emergency facilities ───────────────────────────────────────────

    @Then("nearby police station information should be displayed")
    public void nearbyPoliceStationShouldBeDisplayed() {
        Assert.assertTrue(page.isPoliceStationDisplayed(),
                "Expected 'Police Station' to be shown in nearby facilities");
    }

    @And("nearby fire station information should be displayed")
    public void nearbyFireStationShouldBeDisplayed() {
        Assert.assertTrue(page.isFireStationDisplayed(),
                "Expected 'Fire Station' to be shown in nearby facilities");
    }

    // ── TC28 — Supermarkets and transport ────────────────────────────────────

    @Then("nearby supermarkets should be listed")
    public void nearbySupermarketsShouldBeListed() {
        Assert.assertTrue(page.areNearbySupermarketsListed(),
                "Expected 'More Supermarket' to be listed in nearby facilities");
    }

    @And("nearby metro stations should be listed")
    public void nearbyMetroStationsShouldBeListed() {
        Assert.assertTrue(page.areNearbyMetroStationsListed(),
                "Expected 'Koramangala Metro' to be listed in nearby facilities");
    }

    // ── TC29 — Locality insights ──────────────────────────────────────────────

    @Then("the locality insights section should be displayed")
    public void theLocalityInsightsSectionShouldBeDisplayed() {
        Assert.assertTrue(page.isLocalityInsectionDisplayed(),
                "Expected 'Locality Insights' section heading to be visible");
    }

    @And("locality scores should be visible")
    public void localityScoresShouldBeVisible() {
        Assert.assertTrue(page.areLocalityScoresVisible(),
                "Expected locality score bars (.score-bar) to be rendered");
    }

    // ── TC30 — Safety and security ────────────────────────────────────────────

    @Then("the safety and security section should be displayed")
    public void theSafetyAndSecuritySectionShouldBeDisplayed() {
        Assert.assertTrue(page.isSafetyAndSecurityDisplayed(),
                "Expected 'Safety & Security' section to be visible");
    }

    @And("CCTV and gated community information should be visible")
    public void cctvAndGatedCommunityInfoShouldBeVisible() {
        Assert.assertTrue(page.isCctvAndGatedInfoVisible(),
                "Expected CCTV and Gated Community fields to be visible in safety section");
    }
}
