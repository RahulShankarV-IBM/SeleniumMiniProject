package com.propfind.steps;

import com.propfind.context.CompareTestContext;
import com.propfind.pages.ComparePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for Compare.feature (TC36–TC40).
 * All Selenium interactions are delegated to ComparePage — no driver calls here.
 * The shared ComparePage instance is provided via CompareTestContext.
 */
public class CompareSteps {

    private final ComparePage comparePage;
    // Track the first property ID seeded so TC40 (remove) can reference it
    private int firstPropertyId;

    public CompareSteps(CompareTestContext ctx) {
        this.comparePage = ctx.getComparePage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the compare page with properties in the compare list")
    public void iAmOnTheComparePageWithPropertiesInTheCompareList() {
        comparePage.navigateTo(CompareTestContext.getBaseUrl());
    }

    // ── Given: pre-conditions ─────────────────────────────────────────────────

    @Given("the compare list contains three properties")
    public void theCompareListContainsThreeProperties() {
        comparePage.seedCompareList(1, 2, 3);
        firstPropertyId = 1;
    }

    @Given("the compare list contains two properties")
    public void theCompareListContainsTwoProperties() {
        comparePage.seedCompareList(1, 2);
        firstPropertyId = 1;
    }

    @Given("the compare list contains one property with id {int}")
    public void theCompareListContainsOnePropertyWithId(int id) {
        comparePage.seedCompareList(id);
        firstPropertyId = id;
    }

    // ── When: actions ─────────────────────────────────────────────────────────

    @When("I attempt to add the same property with id {int} to the compare list again")
public void iAttemptToAddTheSamePropertyWithIdToTheCompareListAgain(int id) {
    comparePage.attemptToAddProperty(id);
}

    @When("I remove the first property from the comparison")
    public void iRemoveTheFirstPropertyFromTheComparison() {
        comparePage.removeProperty(firstPropertyId);
    }

    // ── Then: assertions ──────────────────────────────────────────────────────

    @Then("three property columns should be displayed side by side")
    public void threePropertyColumnsShouldBeDisplayedSideBySide() {
        Assert.assertEquals(comparePage.getVisiblePropertyColumnCount(), 3,
                "Expected 3 property columns in the comparison table but found a different count");
    }

    @Then("the comparison table should display price, BHK, location and amenities for each property")
    public void theComparisonTableShouldDisplayPriceBhkLocationAndAmenities() {
        Assert.assertTrue(comparePage.isComparisonRowPresent("Price"),
                "Expected a 'Price' row in the comparison table");
        Assert.assertTrue(comparePage.isComparisonRowPresent("BHK"),
                "Expected a 'BHK' row in the comparison table");
        Assert.assertTrue(comparePage.isComparisonRowPresent("Location"),
                "Expected a 'Location' row in the comparison table");
        Assert.assertTrue(comparePage.isComparisonRowPresent("Amenities"),
                "Expected an 'Amenities' row in the comparison table");
    }

    @Then("the comparison table should display nearby school, hospital and metro station information")
    public void theComparisonTableShouldDisplayNearbyLocalityInformation() {
        Assert.assertTrue(comparePage.isComparisonRowPresent("Nearby School"),
                "Expected a 'Nearby School' row in the comparison table");
        Assert.assertTrue(comparePage.isComparisonRowPresent("Nearby Hospital"),
                "Expected a 'Nearby Hospital' row in the comparison table");
        Assert.assertTrue(comparePage.isComparisonRowPresent("Metro Station"),
                "Expected a 'Metro Station' row in the comparison table");
    }

    @Then("the compare list should still contain only one entry for that property")
    public void theCompareListShouldStillContainOnlyOneEntryForThatProperty() {
        String session = comparePage.getCompareListFromSession();
        int count = 0;
        int idx = 0;
        while ((idx = session.indexOf(String.valueOf(firstPropertyId), idx)) != -1) {
            count++;
            idx++;
        }
        Assert.assertEquals(count, 1,
                "Expected the compare list to contain the property id exactly once but found: " + count
                + " occurrences in: " + session);
    }

    @Then("the removed property column should no longer be displayed")
    public void theRemovedPropertyColumnShouldNoLongerBeDisplayed() {
        Assert.assertTrue(comparePage.isPropertyRemoved(firstPropertyId),
                "Expected remove button for property id " + firstPropertyId
                + " to be absent after removal, but it is still present");
    }
}
