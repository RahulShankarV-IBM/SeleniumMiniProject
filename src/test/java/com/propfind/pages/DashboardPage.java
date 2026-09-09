package com.propfind.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for dashboard.html
 * Handles user dashboard interactions for US07 & US11 (TC31–TC35, TC51–TC55).
 * No assertions live here — all assertions belong in step definitions.
 */
public class DashboardPage {

    // ── Sidebar Navigation Locators ──────────────────────────────────────────
    private static final By USER_AVATAR          = By.id("user-avatar");
    private static final By USER_NAME            = By.id("user-name");
    private static final By USER_EMAIL           = By.id("user-email");
    private static final By NAV_OVERVIEW         = By.id("nav-overview");
    private static final By NAV_FAVORITES        = By.id("nav-favorites");
    private static final By NAV_RECENT           = By.id("nav-recent");
    private static final By NAV_APPOINTMENTS     = By.id("nav-appointments");
    private static final By NAV_RECOMMENDATIONS  = By.id("nav-recommendations");
    private static final By NAV_PREFERENCES      = By.id("nav-preferences");

    // ── Panels Locators ──────────────────────────────────────────────────────
    private static final By PANEL_OVERVIEW       = By.id("panel-overview");
    private static final By GREET_NAME           = By.id("greet-name");
    private static final By STAT_FAVS            = By.id("stat-favs");
    private static final By STAT_APPTS           = By.id("stat-appts");
    private static final By STAT_RECENT          = By.id("stat-recent");
    private static final By OVERVIEW_APPTS       = By.id("overview-appts");

    private static final By PANEL_FAVORITES      = By.id("panel-favorites");
    private static final By FAVS_LIST            = By.id("favs-list");

    private static final By PANEL_RECENT         = By.id("panel-recent");
    private static final By RECENT_LIST          = By.id("recent-list");

    private static final By PANEL_RECOMMENDATIONS = By.id("panel-recommendations");
    private static final By RECO_GRID            = By.id("reco-grid");

    private static final By PANEL_PREFERENCES    = By.id("panel-preferences");

    // ── Preferences Locators ─────────────────────────────────────────────────
    private static final By ALERT_STATUS_LBL     = By.id("alert-status-lbl");
    private static final By ALERTS_TOGGLE        = By.id("alerts-toggle");
    private static final By TOGGLE_TRACK         = By.id("toggle-track");
    private static final By TOGGLE_THUMB         = By.id("toggle-thumb");
    private static final By PREF_SAVED_ALERT     = By.id("pref-saved-alert");
    private static final By PREF_NL              = By.id("pref-nl");
    private static final By PREF_CITY            = By.id("pref-city");
    private static final By PREF_PURPOSE         = By.id("pref-purpose");
    private static final By PREF_BHK             = By.id("pref-bhk");
    private static final By PREF_FURNISH         = By.id("pref-furnish");
    private static final By PREF_BUDGET          = By.id("pref-budget");
    private static final By PREF_TYPE            = By.id("pref-type");
    private static final By PREF_AMENITY_ROW     = By.id("pref-amenity-row");
    private static final By BTN_SAVE_PREFERENCES = By.id("btn-save-preferences");
    private static final By PREF_RECO_GRID       = By.id("pref-reco-grid");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation & Auth ────────────────────────────────────────────────────
    public void loginAs(String baseUrl, String username, String password) {
        driver.get(baseUrl + "login.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userJson = String.format(
            "{\"id\":1001,\"name\":\"Demo User\",\"email\":\"demo@propfind.com\",\"username\":\"%s\"," +
            "\"password\":\"%s\",\"favorites\":[1,3],\"recentlyViewed\":[1,2,3],\"appointments\":[]}",
            username, password);
        String usersJson = String.format("[%s]", userJson);
        js.executeScript("localStorage.setItem('prop_users', arguments[0]);", usersJson);
        js.executeScript("sessionStorage.setItem('prop_current_user', arguments[0]);", userJson);
        driver.get(baseUrl + "dashboard.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(USER_AVATAR));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "dashboard.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(USER_AVATAR));
    }

    // ── Seed User State via JS ───────────────────────────────────────────────
    public void setUserFavoritesAndRecent(List<Integer> favIds, List<Integer> recentIds) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String favsStr = favIds.toString();
        String recentStr = recentIds.toString();
        js.executeScript(
            "var u = JSON.parse(sessionStorage.getItem('prop_current_user') || '{}');" +
            "u.favorites = " + favsStr + ";" +
            "u.recentlyViewed = " + recentStr + ";" +
            "sessionStorage.setItem('prop_current_user', JSON.stringify(u));" +
            "Auth.updateUser(u);" +
            "if (typeof initDashboard === 'function') initDashboard();"
        );
    }

    public void addFavorite(int propertyId) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "Auth.toggleFavorite(" + propertyId + ");" +
            "var u = Auth.getCurrentUser();" +
            "if (!u.favorites.includes(" + propertyId + ")) { Auth.toggleFavorite(" + propertyId + "); }" +
            "if (typeof initDashboard === 'function') initDashboard();"
        );
    }

    public void setRecentlyViewed(int... propertyIds) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < propertyIds.length; i++) {
            sb.append(propertyIds[i]);
            if (i < propertyIds.length - 1) sb.append(",");
        }
        sb.append("]");
        js.executeScript(
            "var u = JSON.parse(sessionStorage.getItem('prop_current_user') || '{}');" +
            "u.recentlyViewed = " + sb.toString() + ";" +
            "sessionStorage.setItem('prop_current_user', JSON.stringify(u));" +
            "Auth.updateUser(u);" +
            "if (typeof initDashboard === 'function') initDashboard();"
        );
    }

    // ── Panel Navigation ─────────────────────────────────────────────────────
    public void openOverviewSection() {
        wait.until(ExpectedConditions.elementToBeClickable(NAV_OVERVIEW)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_OVERVIEW));
    }

    public void openFavoritesSection() {
        wait.until(ExpectedConditions.elementToBeClickable(NAV_FAVORITES)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_FAVORITES));
    }

    public void openRecentSection() {
        wait.until(ExpectedConditions.elementToBeClickable(NAV_RECENT)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECENT));
    }

    public void openRecommendationsSection() {
        wait.until(ExpectedConditions.elementToBeClickable(NAV_RECOMMENDATIONS)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECOMMENDATIONS));
    }

    public void openPreferencesSection() {
        wait.until(ExpectedConditions.elementToBeClickable(NAV_PREFERENCES)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_PREFERENCES));
    }

    // ── Favorites Actions ────────────────────────────────────────────────────
    public void removeFavorite(int propertyId) {
        By removeBtn = By.id("btn-remove-fav-" + propertyId);
        wait.until(ExpectedConditions.elementToBeClickable(removeBtn)).click();
    }

    // ── Preferences Actions ──────────────────────────────────────────────────
    public void enterNaturalLanguagePreference(String text) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_NL));
        field.clear();
        field.sendKeys(text);
    }

    public void selectPreferredCity(String city) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_CITY));
        new Select(el).selectByVisibleText(city);
    }

    public void selectPreferredPurpose(String purpose) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_PURPOSE));
        new Select(el).selectByVisibleText(purpose);
    }

    public void selectPreferredBhk(String bhk) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_BHK));
        new Select(el).selectByVisibleText(bhk.equals("1") ? "1 BHK" : bhk.equals("2") ? "2 BHK" : bhk.equals("3") ? "3 BHK" : "4 BHK+");
    }

    public void enterMaxBudget(String budget) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_BUDGET));
        field.clear();
        field.sendKeys(budget);
    }

    public void clickSavePreferences() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE_PREFERENCES)).click();
    }

    public void toggleAlerts() {
        wait.until(ExpectedConditions.elementToBeClickable(TOGGLE_TRACK)).click();
    }

    // ── Query Methods ────────────────────────────────────────────────────────
    public boolean isFavoritePresent(int propertyId) {
        try {
            return !driver.findElements(By.id("btn-remove-fav-" + propertyId)).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public int getFavoritesCount() {
        try {
            WebElement list = wait.until(ExpectedConditions.visibilityOfElementLocated(FAVS_LIST));
            return list.findElements(By.className("mini-card")).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isRecentlyViewedListDisplayed() {
        try {
            WebElement list = wait.until(ExpectedConditions.visibilityOfElementLocated(RECENT_LIST));
            return !list.findElements(By.className("mini-card")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstRecentlyViewedTitle() {
        try {
            WebElement list = wait.until(ExpectedConditions.visibilityOfElementLocated(RECENT_LIST));
            List<WebElement> cards = list.findElements(By.className("mini-card"));
            if (!cards.isEmpty()) {
                return cards.get(0).getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isPrefSavedAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_SAVED_ALERT));
            return !alert.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPrefSavedAlertText() {
        return driver.findElement(PREF_SAVED_ALERT).getText().trim();
    }

    public boolean areRecommendationsVisible() {
        try {
            WebElement grid = wait.until(ExpectedConditions.visibilityOfElementLocated(RECO_GRID));
            return !grid.findElements(By.className("card")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean arePreferenceRecommendationsVisible() {
        try {
            WebElement grid = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_RECO_GRID));
            return !grid.findElements(By.className("card")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getAlertStatusText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ALERT_STATUS_LBL)).getText().trim();
    }

    public boolean isUserLoggedIn() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(USER_NAME)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
