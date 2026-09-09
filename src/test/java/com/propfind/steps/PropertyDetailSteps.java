package com.propfind.steps;

import com.propfind.context.PropertyDetailTestContext;
import com.propfind.pages.PropertyDetailPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for PropertyDetail.feature (TC21–TC30, TC41–TC50, TC66–TC70).
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

    @Given("I am on the property detail page")
    public void iAmOnThePropertyDetailPage() {
        page.navigateTo(PropertyDetailTestContext.getBaseUrl());
    }

    @Given("I am on the property detail page as a logged-in user")
    public void iAmOnThePropertyDetailPageAsLoggedInUser() {
        page.loginAs(PropertyDetailTestContext.getBaseUrl(), "testuser", "Test@1234");
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

    // ── Schedule Visit actions ─────────────────────────────────────────────────

    @When("I click the Schedule Visit button")
    public void iClickTheScheduleVisitButton() {
        page.clickScheduleVisit();
    }

    @And("I select the visit date {string}")
    public void iSelectTheVisitDate(String date) {
        page.selectVisitDate(date);
    }

    @And("I select the visit time slot {string}")
    public void iSelectTheVisitTimeSlot(String timeSlot) {
        page.selectVisitTimeSlot(timeSlot);
    }

    @And("I enter visitor name {string}")
    public void iEnterVisitorName(String name) {
        page.enterVisitorName(name);
    }

    @And("I enter visitor phone {string}")
    public void iEnterVisitorPhone(String phone) {
        page.enterVisitorPhone(phone);
    }

    @And("I confirm the booking")
    public void iConfirmTheBooking() {
        page.confirmBooking();
    }

    @And("I confirm the booking without filling any fields")
    public void iConfirmBookingWithoutFillingFields() {
        page.confirmBooking();
    }

    @When("I close the schedule visit modal")
    public void iCloseTheScheduleVisitModal() {
        page.closeScheduleModal();
    }

    // ── Contact Owner actions ──────────────────────────────────────────────────

    @When("I click the Contact Owner button")
    public void iClickTheContactOwnerButton() {
        page.clickContactOwner();
    }

    @And("I enter inquiry message {string}")
    public void iEnterInquiryMessage(String message) {
        page.enterInquiryMessage(message);
    }

    @And("I enter an inquiry message exceeding 500 characters")
    public void iEnterInquiryMessageExceeding500Characters() {
        page.enterInquiryMessageOverLimit("A".repeat(501));
    }

    @And("I enter contact name {string}")
    public void iEnterContactName(String name) {
        page.enterContactName(name);
    }

    @And("I enter contact phone {string}")
    public void iEnterContactPhone(String phone) {
        page.enterContactPhone(phone);
    }

    @And("I submit the inquiry")
    public void iSubmitTheInquiry() {
        page.submitInquiry();
    }

    // ── Report Listing actions ─────────────────────────────────────────────────

    @When("I open the report listing form")
    public void iOpenTheReportListingForm() {
        page.openReportForm();
    }

    @And("I select report reason {string}")
    public void iSelectReportReason(String reason) {
        page.selectReportReason(reason);
    }

    @And("I submit the report")
    public void iSubmitTheReport() {
        page.submitReport();
    }

    @And("I submit the report without selecting a reason")
    public void iSubmitReportWithoutReason() {
        page.submitReportWithoutReason();
    }

    // ── Assertions ─────────────────────────────────────────────────────────────

    @Then("the schedule visit modal should be displayed")
    public void theScheduleVisitModalShouldBeDisplayed() {
        Assert.assertTrue(page.isScheduleModalVisible(),
                "Expected the schedule visit modal to be visible");
    }

    @Then("the visit date and time slot fields should be visible")
    public void theVisitDateAndTimeSlotFieldsShouldBeVisible() {
        Assert.assertTrue(page.isVisitDateVisible(),
                "Expected the visit date field to be visible");
        Assert.assertTrue(page.isVisitTimeVisible(),
                "Expected the visit time slot field to be visible");
    }

    @Then("the visit booking should be confirmed successfully")
    public void theVisitBookingShouldBeConfirmedSuccessfully() {
        Assert.assertTrue(page.isBookingConfirmed(),
                "Expected the booking to succeed and the schedule modal to close");
    }

    @Then("a booking validation error should be displayed")
    public void aBookingValidationErrorShouldBeDisplayed() {
        Assert.assertTrue(page.isModalAlertVisible(),
                "Expected a validation error alert inside the schedule visit modal");
    }

    @Then("the schedule visit modal should be closed")
    public void theScheduleVisitModalShouldBeClosed() {
        Assert.assertTrue(page.isScheduleModalClosed(),
                "Expected the schedule visit modal to be closed");
    }

    @Then("the Contact Owner button should be visible")
    public void theContactOwnerButtonShouldBeVisibleInSidebar() {
        Assert.assertTrue(page.isContactOwnerButtonVisible(),
                "Expected the Contact Owner button to be visible on the property detail page");
    }

    @Then("the inquiry should be sent successfully")
    public void theInquiryShouldBeSentSuccessfully() {
        Assert.assertTrue(page.isInquirySent(),
                "Expected the inquiry to be sent and the contact modal to close");
    }

    @Then("a contact validation error should be displayed")
    public void aContactValidationErrorShouldBeDisplayed() {
        Assert.assertTrue(page.isContactAlertVisible(),
                "Expected a validation error alert inside the contact owner modal");
    }

    @Then("the verified property badge should be displayed")
    public void theVerifiedPropertyBadgeShouldBeDisplayed() {
        Assert.assertTrue(page.isVerifiedBadgeDisplayed(),
                "Expected the verified badge to be visible on a verified property listing");
    }

    @Then("the report should be submitted successfully")
    public void theReportShouldBeSubmittedSuccessfully() {
        Assert.assertTrue(page.isReportSubmitted(),
                "Expected the report to be submitted and the report modal to close");
    }

    @Then("a report validation error should be displayed")
    public void aReportValidationErrorShouldBeDisplayed() {
        Assert.assertTrue(page.isReportAlertVisible(),
                "Expected a validation error alert inside the report modal");
    }

    @Then("the property safety information section should be displayed")
    public void thePropertySafetyInformationSectionShouldBeDisplayed() {
        Assert.assertTrue(page.isSafetySectionDisplayed(),
                "Expected the Safety & Security section to be visible on the property detail page");
    }

    @Then("the property amenities section should be displayed")
    public void thePropertyAmenitiesSectionShouldBeDisplayed() {
        Assert.assertTrue(page.isAmenitiesSectionDisplayed(),
                "Expected at least one amenity tag to be visible on the property detail page");
    }
}
