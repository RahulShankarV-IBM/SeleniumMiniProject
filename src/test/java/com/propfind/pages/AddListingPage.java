package com.propfind.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for add-listing.html (US13 — TC61–TC65).
 * Contains all locators and user-action methods for the Add Listing page.
 * No assertions live here — all assertions belong in step definitions.
 */
public class AddListingPage {

    // ── Locators ──────────────────────────────────────────────────────────────

    // Form & alert
    private static final By LISTING_FORM        = By.id("listing-form");
    private static final By LISTING_ALERT       = By.id("listing-alert");

    // Section 1 — Basic Info
    private static final By F_TITLE             = By.id("f-title");
    private static final By F_PURPOSE           = By.id("f-purpose");
    private static final By F_TYPE              = By.id("f-type");
    private static final By F_PRICE             = By.id("f-price");
    private static final By F_BHK               = By.id("f-bhk");

    // Section 2 — Location & Details
    private static final By F_CITY              = By.id("f-city");
    private static final By F_LOCALITY          = By.id("f-locality");
    private static final By F_PINCODE           = By.id("f-pincode");
    private static final By F_AREA              = By.id("f-area");

    // Section 4 — Media
    private static final By PHOTO_INPUT         = By.id("photo-input");
    private static final By PHOTO_PREVIEW       = By.id("photo-preview");

    // Section 5 — Owner Info & Review
    private static final By F_OWNER_NAME        = By.id("f-owner-name");
    private static final By F_TERMS             = By.id("f-terms");
    private static final By BTN_SUBMIT_LISTING  = By.id("btn-submit-listing");
    private static final By SUCCESS_MODAL       = By.id("success-modal");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public AddListingPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "add-listing.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(LISTING_FORM));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void enterTitle(String title) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(F_TITLE));
        field.clear();
        field.sendKeys(title);
    }

    public void selectPurpose(String purpose) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(F_PURPOSE)));
        select.selectByVisibleText(purpose);
    }

    public void selectType(String type) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(F_TYPE)));
        select.selectByVisibleText(type);
    }

    public void enterPrice(String price) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(F_PRICE));
        field.clear();
        field.sendKeys(price);
    }

    public void selectBhk(String bhk) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(F_BHK)));
        select.selectByVisibleText(bhk);
    }

    public void selectCity(String city) {
        Select select = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(F_CITY)));
        select.selectByVisibleText(city);
    }

    public void enterLocality(String locality) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(F_LOCALITY));
        field.clear();
        field.sendKeys(locality);
    }

    public void enterPincode(String pincode) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(F_PINCODE));
        field.clear();
        field.sendKeys(pincode);
    }

    public void enterArea(String area) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(F_AREA));
        field.clear();
        field.sendKeys(area);
    }

    public void enterOwnerName(String ownerName) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(F_OWNER_NAME));
        field.clear();
        field.sendKeys(ownerName);
    }

    public void acceptTerms() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(F_TERMS));
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void submitListingForm() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SUBMIT_LISTING)).click();
        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(SUCCESS_MODAL),
            ExpectedConditions.visibilityOfElementLocated(LISTING_ALERT)
        ));
    }

    public void submitListingFormEmpty() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SUBMIT_LISTING)).click();
    }

    public void uploadPhoto(String absoluteFilePath) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(PHOTO_INPUT));
        input.sendKeys(absoluteFilePath);
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    public boolean isSuccessModalVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_MODAL)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isListingAlertVisible() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(LISTING_ALERT));
            return !alert.getText().isBlank();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areRequiredFieldsInvalid() {
        org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
        WebElement titleField = driver.findElement(F_TITLE);
        return !(Boolean) js.executeScript("return arguments[0].validity.valid;", titleField);
    }

    public boolean isPhotoPreviewPopulated() {
        try {
            WebElement preview = wait.until(ExpectedConditions.presenceOfElementLocated(PHOTO_PREVIEW));
            return !preview.findElements(By.tagName("img")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
