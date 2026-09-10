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
 * Page Object for dashboard.html — US07 (TC31–TC35) and US11 (TC51–TC55).
 *
 * loginAs() seeds localStorage/sessionStorage via JS so tests run without
 * a real login form and with fully controlled initial state.
 */
public class DashboardPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ─────────────────────────────────────────────────────────────

    private static final By LOGIN_ID_FIELD      = By.id("login-id");

    // Sidebar nav links
    private static final By NAV_FAVORITES       = By.id("nav-favorites");
    private static final By NAV_RECENT          = By.id("nav-recent");
    private static final By NAV_RECOMMENDATIONS = By.id("nav-recommendations");
    private static final By NAV_PREFERENCES     = By.id("nav-preferences");

    // Panels
    private static final By PANEL_FAVORITES       = By.id("panel-favorites");
    private static final By FAVS_LIST             = By.id("favs-list");
    private static final By PANEL_RECENT          = By.id("panel-recent");
    private static final By RECENT_LIST           = By.id("recent-list");
    private static final By PANEL_RECOMMENDATIONS = By.id("panel-recommendations");
    private static final By RECO_GRID             = By.id("reco-grid");
    private static final By PANEL_PREFERENCES     = By.id("panel-preferences");

    // Preferences form
    private static final By PREF_CITY        = By.id("pref-city");
    private static final By PREF_PURPOSE     = By.id("pref-purpose");
    private static final By PREF_BHK         = By.id("pref-bhk");
    private static final By PREF_BUDGET      = By.id("pref-budget");
    private static final By PREF_NL          = By.id("pref-nl");
    private static final By BTN_SAVE_PREFS   = By.id("btn-save-preferences");
    private static final By PREF_SAVED_ALERT = By.id("pref-saved-alert");

    // Alerts toggle (opacity:0 element — must use presence, not visibility)
    private static final By ALERTS_TOGGLE    = By.id("alerts-toggle");
    private static final By ALERT_STATUS_LBL = By.id("alert-status-lbl");

    // ── Constructor ───────────────────────────────────────────────────────────

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Seeds the browser storage with a fully-populated demo user (with favorites
     * and recentlyViewed pre-populated) then navigates straight to dashboard.html.
     * This bypasses the login form entirely so tests are fast and deterministic.
     *
     * @param baseUrl  the file:// base URL of propfind-website
     * @param username demo username (used in the seeded user object)
     * @param password demo password (stored in the seeded user object)
     */
    public void loginAs(String baseUrl, String username, String password) {
        // First visit the login page to get the origin so storage is writable
        driver.get(baseUrl + "login.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_ID_FIELD));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Seed a user that already has 2 favorites and 2 recently-viewed entries
        // so TC31–TC35 have data to work with from the start
        String seedScript = String.format(
            "const user = {" +
            "  id: 1, name: 'Demo User', email: 'demo@test.com'," +
            "  username: '%s', password: '%s'," +
            "  favorites: [1, 2]," +
            "  recentlyViewed: [3, 4]," +
            "  appointments: []" +
            "};" +
            "localStorage.setItem('prop_users', JSON.stringify([user]));" +
            "sessionStorage.setItem('prop_current_user', JSON.stringify(user));",
            username, password
        );
        js.executeScript(seedScript);

        driver.get(baseUrl + "dashboard.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(NAV_FAVORITES));
    }

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "dashboard.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(NAV_FAVORITES));
    }

    // ── Sidebar nav actions ───────────────────────────────────────────────────

    public void clickFavoritesNav() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(NAV_FAVORITES)));
    }

    public void clickRecentNav() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(NAV_RECENT)));
    }

    public void clickRecommendationsNav() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(NAV_RECOMMENDATIONS)));
    }

    public void clickPreferencesNav() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(NAV_PREFERENCES)));
    }

    // ── Panel visibility queries ──────────────────────────────────────────────

    public boolean isFavoritesPanelDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_FAVORITES)).isDisplayed();
    }

    public boolean isRecentPanelDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECENT)).isDisplayed();
    }

    public boolean isRecommendationsPanelDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECOMMENDATIONS)).isDisplayed();
    }

    public boolean isPreferencesPanelDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_PREFERENCES)).isDisplayed();
    }

    // ── Favourites ────────────────────────────────────────────────────────────

    public List<WebElement> getFavoriteCards() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FAVS_LIST));
        return driver.findElement(FAVS_LIST).findElements(By.cssSelector(".mini-card"));
    }

    public int getFavoriteCount() {
        return getFavoriteCards().size();
    }

    public void removeFavoriteAtIndex(int index) {
        List<WebElement> cards = getFavoriteCards();
        if (index < 0 || index >= cards.size()) return;
        WebElement removeBtn = cards.get(index).findElement(By.cssSelector("[id^='btn-remove-fav-']"));
        jsClick(removeBtn);
        wait.until(ExpectedConditions.presenceOfElementLocated(FAVS_LIST));
    }

    // ── Recently Viewed ───────────────────────────────────────────────────────

    public List<WebElement> getRecentCards() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECENT));
        return driver.findElement(RECENT_LIST).findElements(By.cssSelector(".mini-card"));
    }

    // ── Recommendations ───────────────────────────────────────────────────────

    public int getRecommendationCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECOMMENDATIONS));
        return driver.findElement(RECO_GRID).findElements(By.cssSelector(".card")).size();
    }

    // ── Preferences panel ─────────────────────────────────────────────────────

    public void selectCity(String city) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_CITY)))
            .selectByVisibleText(city);
    }

    public void selectPurpose(String purpose) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_PURPOSE)))
            .selectByVisibleText(purpose);
    }

    /** BHK options: Any / 1 BHK / 2 BHK / 3 BHK / 4 BHK+ */
    public void selectBhk(String bhkLabel) {
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_BHK)))
            .selectByVisibleText(bhkLabel);
    }

    public void enterBudget(String budget) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_BUDGET));
        el.clear();
        el.sendKeys(budget);
    }

    public void enterNaturalLanguagePreference(String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_NL));
        el.clear();
        el.sendKeys(text);
    }

    public void clickSavePreferences() {
        jsClick(wait.until(ExpectedConditions.elementToBeClickable(BTN_SAVE_PREFS)));
    }

    public boolean isPreferencesSavedAlertDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("#pref-saved-alert .alert"))).isDisplayed();
    }

    public String getNaturalLanguageText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(PREF_NL))
                   .getAttribute("value");
    }

    // ── Alerts toggle ─────────────────────────────────────────────────────────

    /**
     * The underlying checkbox (id=alerts-toggle) has opacity:0 and zero dimensions,
     * so visibilityOfElementLocated times out. Use presenceOfElement + JS click instead.
     */
    public void enableAlertsToggle() {
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(ALERTS_TOGGLE));
        if (!toggle.isSelected()) jsClick(toggle);
    }

    public void disableAlertsToggle() {
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(ALERTS_TOGGLE));
        if (toggle.isSelected()) jsClick(toggle);
    }

    public String getAlertStatusLabelText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ALERT_STATUS_LBL))
                   .getText().trim();
    }

    // ── Session / state helpers (called from step Given clauses) ──────────────

    /**
     * Returns true when the dashboard sidebar is visible, confirming the session
     * is active (nav-favorites is only rendered when a user is logged in).
     */
    public boolean isUserLoggedIn() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(NAV_FAVORITES)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Adds a single property id to the current user's favorites array in
     * localStorage and re-seeds sessionStorage so the page reflects it on reload.
     */
    public void addFavorite(int propertyId) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "const raw = localStorage.getItem('prop_users');" +
            "if (!raw) return;" +
            "const users = JSON.parse(raw);" +
            "const sess  = JSON.parse(sessionStorage.getItem('prop_current_user') || 'null');" +
            "if (!sess) return;" +
            "const user = users.find(u => u.id === sess.id);" +
            "if (!user) return;" +
            "if (!user.favorites.includes(arguments[0])) user.favorites.push(arguments[0]);" +
            "localStorage.setItem('prop_users', JSON.stringify(users));" +
            "sessionStorage.setItem('prop_current_user', JSON.stringify(user));",
            propertyId);
    }

    /**
     * Replaces the current user's recentlyViewed list with the supplied ids
     * (first argument = most recently viewed).
     */
    public void setRecentlyViewed(int... ids) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        StringBuilder idsJs = new StringBuilder("[");
        for (int i = 0; i < ids.length; i++) {
            idsJs.append(ids[i]);
            if (i < ids.length - 1) idsJs.append(",");
        }
        idsJs.append("]");
        js.executeScript(
            "const raw = localStorage.getItem('prop_users');" +
            "if (!raw) return;" +
            "const users = JSON.parse(raw);" +
            "const sess  = JSON.parse(sessionStorage.getItem('prop_current_user') || 'null');" +
            "if (!sess) return;" +
            "const user = users.find(u => u.id === sess.id);" +
            "if (!user) return;" +
            "user.recentlyViewed = " + idsJs + ";" +
            "localStorage.setItem('prop_users', JSON.stringify(users));" +
            "sessionStorage.setItem('prop_current_user', JSON.stringify(user));");
    }

    /**
     * Replaces both favorites and recentlyViewed for the current user in one call.
     */
    public void setUserFavoritesAndRecent(List<Integer> favoriteIds, List<Integer> recentIds) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String favsJs   = favoriteIds.toString();  // e.g. "[1, 2]"
        String recentJs = recentIds.toString();
        js.executeScript(
            "const raw = localStorage.getItem('prop_users');" +
            "if (!raw) return;" +
            "const users = JSON.parse(raw);" +
            "const sess  = JSON.parse(sessionStorage.getItem('prop_current_user') || 'null');" +
            "if (!sess) return;" +
            "const user = users.find(u => u.id === sess.id);" +
            "if (!user) return;" +
            "user.favorites      = " + favsJs + ";" +
            "user.recentlyViewed = " + recentJs + ";" +
            "localStorage.setItem('prop_users', JSON.stringify(users));" +
            "sessionStorage.setItem('prop_current_user', JSON.stringify(user));");
    }

    // ── Section navigation aliases (DashboardSteps naming convention) ─────────

    public void openFavoritesSection()     { clickFavoritesNav(); }
    public void openRecentSection()        { clickRecentNav(); }
    public void openPreferencesSection()   { clickPreferencesNav(); }
    public void openRecommendationsSection() { clickRecommendationsNav(); }

    // ── Preference field aliases ───────────────────────────────────────────────

    public void selectPreferredCity(String city)        { selectCity(city); }
    public void selectPreferredPurpose(String purpose)  { selectPurpose(purpose); }
    public void selectPreferredBhk(String bhk)          { selectBhk(bhk); }
    public void enterMaxBudget(String budget)            { enterBudget(budget); }

    // ── Alert toggle ──────────────────────────────────────────────────────────

    /** Flips the current state of the alerts toggle regardless of direction. */
    public void toggleAlerts() {
        WebElement toggle = wait.until(ExpectedConditions.presenceOfElementLocated(ALERTS_TOGGLE));
        jsClick(toggle);
    }

    public String getAlertStatusText() { return getAlertStatusLabelText(); }

    // ── Favourites query aliases ───────────────────────────────────────────────

    public int getFavoritesCount() { return getFavoriteCount(); }

    /**
     * Returns true when a favorite card whose remove-button id contains the
     * given property id exists in the favorites list.
     */
    public boolean isFavoritePresent(int propertyId) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(FAVS_LIST));
            return !driver.findElements(
                By.id("btn-remove-fav-" + propertyId)).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Removes the favorite card matching the given property id by clicking its
     * remove button directly (id-based, not index-based).
     */
    public void removeFavorite(int propertyId) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(FAVS_LIST));
        WebElement removeBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("btn-remove-fav-" + propertyId)));
        jsClick(removeBtn);
        wait.until(ExpectedConditions.presenceOfElementLocated(FAVS_LIST));
    }

    // ── Recently Viewed query ─────────────────────────────────────────────────

    public boolean isRecentlyViewedListDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECENT));
            return !driver.findElement(RECENT_LIST)
                          .findElements(By.cssSelector(".mini-card")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFirstRecentlyViewedTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_RECENT));
        List<WebElement> cards = driver.findElement(RECENT_LIST)
                                       .findElements(By.cssSelector(".mini-card"));
        if (cards.isEmpty()) return "";
        WebElement title = cards.get(0).findElement(By.cssSelector(".card-title, h4, h3, .title"));
        return title.getText().trim();
    }

    // ── Preferences saved alert ───────────────────────────────────────────────

    public boolean isPrefSavedAlertVisible() { return isPreferencesSavedAlertDisplayed(); }

    public String getPrefSavedAlertText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("#pref-saved-alert .alert"))).getText().trim();
    }

    // ── Recommendations visibility ────────────────────────────────────────────

    public boolean areRecommendationsVisible() {
        try {
            return getRecommendationCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks whether the recommendations grid inside the Preferences panel
     * (id=pref-reco-grid) contains at least one card.
     */
    public boolean arePreferenceRecommendationsVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(PANEL_PREFERENCES));
            return !driver.findElement(By.id("pref-reco-grid"))
                          .findElements(By.cssSelector(".card")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
