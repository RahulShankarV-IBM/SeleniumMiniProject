import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for compare.html (US08 – TC36–TC40).
 * No assertions live here — all assertions belong in step definitions.
 */
public class ComparePage {

    // ── Locators ──────────────────────────────────────────────────────────────
    private static final By COMPARE_CONTENT = By.id("compare-content");

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

    // ── Session seeding ───────────────────────────────────────────────────────

    /**
     * Seeds the sessionStorage compare_list with the given property IDs,
     * then reloads compare.html so the JS picks them up.
     */
    public void seedCompareList(int... ids) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < ids.length; i++) {
            json.append(ids[i]);
            if (i < ids.length - 1) json.append(",");
        }
        json.append("]");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("sessionStorage.setItem('compare_list', arguments[0]);", json.toString());
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
    }

    /**
     * Clears the compare_list from sessionStorage and reloads the page.
     */
    public void clearCompareList() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("sessionStorage.removeItem('compare_list');");
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Clicks the remove button for the property with the given ID.
     * Dynamic ID pattern: btn-remove-{id}
     */
    public void removeProperty(int propertyId) {
        By removeBtn = By.id("btn-remove-" + propertyId);
        wait.until(ExpectedConditions.elementToBeClickable(removeBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT));
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    /** Returns the number of property columns in the comparison table header. */
    public int getPropertyColumnCount() {
        try {
            List<WebElement> headers = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(COMPARE_CONTENT))
                    .findElements(By.cssSelector(".compare-table thead tr th.compare-header-cell"));
            // Subtract 1 if the "Add Property" placeholder cell is present
            long addCells = headers.stream()
                    .filter(h -> h.findElements(By.cssSelector(".add-more-btn")).size() > 0)
                    .count();
            return (int)(headers.size() - addCells);
        } catch (Exception e) {
            return 0;
        }
    }

    /** Returns true when the empty-state / "select at least 2 properties" message is visible. */
    public boolean isEmptyStateVisible() {
        try {
            WebElement content = driver.findElement(COMPARE_CONTENT);
            return content.findElements(By.cssSelector(".empty-compare")).size() > 0
                    && content.findElement(By.cssSelector(".empty-compare")).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true if the comparison table contains a row with the given label text. */
    public boolean isRowPresent(String rowLabel) {
        try {
            List<WebElement> firstCells = driver.findElements(
                    By.cssSelector(".compare-table tbody tr td:first-child"));
            return firstCells.stream()
                    .anyMatch(td -> td.getText().trim().equalsIgnoreCase(rowLabel));
        } catch (Exception e) {
            return false;
        }
    }

    /** Returns true when the comparison table is rendered (at least 2 property columns). */
    public boolean isCompareTableVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".compare-table")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
