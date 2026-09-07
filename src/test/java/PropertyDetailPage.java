import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for property-detail.html (US05, US06 — TC21–TC30).
 * All content is rendered dynamically by JS into #detail-content after page load,
 * so every query waits for the content wrapper to be populated before reading.
 * No assertions live here.
 */
public class PropertyDetailPage {

    // ── Static shell locator ──────────────────────────────────────────────────
    private static final By DETAIL_CONTENT  = By.id("detail-content");

    // ── Dynamic content locators (rendered into #detail-content by JS) ────────
    private static final By MAIN_IMG        = By.id("main-img");
    private static final By MAIN_IMG_WRAP   = By.id("main-img-wrap");
    private static final By FAV_BTN         = By.id("fav-btn");
    private static final By VIRTUAL_TOUR    = By.id("virtual-tour");
    private static final By BTN_SCHEDULE    = By.id("btn-schedule-visit");
    private static final By BTN_CONTACT     = By.id("btn-contact-owner");

    // ── State ─────────────────────────────────────────────────────────────────
    private final WebDriver driver;
    private final WebDriverWait wait;

    public PropertyDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // ── Navigation ────────────────────────────────────────────────────────────

    /**
     * Navigate to property-detail.html for the given property ID.
     * Waits until JS has rendered the dynamic content (i.e. #detail-content
     * no longer contains the "Loading property..." placeholder text).
     */
    public void navigateTo(String baseUrl, int propertyId) {
        driver.get(baseUrl + "property-detail.html?id=" + propertyId);
        wait.until(driver -> {
            WebElement content = driver.findElement(DETAIL_CONTENT);
            String text = content.getText();
            return !text.contains("Loading property...") && !text.isBlank();
        });
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    public void clickVirtualTour() {
        WebElement tourSection = wait.until(ExpectedConditions.elementToBeClickable(VIRTUAL_TOUR));
        tourSection.click();
    }

    // ── Query methods ─────────────────────────────────────────────────────────

    public boolean isDetailContentDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(DETAIL_CONTENT)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean isTitleVisible() {
        return !getTitleText().isBlank();
    }

    public String getTitleText() {
        try {
            return driver.findElement(DETAIL_CONTENT)
                    .findElement(By.tagName("h1")).getText().trim();
        } catch (Exception e) { return ""; }
    }

    public boolean isLocationVisible() {
        return getLocationText().contains("📍") || getLocationText().length() > 0;
    }

    public String getLocationText() {
        try {
            List<WebElement> paras = driver.findElement(DETAIL_CONTENT)
                    .findElements(By.cssSelector("p"));
            for (WebElement p : paras) {
                String t = p.getText();
                if (t.contains("📍") || t.contains("Koramangala") || t.contains("Bangalore")) return t;
            }
            return "";
        } catch (Exception e) { return ""; }
    }

    public boolean isPriceDisplayed() {
        try {
            List<WebElement> els = driver.findElement(DETAIL_CONTENT)
                    .findElements(By.cssSelector(".price-big"));
            return !els.isEmpty() && !els.get(0).getText().isBlank();
        } catch (Exception e) { return false; }
    }

    public boolean isAreaDisplayed() {
        return getDetailContentText().contains("sq.ft");
    }

    public boolean isBhkDisplayed() {
        return getDetailContentText().contains("BHK");
    }

    public boolean isMainImageLoaded() {
        try {
            WebElement img = wait.until(ExpectedConditions.visibilityOfElementLocated(MAIN_IMG));
            String src = img.getAttribute("src");
            return src != null && !src.isBlank();
        } catch (Exception e) { return false; }
    }

    public boolean areGalleryThumbsVisible() {
        try {
            List<WebElement> thumbs = driver.findElement(DETAIL_CONTENT)
                    .findElements(By.cssSelector(".gallery-thumbs img"));
            return !thumbs.isEmpty();
        } catch (Exception e) { return false; }
    }

    public boolean isVirtualTourDisplayed() {
        try {
            WebElement tour = wait.until(ExpectedConditions.visibilityOfElementLocated(VIRTUAL_TOUR));
            return tour.isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean isVirtualTourContentLoaded() {
        try {
            WebElement tour = driver.findElement(VIRTUAL_TOUR);
            String html = tour.getAttribute("innerHTML");
            // After clicking, JS replaces content with an iframe or heading
            return html != null && (html.contains("iframe") || html.contains("Virtual Tour"));
        } catch (Exception e) { return false; }
    }

    public boolean isOwnerNameDisplayed() {
        return getDetailContentText().contains("Rajesh Kumar");
    }

    public boolean isContactOwnerButtonVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(BTN_CONTACT)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public boolean areNearbySchoolsListed() {
        return getDetailContentText().contains("Delhi Public School");
    }

    public boolean areNearbyHospitalsListed() {
        return getDetailContentText().contains("Manipal Hospital");
    }

    public boolean isPoliceStationDisplayed() {
        return getDetailContentText().contains("Police Station");
    }

    public boolean isFireStationDisplayed() {
        return getDetailContentText().contains("Fire Station");
    }

    public boolean areNearbySupermarketsListed() {
        return getDetailContentText().contains("More Supermarket");
    }

    public boolean areNearbyMetroStationsListed() {
        return getDetailContentText().contains("Koramangala Metro");
    }

    public boolean isLocalityInsectionDisplayed() {
        return getDetailContentText().contains("Locality Insights");
    }

    public boolean areLocalityScoresVisible() {
        try {
            List<WebElement> bars = driver.findElement(DETAIL_CONTENT)
                    .findElements(By.cssSelector(".score-bar"));
            return !bars.isEmpty();
        } catch (Exception e) { return false; }
    }

    public boolean isSafetyAndSecurityDisplayed() {
        return getDetailContentText().contains("Safety & Security");
    }

    public boolean isCctvAndGatedInfoVisible() {
        String text = getDetailContentText();
        return text.contains("CCTV") && text.contains("Gated Community");
    }

    // ── Internal helper ───────────────────────────────────────────────────────

    private String getDetailContentText() {
        try {
            return driver.findElement(DETAIL_CONTENT).getText();
        } catch (Exception e) { return ""; }
    }
}
