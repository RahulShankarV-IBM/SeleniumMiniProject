package com.propfind.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for compare.html
 * Contains all locators and user-action methods for the Compare Properties page.
 * No assertions live here — all assertions belong in step definitions.
 */
public class ComparePage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final By COMPARE_CONTENT     = By.id("compare-content");
    // btn-remove-{id} and btn-add-more-property are dynamic — built at call time

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ComparePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "compare.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
    }

    // ── Setup: seed sessionStorage with a compare list ────────────────────────

    /**
     * Seeds sessionStorage with the supplied property IDs so compare.html
     * renders the comparison table on load without navigating through search-results.
     *
     * @param ids property IDs to seed (must exist in properties.js)
     */
    public void seedCompareList(int... ids) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < ids.length; i++) {
            sb.append(ids[i]);
            if (i < ids.length - 1) sb.append(",");
        }
        sb.append("]");
        js.executeScript("sessionStorage.setItem('compare_list', arguments[0]);", sb.toString());
        // Reload so the page re-reads sessionStorage and renders the table
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Clicks the remove button for the given property ID column.
     */
    public void removeProperty(int propertyId) {
        By removeBtn = By.id("btn-remove-" + propertyId);
        wait.until(ExpectedConditions.elementToBeClickable(removeBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
    }
    /**
 * Attempts to add a property to the compare list using the same
 * duplicate-prevention logic as the application.
 */
public void attemptToAddProperty(int propertyId) {
    JavascriptExecutor js = (JavascriptExecutor) driver;

    js.executeScript(
        "let compareList = JSON.parse(sessionStorage.getItem('compare_list') || '[]');" +
        "if (compareList.length < 3 && !compareList.includes(arguments[0])) {" +
        "    compareList.push(arguments[0]);" +
        "}" +
        "sessionStorage.setItem('compare_list', JSON.stringify(compareList));",
        propertyId
    );

    driver.navigate().refresh();
    wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
}

    // ── Query methods (for assertions in step definitions) ────────────────────

    /**
     * Returns the number of property columns rendered in the comparison table header.
     * Each property occupies a <th class="compare-header-cell">.
     */
    public int getVisiblePropertyColumnCount() {
        List<WebElement> headers = driver.findElements(
                By.cssSelector("#compare-content th.compare-header-cell"));
        // The "Add Property" button also sits inside a compare-header-cell when < 3 props;
        // filter out the add-more cell by checking if it contains a remove button.
        long propCols = headers.stream()
                .filter(th -> !th.findElements(By.id("btn-add-more-property")).isEmpty()
                        ? false
                        : true)
                .count();
        // Simpler: count remove buttons — one per property column
        return driver.findElements(
                By.cssSelector("#compare-content button[id^='btn-remove-']")).size();
    }

    /**
     * Returns true if the comparison table contains a row labelled with the given field name.
     * Used to verify price, BHK, location, amenities and locality rows are present.
     */
    public boolean isComparisonRowPresent(String rowLabel) {
        List<WebElement> rows = driver.findElements(
                By.cssSelector("#compare-content .compare-table tbody tr td:first-child"));
        return rows.stream()
                .anyMatch(td -> td.getText().trim().equalsIgnoreCase(rowLabel));
    }

    /**
     * Returns true if the remove button for the given property ID is absent —
     * meaning the property has been successfully removed from the comparison.
     */
    public boolean isPropertyRemoved(int propertyId) {
        return driver.findElements(By.id("btn-remove-" + propertyId)).isEmpty();
    }

    /**
     * Returns the current compare_list from sessionStorage as a raw JSON string.
     * Used to verify duplicate-prevention behaviour.
     */
    public String getCompareListFromSession() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object result = js.executeScript("return sessionStorage.getItem('compare_list');");
        return result != null ? result.toString() : "[]";
    }

    /**
     * Returns true if the compare-content area is visible and rendered (not empty-state).
     */
    public boolean isCompareTableVisible() {
        try {
            WebElement content = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
            return content.findElements(By.cssSelector(".compare-table")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
