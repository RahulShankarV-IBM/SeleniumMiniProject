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
 * Page Object for property-detail.html
 * Contains all locators and user-action methods for the Property Detail page.
 * Covers US05, US06, US09, US10, US14 (TC21–TC30, TC41–TC50, TC66–TC70).
 * No assertions live here — all assertions belong in step definitions.
 */
public class PropertyDetailPage {

    // ── Locators ──────────────────────────────────────────────────────────────
    // Page landmark & main elements
    private static final By DETAIL_CONTENT       = By.id("detail-content");
    private static final By MAIN_IMG             = By.id("main-img");
    private static final By VIRTUAL_TOUR         = By.id("virtual-tour");

    // Action buttons (rendered by JS into #detail-content)
    private static final By BTN_SCHEDULE_VISIT   = By.id("btn-schedule-visit");
    private static final By BTN_CONTACT_OWNER    = By.id("btn-contact-owner");

    // Schedule Visit Modal
    private static final By SCHEDULE_MODAL       = By.id("schedule-modal");
    private static final By VISIT_DATE           = By.id("visit-date");
    private static final By VISIT_TIME           = By.id("visit-time");
    private static final By VISIT_NAME           = By.id("visit-name");
    private static final By VISIT_PHONE          = By.id("visit-phone");
    private static final By BTN_CONFIRM_BOOKING  = By.id("btn-confirm-booking");
    private static final By BTN_CLOSE_SCHEDULE   = By.id("btn-close-schedule-modal");
    private static final By MODAL_ALERT          = By.id("modal-alert");

    // Contact Owner Modal
    private static final By CONTACT_MODAL        = By.id("contact-modal");
    private static final By CONTACT_MSG          = By.id("contact-msg");
    private static final By CONTACT_NAME         = By.id("contact-name");
    private static final By CONTACT_PHONE        = By.id("contact-phone");
    private static final By BTN_SEND_INQUIRY     = By.id("btn-send-inquiry");
    private static final By CONTACT_ALERT        = By.id("contact-alert");

    // Report Modal
    private static final By REPORT_MODAL         = By.id("report-modal");
    private static final By REPORT_REASON        = By.id("report-reason");
    private static final By REPORT_DETAIL        = By.id("report-detail");
    private static final By BTN_SUBMIT_REPORT    = By.id("btn-submit-report");
    private static final By REPORT_ALERT         = By.id("report-alert");

    // Verification badge (no id — CSS selector)
    private static final By VERIFIED_BADGE       = By.cssSelector(".badge-verified");

    // Safety section heading (no id — CSS selector)
    private static final By SAFETY_SECTION       = By.cssSelector("#detail-content .spec-grid");

    // Amenities section (no id — CSS selector)
    private static final By AMENITIES_SECTION    = By.cssSelector(".amenity-tag");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public PropertyDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Navigate to property-detail.html for a specific property by default (ID 1).
     */
    public void navigateTo(String baseUrl) {
        navigateTo(baseUrl, 1);
    }

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

    /**
     * Seeds both localStorage (prop_users) and sessionStorage (prop_current_user)
     * so the page behaves as if a user is logged in — required for Schedule Visit.
     * Must be called after navigating to property-detail.html (same file:// origin).
     */
    public void loginAs(String baseUrl, String username, String password) {
        // Step 1: Navigate to login.html first so localStorage APIs are available
        driver.get(baseUrl + "login.html");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tab-login")));

        // Step 2: Seed localStorage and sessionStorage via JS
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userJson = String.format(
            "{\"id\":99999,\"name\":\"Test User\",\"email\":\"\",\"username\":\"%s\"," +
            "\"password\":\"%s\",\"favorites\":[],\"recentlyViewed\":[],\"appointments\":[]}",
            username, password);
        String usersArray = "[" + userJson + "]";
        js.executeScript("localStorage.setItem('prop_users', arguments[0]);", usersArray);
        js.executeScript("sessionStorage.setItem('prop_current_user', arguments[0]);", userJson);

        // Step 3: Navigate to the protected property page
        navigateTo(baseUrl, 1);
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /** Clicks the virtual tour section to trigger the tour embed. */
    public void clickVirtualTour() {
        WebElement tourSection = wait.until(ExpectedConditions.elementToBeClickable(VIRTUAL_TOUR));
        tourSection.click();
    }

    // ── Schedule Visit actions ─────────────────────────────────────────────────

    public void clickScheduleVisit() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SCHEDULE_VISIT)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(SCHEDULE_MODAL));
    }

    public void selectVisitDate(String date) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(VISIT_DATE));
        field.clear();
        field.sendKeys(date);
    }

    public void selectVisitTimeSlot(String timeSlot) {
        WebElement select = wait.until(ExpectedConditions.visibilityOfElementLocated(VISIT_TIME));
        new Select(select).selectByVisibleText(timeSlot);
    }

    public void enterVisitorName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(VISIT_NAME));
        field.clear();
        field.sendKeys(name);
    }

    public void enterVisitorPhone(String phone) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(VISIT_PHONE));
        field.clear();
        field.sendKeys(phone);
    }

    public void confirmBooking() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_CONFIRM_BOOKING)).click();
    }

    public void closeScheduleModal() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_CLOSE_SCHEDULE)).click();
    }

    // ── Contact Owner actions ──────────────────────────────────────────────────

    public void clickContactOwner() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_CONTACT_OWNER)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(CONTACT_MODAL));
    }

    public void enterInquiryMessage(String message) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTACT_MSG));
        field.clear();
        field.sendKeys(message);
    }

    public void enterInquiryMessageOverLimit(String message) {
        // Use JS to bypass the maxlength="500" attribute so the full 501+ char value is set
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTACT_MSG));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1];", field, message);
        // Trigger an input event so JS character-counter picks up the change
        js.executeScript("arguments[0].dispatchEvent(new Event('input'));", field);
    }

    public void enterContactName(String name) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTACT_NAME));
        field.clear();
        field.sendKeys(name);
    }

    public void enterContactPhone(String phone) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTACT_PHONE));
        field.clear();
        field.sendKeys(phone);
    }

    public void submitInquiry() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SEND_INQUIRY)).click();
    }

    // ── Report Listing actions ─────────────────────────────────────────────────

    public void openReportForm() {
        // The report link is an inline onclick — trigger it via JS
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('report-modal').classList.add('open');");
        wait.until(ExpectedConditions.visibilityOfElementLocated(REPORT_MODAL));
    }

    public void selectReportReason(String reason) {
        WebElement select = wait.until(ExpectedConditions.visibilityOfElementLocated(REPORT_REASON));
        new Select(select).selectByVisibleText(reason);
    }

    public void submitReport() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SUBMIT_REPORT)).click();
    }

    public void submitReportWithoutReason() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SUBMIT_REPORT)).click();
    }

    // ── Query methods ──────────────────────────────────────────────────────────

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

    public boolean isScheduleModalVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(SCHEDULE_MODAL)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isScheduleModalClosed() {
        try {
            WebElement modal = driver.findElement(SCHEDULE_MODAL);
            return !modal.getAttribute("class").contains("open");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isVisitDateVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(VISIT_DATE)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVisitTimeVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(VISIT_TIME)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isBookingConfirmed() {
        // After successful booking the modal closes; check modal is no longer open
        try {
            WebElement modal = driver.findElement(SCHEDULE_MODAL);
            return !modal.getAttribute("class").contains("open");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isModalAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(MODAL_ALERT));
            return !alert.getText().isBlank();
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

    public boolean isInquirySent() {
        // After successful inquiry the contact modal closes
        try {
            WebElement modal = driver.findElement(CONTACT_MODAL);
            return !modal.getAttribute("class").contains("open");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isContactAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(CONTACT_ALERT));
            return !alert.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVerifiedBadgeDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(VERIFIED_BADGE)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReportSubmitted() {
        // After successful report submission the modal closes
        try {
            WebElement modal = driver.findElement(REPORT_MODAL);
            return !modal.getAttribute("class").contains("open");
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isReportAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(REPORT_ALERT));
            return !alert.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSafetySectionDisplayed() {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(SAFETY_SECTION)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAmenitiesSectionDisplayed() {
        try {
            return !driver.findElements(AMENITIES_SECTION).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
