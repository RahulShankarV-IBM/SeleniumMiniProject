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
 * Page Object for search-results.html — US02 (TC06–TC10) and US03 (TC11–TC15).
 */
public class SearchResultsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // ── Locators ─────────────────────────────────────────────────────────────

    // Search bar
    private static final By LOC_SEARCH_INPUT   = By.id("search-loc");
    private static final By LOC_PURPOSE_SELECT = By.id("search-purpose");
    private static final By LOC_TYPE_SELECT    = By.id("search-type");
    private static final By LOC_BTN_SEARCH     = By.id("btn-search");

    // Results header
    private static final By LOC_RESULTS_COUNT  = By.id("results-count");
    private static final By LOC_SORT_SELECT    = By.id("sort-by");

    // Sidebar filters — budget
    private static final By LOC_F_MIN          = By.id("f-min");
    private static final By LOC_F_MAX          = By.id("f-max");

    // Sidebar filters — BHK buttons
    private static final By LOC_BHK_1          = By.id("bhk-btn-1");
    private static final By LOC_BHK_2          = By.id("bhk-btn-2");
    private static final By LOC_BHK_3          = By.id("bhk-btn-3");
    private static final By LOC_BHK_4          = By.id("bhk-btn-4");

    // Sidebar filters — furnishing radios (located by name + value)
    private static final By LOC_FURNISH_ANY  = By.cssSelector("input[name='furnish'][value='']");
    private static final By LOC_FURNISH_FULL = By.cssSelector("input[name='furnish'][value='Fully Furnished']");
    private static final By LOC_FURNISH_SEMI = By.cssSelector("input[name='furnish'][value='Semi-Furnished']");
    private static final By LOC_FURNISH_UN   = By.cssSelector("input[name='furnish'][value='Unfurnished']");

    // Sidebar filters — amenity checkboxes
    private static final By LOC_F_WIFI     = By.id("f-wifi");
    private static final By LOC_F_PARKING  = By.id("f-parking");
    private static final By LOC_F_PET      = By.id("f-pet");
    private static final By LOC_F_POWER    = By.id("f-power");
    private static final By LOC_F_GATED    = By.id("f-gated");
    private static final By LOC_F_CCTV     = By.id("f-cctv");
    private static final By LOC_F_VERIFIED = By.id("f-verified");

    // Sidebar filters — property type checkboxes
    private static final By LOC_TYPE_APARTMENT = By.id("type-chk-apartment");
    private static final By LOC_TYPE_VILLA      = By.id("type-chk-villa");
    private static final By LOC_TYPE_IND_HOUSE  = By.id("type-chk-independent-house");
    private static final By LOC_TYPE_STUDIO     = By.id("type-chk-studio");
    private static final By LOC_TYPE_BUILDER    = By.id("type-chk-builder-floor");
    private static final By LOC_TYPE_PENTHOUSE  = By.id("type-chk-penthouse");

    // Filter action buttons
    private static final By LOC_BTN_APPLY = By.id("btn-apply-filters");
    private static final By LOC_BTN_CLEAR = By.id("btn-clear-filters");

    // Results grid + no-results
    private static final By LOC_RESULTS_GRID = By.id("results-grid");
    private static final By LOC_NO_RESULTS   = By.id("no-results");
    private static final By LOC_CARD_TITLE   = By.cssSelector("#results-grid .card-title");
    private static final By LOC_CARD_PRICE   = By.cssSelector("#results-grid .card-price");

    // ── Constructor ───────────────────────────────────────────────────────────

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "search-results.html");
        waitForResultsGridReady();
    }

    // ── Search bar actions ────────────────────────────────────────────────────

    public void enterLocation(String location) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(LOC_SEARCH_INPUT));
        el.clear();
        el.sendKeys(location);
    }

    public void selectPurpose(String purpose) {
        new Select(driver.findElement(LOC_PURPOSE_SELECT)).selectByVisibleText(purpose);
    }

    public void clickSearch() {
        jsClick(driver.findElement(LOC_BTN_SEARCH));
        waitForResultsGridReady();
    }

    // ── Sidebar filter actions ────────────────────────────────────────────────

    public void enterMinBudget(String min) {
        WebElement el = driver.findElement(LOC_F_MIN);
        el.clear();
        el.sendKeys(min);
    }

    public void enterMaxBudget(String max) {
        WebElement el = driver.findElement(LOC_F_MAX);
        el.clear();
        el.sendKeys(max);
    }

    public void clickBhkButton(int bhk) {
        By locator = switch (bhk) {
            case 1  -> LOC_BHK_1;
            case 2  -> LOC_BHK_2;
            case 3  -> LOC_BHK_3;
            default -> LOC_BHK_4;
        };
        driver.findElement(locator).click();
        waitForResultsGridReady();
    }

    public void selectFurnishing(String value) {
        By locator = switch (value) {
            case "Fully Furnished" -> LOC_FURNISH_FULL;
            case "Semi-Furnished"  -> LOC_FURNISH_SEMI;
            case "Unfurnished"     -> LOC_FURNISH_UN;
            default                -> LOC_FURNISH_ANY;
        };
        driver.findElement(locator).click();
        waitForResultsGridReady();
    }

    public void checkAmenity(String amenity) {
        By locator = switch (amenity.toLowerCase()) {
            case "parking"         -> LOC_F_PARKING;
            case "pet friendly"    -> LOC_F_PET;
            case "wi-fi", "wifi"   -> LOC_F_WIFI;
            case "power backup"    -> LOC_F_POWER;
            case "gated community" -> LOC_F_GATED;
            case "cctv"            -> LOC_F_CCTV;
            case "verified"        -> LOC_F_VERIFIED;
            default -> throw new IllegalArgumentException("Unknown amenity: " + amenity);
        };
        WebElement chk = driver.findElement(locator);
        if (!chk.isSelected()) chk.click();
    }

    public void checkPropertyType(String type) {
        By locator = switch (type.toLowerCase()) {
            case "apartment"         -> LOC_TYPE_APARTMENT;
            case "villa"             -> LOC_TYPE_VILLA;
            case "independent house" -> LOC_TYPE_IND_HOUSE;
            case "studio"            -> LOC_TYPE_STUDIO;
            case "builder floor"     -> LOC_TYPE_BUILDER;
            case "penthouse"         -> LOC_TYPE_PENTHOUSE;
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
        WebElement chk = driver.findElement(locator);
        if (!chk.isSelected()) chk.click();
    }

    public void clickApplyFilters() {
        jsClick(driver.findElement(LOC_BTN_APPLY));
        waitForResultsGridReady();
    }

    public void clickClearAllFilters() {
        jsClick(driver.findElement(LOC_BTN_CLEAR));
        waitForResultsGridReady();
    }

    public void selectSortOption(String visibleText) {
        new Select(driver.findElement(LOC_SORT_SELECT)).selectByVisibleText(visibleText);
        waitForResultsGridReady();
    }

    // ── Result queries ────────────────────────────────────────────────────────

    public int getResultCount() {
        String text = driver.findElement(LOC_RESULTS_COUNT).getText();
        return Integer.parseInt(text.replaceAll("[^0-9]", "").trim());
    }

    public boolean isNoResultsMessageVisible() {
        return driver.findElement(LOC_NO_RESULTS).isDisplayed();
    }

    public List<WebElement> getResultCards() {
        return driver.findElements(By.cssSelector("#results-grid .card"));
    }

    public List<String> getDisplayedCardTitles() {
        return driver.findElements(LOC_CARD_TITLE)
                     .stream()
                     .map(WebElement::getText)
                     .toList();
    }

    public List<String> getDisplayedCardPrices() {
        return driver.findElements(LOC_CARD_PRICE)
                     .stream()
                     .map(WebElement::getText)
                     .toList();
    }

    /**
     * Parses a price label to an integer for sort-order comparison.
     * Handles: "₹45,000/mo", "₹85 Lakhs", "₹2.5 Cr"
     */
    public int parsePriceFromLabel(String label) {
        String n = label.trim().toLowerCase();
        if (n.contains("cr")) {
            return (int)(Double.parseDouble(n.replaceAll("[^0-9.]", "").trim()) * 10_000_000);
        } else if (n.contains("lakh") || n.contains("lac")) {
            return (int)(Double.parseDouble(n.replaceAll("[^0-9.]", "").trim()) * 100_000);
        } else {
            return Integer.parseInt(n.replaceAll("[^0-9]", "").trim());
        }
    }

    public boolean isBhkButtonActive(int bhk) {
        By locator = switch (bhk) {
            case 1  -> LOC_BHK_1;
            case 2  -> LOC_BHK_2;
            case 3  -> LOC_BHK_3;
            default -> LOC_BHK_4;
        };
        return driver.findElement(locator).getAttribute("class").contains("btn-primary");
    }

    public boolean isAmenityFilterChecked(String amenity) {
        By locator = switch (amenity.toLowerCase()) {
            case "parking"         -> LOC_F_PARKING;
            case "pet friendly"    -> LOC_F_PET;
            case "wi-fi", "wifi"   -> LOC_F_WIFI;
            case "power backup"    -> LOC_F_POWER;
            case "gated community" -> LOC_F_GATED;
            case "cctv"            -> LOC_F_CCTV;
            case "verified"        -> LOC_F_VERIFIED;
            default -> throw new IllegalArgumentException("Unknown amenity: " + amenity);
        };
        return driver.findElement(locator).isSelected();
    }

    public String getLocationFieldValue() {
        return driver.findElement(LOC_SEARCH_INPUT).getAttribute("value");
    }

    public String getPurposeDropdownValue() {
        return new Select(driver.findElement(LOC_PURPOSE_SELECT)).getFirstSelectedOption().getText();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void waitForResultsGridReady() {
        wait.until(ExpectedConditions.presenceOfElementLocated(LOC_RESULTS_GRID));
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
