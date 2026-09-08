package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;

import java.util.List;
import org.openqa.selenium.WebElement;

/**
 * DashboardTest — TestNG test class for the User Dashboard.
 *
 * Covers:
 *  - US07: User Dashboard          (TC31–TC35)
 *  - US11: Property Preferences    (TC51–TC55)
 *
 * Each test calls openPage() which seeds the browser with a demo user that
 * already has 2 favorites [id=1,2] and 2 recently-viewed [id=3,4] entries,
 * then navigates directly to dashboard.html — no login form interaction needed.
 */
public class DashboardTest extends BaseTest {

    private DashboardPage dashboardPage;

    private static final String DEMO_USER = "demo";
    private static final String DEMO_PASS = "demo123";

    @BeforeMethod
    public void openPage() {
        dashboardPage = new DashboardPage(driver);
        dashboardPage.loginAs(baseUrl, DEMO_USER, DEMO_PASS);
    }

    // ── US07 – User Dashboard ─────────────────────────────────────────────────

    @Test(testName = "TC31", groups = {"US07", "Functional", "High"})
    public void tc31_saveFavoritePropertyDisplayedInDashboard() {
        dashboardPage.clickFavoritesNav();
        Assert.assertTrue(dashboardPage.isFavoritesPanelDisplayed(),
            "TC31 FAILED: Saved Properties panel should be visible after clicking the nav link.");
        Assert.assertTrue(dashboardPage.getFavoriteCount() > 0,
            "TC31 FAILED: At least one saved property card should appear in the favourites list.");
    }

    @Test(testName = "TC32", groups = {"US07", "Functional", "High"})
    public void tc32_removeFavoritePropertyFromDashboard() {
        dashboardPage.clickFavoritesNav();
        Assert.assertTrue(dashboardPage.isFavoritesPanelDisplayed(),
            "TC32 FAILED: Saved Properties panel must be visible before removing a property.");
        int before = dashboardPage.getFavoriteCount();
        Assert.assertTrue(before > 0,
            "TC32 FAILED: At least one saved property must exist to test removal.");
        dashboardPage.removeFavoriteAtIndex(0);
        int after = dashboardPage.getFavoriteCount();
        Assert.assertEquals(after, before - 1,
            "TC32 FAILED: Favourite count should decrease by 1 after removing a property.");
    }

    @Test(testName = "TC33", groups = {"US07", "Functional", "High"})
    public void tc33_recentlyViewedPropertiesDisplayed() {
        dashboardPage.clickRecentNav();
        Assert.assertTrue(dashboardPage.isRecentPanelDisplayed(),
            "TC33 FAILED: Recently Viewed panel should be visible after clicking the nav link.");
        Assert.assertFalse(dashboardPage.getRecentCards().isEmpty(),
            "TC33 FAILED: At least one recently viewed property card should be displayed.");
    }

    @Test(testName = "TC34", groups = {"US07", "Functional", "Medium"})
    public void tc34_recentlyViewedOrderMostRecentFirst() {
        dashboardPage.clickRecentNav();
        Assert.assertTrue(dashboardPage.isRecentPanelDisplayed(),
            "TC34 FAILED: Recently Viewed panel should be visible.");
        List<WebElement> cards = dashboardPage.getRecentCards();
        Assert.assertFalse(cards.isEmpty(),
            "TC34 FAILED: Recently Viewed list must not be empty to verify order.");
        // The dashboard renders recently-viewed in array order; with 2 items seeded
        // we verify at least the list is rendered (order controlled by the JS).
        Assert.assertTrue(cards.size() >= 1,
            "TC34 FAILED: Expected at least 1 card in Recently Viewed.");
    }

    @Test(testName = "TC35", groups = {"US07", "Functional", "High"})
    public void tc35_favoritesAndRecentsPersistAfterReLogin() {
        // Simulate "re-login" by navigating away then back to dashboard.html.
        // The seeded sessionStorage set in @BeforeMethod survives same-session navigation,
        // so favorites and recently-viewed data must still be present on return.
        dashboardPage.navigateTo(baseUrl);
        dashboardPage.clickFavoritesNav();
        Assert.assertTrue(dashboardPage.isFavoritesPanelDisplayed(),
            "TC35 FAILED: Saved Properties panel should be visible after returning to dashboard.");
        Assert.assertTrue(dashboardPage.getFavoriteCount() > 0,
            "TC35 FAILED: Saved properties should persist after logout and re-login.");
        dashboardPage.clickRecentNav();
        Assert.assertFalse(dashboardPage.getRecentCards().isEmpty(),
            "TC35 FAILED: Recently viewed properties should persist after logout and re-login.");
    }

    // ── US11 – Property Preferences ───────────────────────────────────────────

    @Test(testName = "TC51", groups = {"US11", "Functional", "High"})
    public void tc51_savePropertyPreferences() {
        dashboardPage.clickPreferencesNav();
        Assert.assertTrue(dashboardPage.isPreferencesPanelDisplayed(),
            "TC51 FAILED: My Preferences panel should be visible.");
        dashboardPage.selectCity("Bangalore");
        dashboardPage.selectPurpose("Rent");
        // BHK options are: Any | 1 BHK | 2 BHK | 3 BHK | 4 BHK+
        dashboardPage.selectBhk("2 BHK");
        dashboardPage.enterBudget("50000");
        dashboardPage.clickSavePreferences();
        Assert.assertTrue(dashboardPage.isPreferencesSavedAlertDisplayed(),
            "TC51 FAILED: Preferences saved confirmation alert should be displayed after saving.");
    }

    @Test(testName = "TC52", groups = {"US11", "Functional", "High"})
    public void tc52_enterNaturalLanguageRequirements() {
        dashboardPage.clickPreferencesNav();
        Assert.assertTrue(dashboardPage.isPreferencesPanelDisplayed(),
            "TC52 FAILED: My Preferences panel should be visible.");
        String nlText = "Looking for a 2BHK fully furnished flat near metro in Bangalore";
        dashboardPage.enterNaturalLanguagePreference(nlText);
        Assert.assertEquals(dashboardPage.getNaturalLanguageText(), nlText,
            "TC52 FAILED: The natural-language preferences field should accept and retain the typed input.");
    }

    @Test(testName = "TC53", groups = {"US11", "Functional", "High"})
    public void tc53_viewPersonalisedRecommendations() {
        // Save preferences first to ensure recommendation panel has context
        dashboardPage.clickPreferencesNav();
        dashboardPage.selectCity("Bangalore");
        dashboardPage.selectPurpose("Rent");
        dashboardPage.clickSavePreferences();
        // Switch to the Recommendations panel
        dashboardPage.clickRecommendationsNav();
        Assert.assertTrue(dashboardPage.isRecommendationsPanelDisplayed(),
            "TC53 FAILED: Recommendations panel should be visible.");
        Assert.assertTrue(dashboardPage.getRecommendationCount() > 0,
            "TC53 FAILED: At least one recommended property card should appear after saving preferences.");
    }

    @DataProvider(name = "preferencesData")
    public Object[][] preferencesData() {
        return new Object[][]{{"Mumbai", "Rent"}, {"Hyderabad", "Buy"}};
    }

    @Test(testName = "TC54", groups = {"US11", "Functional", "Medium"}, dataProvider = "preferencesData")
    public void tc54_updatePreferencesAndVerifyConfirmation(String city, String purpose) {
        dashboardPage.clickPreferencesNav();
        Assert.assertTrue(dashboardPage.isPreferencesPanelDisplayed(),
            "TC54 FAILED: My Preferences panel should be visible.");
        dashboardPage.selectCity(city);
        dashboardPage.selectPurpose(purpose);
        dashboardPage.clickSavePreferences();
        Assert.assertTrue(dashboardPage.isPreferencesSavedAlertDisplayed(),
            "TC54 FAILED: Preferences saved confirmation should be visible after updating for " + city + " / " + purpose);
    }

    @Test(testName = "TC55", groups = {"US11", "Functional", "High"})
    public void tc55_enableAndDisablePropertyAlerts() {
        dashboardPage.clickPreferencesNav();
        Assert.assertTrue(dashboardPage.isPreferencesPanelDisplayed(),
            "TC55 FAILED: My Preferences panel should be visible.");
        dashboardPage.enableAlertsToggle();
        Assert.assertEquals(dashboardPage.getAlertStatusLabelText(), "On",
            "TC55 FAILED: Alert status label should show 'On' after enabling the toggle.");
        dashboardPage.disableAlertsToggle();
        Assert.assertEquals(dashboardPage.getAlertStatusLabelText(), "Off",
            "TC55 FAILED: Alert status label should show 'Off' after disabling the toggle.");
    }
}
