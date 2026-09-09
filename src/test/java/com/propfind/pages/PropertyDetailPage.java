package com.propfind.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for property-detail.html
 * Contains all locators and user-action methods for the Property Detail page.
 * No assertions live here — all assertions belong in step definitions.
 */
public class PropertyDetailPage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final By DETAIL_CONTENT       = By.id("detail-content");
    private static final By MAIN_IMG             = By.id("main-img");
    private static final By VIRTUAL_TOUR         = By.id("virtual-tour");
    private static final By BTN_CONTACT_OWNER    = By.id("btn-contact-owner");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public PropertyDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Navigates to property-detail.html for a given property ID and waits for
     * the dynamic content wrapper to be present.
     */
    public void navigateTo(String baseUrl, int propertyId) {
        driver.get(baseUrl + "property-detail.html?id=" + propertyId);
        wait.until(ExpectedConditions.presenceOfElementLocated(DETAIL_CONTENT));
        // Wait until JS has finished rendering (main-img appears when a valid property loads)
        wait.until(ExpectedConditions.visibilityOfElementLocated(MAIN_IMG));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /** Clicks the virtual tour section to trigger the tour embed. */
    public void clickVirtualTour() {
        WebElement tourSection = wait.until(ExpectedConditions.elementToBeClickable(VIRTUAL_TOUR));
        tourSection.click();
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    /** Returns true when the detail-content wrapper contains rendered property HTML. */
    public boolean isDetailContentDisplayed() {
        try {
            WebElement content = wait.until(ExpectedConditions.visibilityOfElementLocated(DETAIL_CONTENT));
            return !content.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the main property image is visible. */
    public boolean isMainImageVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(MAIN_IMG)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns the full text of the detail-content section for assertions. */
    public String getDetailContentText() {
        return driver.findElement(DETAIL_CONTENT).getText();
    }

    /** Returns true when the virtual tour section contains an iframe (tour loaded). */
    public boolean isVirtualTourDisplayed() {
        try {
            WebElement tour = wait.until(ExpectedConditions.visibilityOfElementLocated(VIRTUAL_TOUR));
            return tour.isDisplayed() && !tour.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the "Contact Owner" button is visible in the sidebar. */
    public boolean isContactOwnerButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_CONTACT_OWNER)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the nearby facilities section contains at least one school entry. */
    public boolean isSchoolDisplayed() {
        return getDetailContentText().toLowerCase().contains("school");
    }

    /** Returns true when the nearby facilities section contains at least one hospital entry. */
    public boolean isHospitalDisplayed() {
        return getDetailContentText().toLowerCase().contains("hospital");
    }

    /** Returns true when the nearby facilities section contains a police station entry. */
    public boolean isPoliceStationDisplayed() {
        return getDetailContentText().contains("Police Station");
    }

    /** Returns true when the nearby facilities section contains a fire station entry. */
    public boolean isFireStationDisplayed() {
        return getDetailContentText().contains("Fire Station");
    }

    /** Returns true when the nearby facilities section contains at least one supermarket entry. */
    public boolean isSupermarketDisplayed() {
        return getDetailContentText().toLowerCase().contains("supermarket");
    }

    /** Returns true when the nearby facilities section contains at least one metro station entry. */
    public boolean isMetroDisplayed() {
        return getDetailContentText().toLowerCase().contains("metro");
    }

    /** Returns true when the locality insights section is rendered (locality score rows present). */
    public boolean isLocalityInsightsDisplayed() {
        List<WebElement> scoreRows = driver.findElements(By.cssSelector(".score-row"));
        return !scoreRows.isEmpty();
    }

    /**
     * Returns true when both Police Station and Fire Station entries are present
     * in the nearby facilities section (covers TC30 emergency support check).
     */
    public boolean isEmergencyFacilitiesDisplayed() {
        String text = getDetailContentText();
        return text.contains("Police Station") && text.contains("Fire Station");
    }
}
