package com.propfind.steps;

import com.propfind.context.MapViewTestContext;
import com.propfind.pages.MapViewPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

/**
 * Step definitions for MapView.feature (TC16–TC20).
 * All Selenium interactions are delegated to MapViewPage — no driver calls here.
 * The shared MapViewPage instance is provided via MapViewTestContext.
 */
public class MapViewSteps {

    private final MapViewPage page;

    public MapViewSteps(MapViewTestContext ctx) {
        this.page = ctx.getMapViewPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am on the map view page")
    public void iAmOnTheMapViewPage() {
        page.navigateTo(MapViewTestContext.getBaseUrl());
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I toggle the property markers overlay")
    public void iTogglePropertyMarkersOverlay() {
        page.clickPropsOverlay();
    }

    @When("I click the schools overlay button")
    public void iClickSchoolsOverlayButton() {
        page.clickSchoolsOverlay();
    }

    @When("I click the hospitals overlay button")
    public void iClickHospitalsOverlayButton() {
        page.clickHospitalsOverlay();
    }

    @When("I click the metro overlay button")
    public void iClickMetroOverlayButton() {
        page.clickMetroOverlay();
    }

    @When("I enter {string} in the map search field")
    public void iEnterInMapSearchField(String location) {
        page.enterSearchLocation(location);
    }

    @And("I select {string} from the map purpose filter")
    public void iSelectFromMapPurposeFilter(String purpose) {
        page.selectPurpose(purpose);
    }

    @And("I select {string} from the map BHK filter")
    public void iSelectFromMapBhkFilter(String bhk) {
        page.selectBhk(bhk);
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("the map container should be visible")
    public void theMapContainerShouldBeVisible() {
        Assert.assertTrue(page.isMapVisible(),
                "Expected the Leaflet map container (#map) to be visible");
    }

    @Then("the property list panel should be visible")
    public void thePropertyListPanelShouldBeVisible() {
        Assert.assertTrue(page.isPropListVisible(),
                "Expected the map property list panel (#map-prop-list) to be visible");
    }

    @Then("the map should display at least one property count")
    public void theMapShouldDisplayAtLeastOnePropertyCount() {
        Assert.assertTrue(page.isMapCountVisible(),
                "Expected the map count indicator (#map-count) to be visible");
        String countText = page.getMapCountText();
        Assert.assertFalse(countText.isBlank(),
                "Expected #map-count to contain text, but it was blank");
    }

    @Then("the property list panel should contain at least one listing")
    public void thePropertyListPanelShouldContainAtLeastOneListing() {
        Assert.assertTrue(page.propListHasListings(),
                "Expected #map-prop-list to contain at least one list-{id} item");
    }

    @Then("the map count indicator should be visible")
    public void theMapCountIndicatorShouldBeVisible() {
        Assert.assertTrue(page.isMapCountVisible(),
                "Expected the map count indicator (#map-count) to be visible after applying filters");
    }
}
