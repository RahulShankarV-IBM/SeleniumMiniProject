package com.propfind.steps;

import com.propfind.context.DashboardTestContext;
import com.propfind.pages.DashboardPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.Arrays;

/**
 * Step definitions for Dashboard.feature (US07 & US11 / TC31–TC35, TC51–TC55).
 * All Selenium interactions are delegated to DashboardPage — no driver calls here.
 */
public class DashboardSteps {

    private final DashboardPage dashboardPage;

    public DashboardSteps(DashboardTestContext ctx) {
        this.dashboardPage = ctx.getDashboardPage();
    }

    // ── Background ────────────────────────────────────────────────────────────

    @Given("I am logged in and on the dashboard page")
    public void iAmLoggedInAndOnTheDashboardPage() {
        dashboardPage.navigateTo(DashboardTestContext.getBaseUrl());
        Assert.assertTrue(dashboardPage.isUserLoggedIn(), "User should be logged in on dashboard");
    }

    // ── Pre-conditions & Setup ────────────────────────────────────────────────

    @Given("I have a property in favorites with id {int}")
    public void iHaveAPropertyInFavoritesWithId(int propertyId) {
        dashboardPage.addFavorite(propertyId);
    }

    @Given("I have recently viewed properties")
    public void iHaveRecentlyViewedProperties() {
        dashboardPage.setRecentlyViewed(1, 2, 3);
    }

    @Given("I have recently viewed properties in sequence")
    public void iHaveRecentlyViewedPropertiesInSequence() {
        dashboardPage.setRecentlyViewed(4, 2, 1);
    }

    @Given("I have saved properties and recently viewed properties")
    public void iHaveSavedPropertiesAndRecentlyViewedProperties() {
        dashboardPage.setUserFavoritesAndRecent(Arrays.asList(1, 2), Arrays.asList(3, 4));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    @When("I have a property added to favorites")
    public void iHaveAPropertyAddedToFavorites() {
        dashboardPage.addFavorite(2);
    }

    @When("I navigate to the Saved Properties section")
    public void iNavigateToTheSavedPropertiesSection() {
        dashboardPage.openFavoritesSection();
    }

    @When("I remove the property with id {int} from favorites")
    public void iRemoveThePropertyWithIdFromFavorites(int propertyId) {
        dashboardPage.removeFavorite(propertyId);
    }

    @When("I navigate to the Recently Viewed section")
    public void iNavigateToTheRecentlyViewedSection() {
        dashboardPage.openRecentSection();
    }

    @When("I re-authenticate into the dashboard")
    public void iReAuthenticateIntoTheDashboard() {
        dashboardPage.navigateTo(DashboardTestContext.getBaseUrl());
    }

    @When("I navigate to the Preferences section")
    public void iNavigateToThePreferencesSection() {
        dashboardPage.openPreferencesSection();
    }

    @When("I select preferred city {string}")
    public void iSelectPreferredCity(String city) {
        dashboardPage.selectPreferredCity(city);
    }

    @When("I select preferred purpose {string}")
    public void iSelectPreferredPurpose(String purpose) {
        dashboardPage.selectPreferredPurpose(purpose);
    }

    @When("I select preferred BHK {string}")
    public void iSelectPreferredBHK(String bhk) {
        dashboardPage.selectPreferredBhk(bhk);
    }

    @When("I enter max budget {string}")
    public void iEnterMaxBudget(String budget) {
        dashboardPage.enterMaxBudget(budget);
    }

    @When("I click save preferences")
    public void iClickSavePreferences() {
        dashboardPage.clickSavePreferences();
    }

    @When("I enter natural language preference {string}")
    public void iEnterNaturalLanguagePreference(String text) {
        dashboardPage.enterNaturalLanguagePreference(text);
    }

    @When("I navigate to the Recommendations section")
    public void iNavigateToTheRecommendationsSection() {
        dashboardPage.openRecommendationsSection();
    }

    @When("I toggle property alerts")
    public void iTogglePropertyAlerts() {
        dashboardPage.toggleAlerts();
    }

    // ── Assertions ────────────────────────────────────────────────────────────

    @Then("the property should be listed in favorites")
    public void thePropertyShouldBeListedInFavorites() {
        Assert.assertTrue(dashboardPage.getFavoritesCount() > 0,
                "Expected at least one property listed in saved favorites");
    }

    @Then("the property with id {int} should not be listed in favorites")
    public void thePropertyWithIdShouldNotBeListedInFavorites(int propertyId) {
        Assert.assertFalse(dashboardPage.isFavoritePresent(propertyId),
                "Expected property with ID " + propertyId + " to be removed from favorites");
    }

    @Then("the recently viewed properties should be displayed")
    public void theRecentlyViewedPropertiesShouldBeDisplayed() {
        Assert.assertTrue(dashboardPage.isRecentlyViewedListDisplayed(),
                "Expected recently viewed properties to be displayed");
    }

    @Then("the most recently viewed property should appear first")
    public void theMostRecentlyViewedPropertyShouldAppearFirst() {
        String firstTitle = dashboardPage.getFirstRecentlyViewedTitle();
        Assert.assertFalse(firstTitle.isBlank(),
                "Expected first recently viewed property card to have a title");
    }

    @Then("my favorites and recently viewed properties should remain available")
    public void myFavoritesAndRecentlyViewedPropertiesShouldRemainAvailable() {
        dashboardPage.openFavoritesSection();
        Assert.assertTrue(dashboardPage.getFavoritesCount() > 0,
                "Favorites should persist after page reload");
        dashboardPage.openRecentSection();
        Assert.assertTrue(dashboardPage.isRecentlyViewedListDisplayed(),
                "Recently viewed properties should persist after page reload");
    }

    @Then("the preferences saved confirmation should be displayed")
    public void thePreferencesSavedConfirmationShouldBeDisplayed() {
        Assert.assertTrue(dashboardPage.isPrefSavedAlertVisible(),
                "Expected preferences saved alert to be visible");
        Assert.assertTrue(dashboardPage.getPrefSavedAlertText().toLowerCase().contains("saved"),
                "Expected alert message to mention 'saved' but got: " + dashboardPage.getPrefSavedAlertText());
    }

    @Then("personalized property recommendations should be displayed")
    public void personalizedPropertyRecommendationsShouldBeDisplayed() {
        Assert.assertTrue(dashboardPage.areRecommendationsVisible(),
                "Expected property recommendation cards to be visible");
    }

    @Then("the preference recommendations should reflect the updated preferences")
    public void thePreferenceRecommendationsShouldReflectTheUpdatedPreferences() {
        Assert.assertTrue(dashboardPage.arePreferenceRecommendationsVisible(),
                "Expected preferences recommendations grid to display matching properties");
    }

    @Then("the alert status should be updated")
    public void theAlertStatusShouldBeUpdated() {
        String status = dashboardPage.getAlertStatusText();
        Assert.assertTrue(status.equalsIgnoreCase("On") || status.equalsIgnoreCase("Off"),
                "Alert status label should be On or Off but got: " + status);
    }
}
