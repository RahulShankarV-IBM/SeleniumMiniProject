package com.propfind.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for map-view.html
 * Contains all locators and user-action methods for the Map View page.
 * No assertions live here — all assertions belong in step definitions.
 */
public class MapViewPage {

    // ── Locators ─────────────────────────────────────────────────────────────
    private static final By MAP_SEARCH_INPUT = By.id("map-search-input");
    private static final By MAP_PURPOSE      = By.id("map-purpose");
    private static final By MAP_BHK          = By.id("map-bhk");
    private static final By BTN_PROPS        = By.id("btn-props");
    private static final By BTN_SCHOOLS      = By.id("btn-schools");
    private static final By BTN_HOSPITALS    = By.id("btn-hospitals");
    private static final By BTN_METRO        = By.id("btn-metro");
    private static final By MAP_COUNT        = By.id("map-count");
    private static final By MAP_PROP_LIST    = By.id("map-prop-list");
    private static final By MAP              = By.id("map");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public MapViewPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "map-view.html");
        wait.until(ExpectedConditions.visibilityOfElementLocated(MAP));
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void clickPropsOverlay() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_PROPS)).click();
    }

    public void clickSchoolsOverlay() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_SCHOOLS)).click();
    }

    public void clickHospitalsOverlay() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_HOSPITALS)).click();
    }

    public void clickMetroOverlay() {
        wait.until(ExpectedConditions.elementToBeClickable(BTN_METRO)).click();
    }

    public void enterSearchLocation(String location) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(MAP_SEARCH_INPUT));
        field.clear();
        field.sendKeys(location);
    }

    public void selectPurpose(String purpose) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(MAP_PURPOSE));
        new Select(el).selectByVisibleText(purpose);
    }

    public void selectBhk(String bhk) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(MAP_BHK));
        new Select(el).selectByVisibleText(bhk);
    }

    // ── Query methods (for assertions in step definitions) ────────────────────

    public boolean isMapVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(MAP)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isPropListVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(MAP_PROP_LIST)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMapCountVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(MAP_COUNT)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getMapCountText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(MAP_COUNT)).getText().trim();
    }

    public boolean propListHasListings() {
        WebElement list = wait.until(ExpectedConditions.visibilityOfElementLocated(MAP_PROP_LIST));
        List<WebElement> items = list.findElements(By.cssSelector("[id^='list-']"));
        return !items.isEmpty();
    }
}
