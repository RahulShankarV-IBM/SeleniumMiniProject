import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for Compare.feature (TC36–TC40, US08).
 * All Selenium interactions are delegated to ComparePage — no driver calls here.
 */
public class CompareSteps {

    private final ComparePage comparePage;

    public CompareSteps(CompareTestContext ctx) {
        this.comparePage = ctx.getComparePage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the compare page")
    public void iAmOnTheComparePage() {
        comparePage.navigateTo(CompareTestContext.getBaseUrl());
    }

    // ── Session seeding ───────────────────────────────────────────────────────

    @Given("the session contains property IDs 1, 2, and 3")
    public void theSessionContainsThreePropertyIds() {
        comparePage.seedCompareList(1, 2, 3);
    }

    @Given("the session contains property IDs 1 and 2")
    public void theSessionContainsTwoPropertyIds() {
        comparePage.seedCompareList(1, 2);
    }

    @Given("the session has no properties selected for comparison")
    public void theSessionHasNoPropertiesSelected() {
        comparePage.clearCompareList();
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I load the compare page")
    public void iLoadTheComparePage() {
        // Session was seeded before navigation; page already rendered — no extra action needed.
    }

    @And("I remove property with ID 1 from the comparison")
    public void iRemovePropertyWithId1() {
        comparePage.removeProperty(1);
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("three property columns should be displayed in the comparison table")
    public void threePropertyColumnsShouldBeDisplayed() {
        Assert.assertEquals(comparePage.getPropertyColumnCount(), 3,
                "Expected 3 property columns in the comparison table but found: "
                        + comparePage.getPropertyColumnCount());
    }

    @Then("the comparison table should display price, BHK, location and amenities for each property")
    public void comparisonTableShouldDisplayKeyDetails() {
        Assert.assertTrue(comparePage.isRowPresent("Price"),
                "Expected a 'Price' row in the comparison table");
        Assert.assertTrue(comparePage.isRowPresent("BHK"),
                "Expected a 'BHK' row in the comparison table");
        Assert.assertTrue(comparePage.isRowPresent("Location"),
                "Expected a 'Location' row in the comparison table");
        Assert.assertTrue(comparePage.isRowPresent("Amenities"),
                "Expected an 'Amenities' row in the comparison table");
    }

    @Then("the comparison table should display nearby school, hospital and metro information")
    public void comparisonTableShouldDisplayNearbyInfo() {
        Assert.assertTrue(comparePage.isRowPresent("Nearby School"),
                "Expected a 'Nearby School' row in the comparison table");
        Assert.assertTrue(comparePage.isRowPresent("Nearby Hospital"),
                "Expected a 'Nearby Hospital' row in the comparison table");
        Assert.assertTrue(comparePage.isRowPresent("Metro Station"),
                "Expected a 'Metro Station' row in the comparison table");
    }

    @Then("the empty comparison message should be displayed")
    public void emptyComparisonMessageShouldBeDisplayed() {
        Assert.assertTrue(comparePage.isEmptyStateVisible(),
                "Expected the empty-state message to be visible when fewer than 2 properties are selected");
    }

    @Then("the comparison table should show only one property remaining")
    public void comparisonTableShouldShowOnePropertyRemaining() {
        Assert.assertEquals(comparePage.getPropertyColumnCount(), 1,
                "Expected only 1 property column after removing one but found: "
                        + comparePage.getPropertyColumnCount());
    }
    @Then("two property columns should be displayed in the comparison table")
public void twoPropertyColumnsShouldBeDisplayed() {
    Assert.assertEquals(
            comparePage.getPropertyColumnCount(),
            2,
            "Expected 2 property columns after removing one but found: "
                    + comparePage.getPropertyColumnCount()
    );
}
}
