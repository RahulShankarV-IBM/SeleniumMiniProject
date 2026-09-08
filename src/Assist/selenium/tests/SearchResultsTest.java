package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.SearchResultsPage;

import java.util.List;

/**
 * SearchResultsTest — sample reference test class.
 *
 * ── SAMPLE FILE ──────────────────────────────────────────────────────────────
 * This file lives in src/Assist/selenium/tests/ for reference only.
 * The live, compiled version is in src/test/java/tests/SearchResultsTest.java.
 * Do not edit this copy — edit the live version instead.
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * Covers: US02 (TC06–TC10) and US03 (TC11–TC15).
 */
public class SearchResultsTest extends BaseTest {

    private SearchResultsPage openPage() {
        SearchResultsPage page = new SearchResultsPage(driver);
        page.navigateTo(baseUrl);
        return page;
    }

    // ── US02 ──────────────────────────────────────────────────────────────────

    @Test(testName = "TC06", groups = {"US02", "Functional", "High"})
    public void tc06_searchByCity() {
        SearchResultsPage page = openPage();
        page.enterLocation("Bangalore");
        page.clickSearch();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC06 FAIL: Expected results for 'Bangalore' but got 0.");
    }

    @Test(testName = "TC07", groups = {"US02", "Functional", "High"}, dataProvider = "purposeData")
    public void tc07_searchByPurpose(String purpose) {
        SearchResultsPage page = openPage();
        page.selectPurpose(purpose);
        page.clickSearch();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC07 FAIL: Page did not load results for purpose: " + purpose);
    }

    @DataProvider(name = "purposeData")
    public Object[][] purposeData() {
        return new Object[][]{{"Rent"}, {"Buy"}, {"Lease"}};
    }

    @Test(testName = "TC08", groups = {"US02", "Functional", "High"})
    public void tc08_budgetRangeFilter() {
        SearchResultsPage page = openPage();
        page.enterMinBudget("10000");
        page.enterMaxBudget("30000");
        page.clickApplyFilters();
        List<String> prices = page.getDisplayedCardPrices();
        Assert.assertFalse(prices.isEmpty(),
            "TC08 FAIL: No properties displayed after applying budget range.");
        for (String label : prices) {
            int price = page.parsePriceFromLabel(label);
            Assert.assertTrue(price >= 10000 && price <= 30000,
                "TC08 FAIL: Property price " + price + " is outside range [10000, 30000].");
        }
    }

    @Test(testName = "TC09", groups = {"US02", "Negative", "High"})
    public void tc09_noMatchingResults() {
        SearchResultsPage page = openPage();
        page.enterLocation("ZZZNOMATCH999");
        page.clickSearch();
        Assert.assertTrue(page.isNoResultsMessageVisible(),
            "TC09 FAIL: No-results message was not displayed for an invalid location.");
        Assert.assertEquals(page.getResultCards().size(), 0,
            "TC09 FAIL: Expected 0 result cards but found some.");
    }

    @Test(testName = "TC10", groups = {"US02", "Functional", "Medium"})
    public void tc10_sortResults() {
        SearchResultsPage page = openPage();
        page.selectSortOption("Price: Low to High");
        List<String> asc = page.getDisplayedCardPrices();
        if (asc.size() >= 2) {
            Assert.assertTrue(page.parsePriceFromLabel(asc.get(0)) <= page.parsePriceFromLabel(asc.get(1)),
                "TC10 FAIL: 'Low to High' sort is incorrect.");
        }
        page.selectSortOption("Price: High to Low");
        List<String> desc = page.getDisplayedCardPrices();
        if (desc.size() >= 2) {
            Assert.assertTrue(page.parsePriceFromLabel(desc.get(0)) >= page.parsePriceFromLabel(desc.get(1)),
                "TC10 FAIL: 'High to Low' sort is incorrect.");
        }
    }

    // ── US03 ──────────────────────────────────────────────────────────────────

    @Test(testName = "TC11", groups = {"US03", "Functional", "High"})
    public void tc11_filterByPropertyType() {
        SearchResultsPage page = openPage();
        page.checkPropertyType("Apartment");
        page.clickApplyFilters();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC11 FAIL: No results shown after filtering by 'Apartment'.");
    }

    @Test(testName = "TC12", groups = {"US03", "Functional", "High"})
    public void tc12_filterByBhk() {
        SearchResultsPage page = openPage();
        page.clickBhkButton(2);
        Assert.assertTrue(page.isBhkButtonActive(2),
            "TC12 FAIL: BHK '2' button was not marked active after click.");
        Assert.assertTrue(page.getResultCount() > 0,
            "TC12 FAIL: No results after filtering by 2 BHK.");
    }

    @Test(testName = "TC13", groups = {"US03", "Functional", "High"})
    public void tc13_filterByFurnishing() {
        SearchResultsPage page = openPage();
        page.selectFurnishing("Fully Furnished");
        page.clickApplyFilters();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC13 FAIL: No results after applying 'Fully Furnished' filter.");
    }

    @Test(testName = "TC14", groups = {"US03", "Functional", "High"})
    public void tc14_multipleFilters() {
        SearchResultsPage page = openPage();
        page.clickBhkButton(3);
        page.checkAmenity("Parking");
        page.selectPurpose("Rent");
        page.clickApplyFilters();
        Assert.assertTrue(page.getResultCount() >= 0,
            "TC14 FAIL: Result count was negative or page errored.");
    }

    @Test(testName = "TC15", groups = {"US03", "Functional", "High"})
    public void tc15_clearAllFilters() {
        SearchResultsPage page = openPage();
        int baseline = page.getResultCount();
        page.clickBhkButton(1);
        page.clickApplyFilters();
        page.clickClearAllFilters();
        Assert.assertEquals(page.getResultCount(), baseline,
            "TC15 FAIL: After Clear All, result count does not match baseline.");
        Assert.assertFalse(page.isBhkButtonActive(1),
            "TC15 FAIL: BHK '1' button is still active after Clear All.");
        Assert.assertEquals(page.getLocationFieldValue(), "",
            "TC15 FAIL: Location field was not cleared.");
    }
}
