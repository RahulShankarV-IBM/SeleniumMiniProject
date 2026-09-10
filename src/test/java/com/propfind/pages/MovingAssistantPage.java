package com.propfind.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for moving-assistant.html (US15 — TC71–TC75).
 * Contains all locators and user-action methods for the Moving Assistant page.
 * No assertions live here — all assertions belong in step definitions.
 */
public class MovingAssistantPage {

    // ── Locators ──────────────────────────────────────────────────────────────

    // Tab navigation
    private static final By TABS_BAR               = By.id("tabs-bar");
    private static final By TAB_BTN_SUPPORT         = By.id("tab-btn-support");
    private static final By TAB_BTN_DOCUMENTS       = By.id("tab-btn-documents");
    private static final By TAB_BTN_CHECKLIST       = By.id("tab-btn-checklist");
    private static final By TAB_BTN_GUIDANCE        = By.id("tab-btn-guidance");
    private static final By TAB_BTN_PROGRESS        = By.id("tab-btn-progress");

    // Tab panels
    private static final By TAB_SUPPORT             = By.id("tab-support");
    private static final By TAB_DOCUMENTS           = By.id("tab-documents");
    private static final By TAB_CHECKLIST           = By.id("tab-checklist");
    private static final By TAB_GUIDANCE            = By.id("tab-guidance");
    private static final By TAB_PROGRESS            = By.id("tab-progress");

    // Legal Support accordions
    private static final By ACC1                    = By.id("acc1");

    // Documents
    private static final By BTN_DL_RENTAL_AGREEMENT   = By.id("btn-dl-rental-agreement");
    private static final By BTN_DL_INVENTORY          = By.id("btn-dl-inventory");
    private static final By BTN_DL_KYC                = By.id("btn-dl-kyc");

    // Checklists
    private static final By CHECKLIST_TENANT       = By.id("checklist-tenant");
    private static final By CHECKLIST_PROPERTY     = By.id("checklist-property");
    private static final By CHECKLIST_MOVEIN       = By.id("checklist-movein");

    // Progress
    private static final By PROG_PCT               = By.id("prog-pct");
    private static final By PROG_FILL              = By.id("prog-fill");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public MovingAssistantPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "moving-assistant.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(TABS_BAR));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void clickLegalSupportTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_BTN_SUPPORT)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_SUPPORT));
    }

    public void clickDocumentsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_BTN_DOCUMENTS)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_DOCUMENTS));
    }

    public void clickChecklistsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_BTN_CHECKLIST)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_CHECKLIST));
    }

    public void clickMovingGuidanceTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_BTN_GUIDANCE)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_GUIDANCE));
    }

    public void clickMyProgressTab() {
        wait.until(ExpectedConditions.elementToBeClickable(TAB_BTN_PROGRESS)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_PROGRESS));
    }

    public void clickDownloadButton(String document) {
        By locator;
        switch (document) {
            case "Rental Agreement":     locator = BTN_DL_RENTAL_AGREEMENT; break;
            case "Inventory Checklist":  locator = BTN_DL_INVENTORY;        break;
            case "KYC Checklist":        locator = BTN_DL_KYC;              break;
            default: throw new IllegalArgumentException("Unknown document: " + document);
        }
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    public boolean isLegalSupportPanelVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_SUPPORT)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRentalAgreementAccordionDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(ACC1)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDocumentsPanelVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_DOCUMENTS)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTenantChecklistDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(CHECKLIST_TENANT)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPropertyChecklistDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(CHECKLIST_PROPERTY)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMoveInChecklistDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(CHECKLIST_MOVEIN)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMovingGuidancePanelVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_GUIDANCE)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProgressPanelVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(TAB_PROGRESS)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProgressPercentageDisplayed() {
        try {
            WebElement pct = wait.until(ExpectedConditions.visibilityOfElementLocated(PROG_PCT));
            return pct.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProgressBarDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(PROG_FILL)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
