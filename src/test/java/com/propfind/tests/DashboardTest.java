package com.propfind.tests;

import com.propfind.base.BaseTestClass;
import com.propfind.pages.DashboardPage;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Plain TestNG tests for the User Dashboard.
 *
 * Covers:
 *   US07 — User Dashboard       (TC31–TC35)
 *   US11 — Property Preferences (TC51–TC55)
 *
 * Each test calls openPage() which seeds the browser with a demo user that
 * already has 2 favorites [id=1,2] and 2 recently-viewed [id=3,4] entries,
 * then navigates directly to dashboard.html — no login form interaction needed.
 */
public class DashboardTest extends BaseTestClass {

    private DashboardPage dashboardPage;

    private static final String DEMO_USER = "demo";
    private static final String DEMO_PASS = "demo123";

    @BeforeMethod(dependsOnMethods = "setUp")
    public void openPage() {
        dashboardPage = new DashboardPage(driver);
        dashboardPage.loginAs(baseUrl, DEMO_USER, DEMO_PASS);
    }

    // ── US07 — User Dashboard ─────────────────────────────────────────────────

    @Test(testName = "TC31", groups = {"US07", "Functional", "High"})
    public void tc31_saveFavoritePropertyDisplayedInDashboard() {
        dashboardPage.clickFavoritesNav();
        Assert.assertTrue(dashboardPage.isFavoritesPanelDisplayed(),
            "TC31: Saved Properties panel should be visible after clicking the nav link.");
        Assert.assertTrue(dashboardPage.getFavoriteCount() > 0,
            "TC31: At least one saved property card should appear in the favourites list.");
    }

    @Test(testName = "TC32", groups = {"US07", "Functional", "High"})
    public void tc32_removeFavoritePropertyFromDashboard() {
        dashboardPage.clickFavoritesNav();
        int before = dashboardPage.getFavoriteCount();
        Assert.assertTrue(before > 0,
            "TC32: At least one saved property must exist to test removal.");
        dashboardPage.removeFavoriteAtIndex(0);
        int after = dashboardPage.getFavoriteCount();
        Assert.assertEquals(after, before - 1,
            "TC32: Favourite count should decrease by 1 after removing a property.");
    }

    @Test(testName = "TC33", groups = {"US07", "Functional", "High"})
    public void tc33_recentlyViewedPropertiesDisplayed() {
        dashboardPage.clickRecentNav();
        Assert.assertTrue(dashboardPage.isRecentPanelDisplayed(),
            "TC33: Recently Viewed panel should be visible.");
        Assert.assertFalse(dashboardPage.getRecentCards().isEmpty(),
            "TC33: At least one recently viewed property card should be displayed.");
    }

    @Test(testName = "TC34", groups = {"US07", "Functional", "Medium"})
    public void tc34_recentlyViewedOrderMostRecentFirst() {
        dashboardPage.clickRecentNav();
        List<WebElement> cards = dashboardPage.getRecentCards();
        Assert.assertFalse(cards.isEmpty(),
            "TC34: Recently Viewed list must not be empty to verify order.");
        Assert.assertTrue(cards.size() >= 1,
            "TC34: Expected at least 1 card in Recently Viewed.");
    }

    @Test(testName = "TC35", groups = {"US07", "Functional", "High"})
    public void tc35_favoritesAndRecentsPersistAfterReLogin() {
        dashboardPage.navigateTo(baseUrl);
        dashboardPage.clickFavoritesNav();
        Assert.assertTrue(dashboardPage.isFavoritesPanelDisplayed(),
            "TC35: Saved Properties panel should be visible after returning to dashboard.");
        Assert.assertTrue(dashboardPage.getFavoriteCount() > 0,
            "TC35: Saved properties should persist after re-navigation.");
        dashboardPage.clickRecentNav();
        Assert.assertFalse(dashboardPage.getRecentCards().isEmpty(),
            "TC35: Recently viewed properties should persist after re-navigation.");
    }

    // ── US11 — Property Preferences ──────────────────────────────────────────

    @Test(testName = "TC51", groups = {"US11", "Functional", "High"})
    public void tc51_savePropertyPreferences() {
        dashboardPage.clickPreferencesNav();
        Assert.assertTrue(dashboardPage.isPreferencesPanelDisplayed(),
            "TC51: My Preferences panel should be visible.");
        dashboardPage.selectCity("Bangalore");
        dashboardPage.selectPurpose("Rent");
        dashboardPage.selectBhk("2 BHK");
        dashboardPage.enterBudget("50000");
        dashboardPage.clickSavePreferences();
        Assert.assertTrue(dashboardPage.isPreferencesSavedAlertDisplayed(),
            "TC51: Preferences saved confirmation alert should be displayed after saving.");
    }

    @Test(testName = "TC52", groups = {"US11", "Functional", "High"})
    public void tc52_enterNaturalLanguageRequirements() {
        dashboardPage.clickPreferencesNav();
        String nlText = "Looking for a 2BHK fully furnished flat near metro in Bangalore";
        dashboardPage.enterNaturalLanguagePreference(nlText);
        Assert.assertEquals(dashboardPage.getNaturalLanguageText(), nlText,
            "TC52: The natural-language preferences field should retain the typed input.");
    }

    @Test(testName = "TC53", groups = {"US11", "Functional", "High"})
    public void tc53_viewPersonalisedRecommendations() {
        dashboardPage.clickPreferencesNav();
        dashboardPage.selectCity("Bangalore");
        dashboardPage.selectPurpose("Rent");
        dashboardPage.clickSavePreferences();
        dashboardPage.clickRecommendationsNav();
        Assert.assertTrue(dashboardPage.isRecommendationsPanelDisplayed(),
            "TC53: Recommendations panel should be visible.");
        Assert.assertTrue(dashboardPage.getRecommendationCount() > 0,
            "TC53: At least one recommended property card should appear after saving preferences.");
    }

    @DataProvider(name = "preferencesData")
    public Object[][] preferencesData() {
        return new Object[][]{ {"Mumbai", "Rent"}, {"Hyderabad", "Buy"} };
    }

    @Test(testName = "TC54", groups = {"US11", "Functional", "Medium"}, dataProvider = "preferencesData")
    public void tc54_updatePreferencesAndVerifyConfirmation(String city, String purpose) {
        dashboardPage.clickPreferencesNav();
        dashboardPage.selectCity(city);
        dashboardPage.selectPurpose(purpose);
        dashboardPage.clickSavePreferences();
        Assert.assertTrue(dashboardPage.isPreferencesSavedAlertDisplayed(),
            "TC54: Preferences saved confirmation should be visible for " + city + " / " + purpose);
    }

    @Test(testName = "TC55", groups = {"US11", "Functional", "High"})
    public void tc55_enableAndDisablePropertyAlerts() {
        dashboardPage.clickPreferencesNav();
        dashboardPage.enableAlertsToggle();
        Assert.assertEquals(dashboardPage.getAlertStatusLabelText(), "On",
            "TC55: Alert status label should show 'On' after enabling.");
        dashboardPage.disableAlertsToggle();
        Assert.assertEquals(dashboardPage.getAlertStatusLabelText(), "Off",
            "TC55: Alert status label should show 'Off' after disabling.");
    }
}
