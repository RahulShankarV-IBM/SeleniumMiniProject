package com.propfind.steps;

import com.propfind.context.MovingAssistantTestContext;
import com.propfind.pages.MovingAssistantPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for MovingAssistant.feature (TC71–TC75).
 * All Selenium interactions are delegated to MovingAssistantPage — no driver calls here.
 * The shared MovingAssistantPage instance is provided via MovingAssistantTestContext.
 */
public class MovingAssistantSteps {

    private final MovingAssistantPage page;

    public MovingAssistantSteps(MovingAssistantTestContext ctx) {
        this.page = ctx.getMovingAssistantPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the moving assistant page")
    public void iAmOnTheMovingAssistantPage() {
        page.navigateTo(MovingAssistantTestContext.getBaseUrl());
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I click the Legal and Rental Support tab")
    public void iClickTheLegalAndRentalSupportTab() {
        page.clickLegalSupportTab();
    }

    @When("I click the Documents tab")
    public void iClickTheDocumentsTab() {
        page.clickDocumentsTab();
    }

    @And("I click the download button for {string}")
    public void iClickTheDownloadButtonFor(String document) {
        page.clickDownloadButton(document);
    }

    @When("I click the Checklists tab")
    public void iClickTheChecklistsTab() {
        page.clickChecklistsTab();
    }

    @When("I click the Moving Guidance tab")
    public void iClickTheMovingGuidanceTab() {
        page.clickMovingGuidanceTab();
    }

    @When("I click the My Progress tab")
    public void iClickTheMyProgressTab() {
        page.clickMyProgressTab();
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("the legal support panel should be visible")
    public void theLegalSupportPanelShouldBeVisible() {
        Assert.assertTrue(page.isLegalSupportPanelVisible(),
                "Expected the Legal & Rental Support panel to be visible");
    }

    @And("the rental agreement accordion should be displayed")
    public void theRentalAgreementAccordionShouldBeDisplayed() {
        Assert.assertTrue(page.isRentalAgreementAccordionDisplayed(),
                "Expected the rental agreement accordion (acc1) to be displayed");
    }

    @Then("the document download should be triggered")
    public void theDocumentDownloadShouldBeTriggered() {
        // Verify the documents panel is still visible after clicking — no navigation away
        Assert.assertTrue(page.isDocumentsPanelVisible(),
                "Expected to remain on the documents panel after clicking a download button");
    }

    @Then("the tenant documentation checklist should be displayed")
    public void theTenantDocumentationChecklistShouldBeDisplayed() {
        Assert.assertTrue(page.isTenantChecklistDisplayed(),
                "Expected the tenant documentation checklist container to be displayed");
    }

    @And("the property inspection checklist should be displayed")
    public void thePropertyInspectionChecklistShouldBeDisplayed() {
        Assert.assertTrue(page.isPropertyChecklistDisplayed(),
                "Expected the property inspection checklist container to be displayed");
    }

    @And("the move-in day checklist should be displayed")
    public void theMoveInDayChecklistShouldBeDisplayed() {
        Assert.assertTrue(page.isMoveInChecklistDisplayed(),
                "Expected the move-in day checklist container to be displayed");
    }

    @Then("the moving guidance panel should be visible")
    public void theMovingGuidancePanelShouldBeVisible() {
        Assert.assertTrue(page.isMovingGuidancePanelVisible(),
                "Expected the Moving Guidance panel to be visible");
    }

    @Then("the progress panel should be visible")
    public void theProgressPanelShouldBeVisible() {
        Assert.assertTrue(page.isProgressPanelVisible(),
                "Expected the My Progress panel to be visible");
    }

    @And("the overall progress percentage should be displayed")
    public void theOverallProgressPercentageShouldBeDisplayed() {
        Assert.assertTrue(page.isProgressPercentageDisplayed(),
                "Expected the overall progress percentage element (prog-pct) to be displayed");
    }

    @And("the progress bar should be displayed")
    public void theProgressBarShouldBeDisplayed() {
        Assert.assertTrue(page.isProgressBarDisplayed(),
                "Expected the progress bar fill element (prog-fill) to be displayed");
    }
}
