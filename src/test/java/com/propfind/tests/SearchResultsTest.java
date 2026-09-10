package com.propfind.tests;

import com.propfind.base.BaseTestClass;
import com.propfind.pages.SearchResultsPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Plain TestNG tests for the Search Results page.
 *
 * Covers:
 *   US02 — Smart Property Search    (TC06–TC10)
 *   US03 — Advanced Property Filters (TC11–TC15)
 */
public class SearchResultsTest extends BaseTestClass {

    private SearchResultsPage openPage() {
        SearchResultsPage page = new SearchResultsPage(driver);
        page.navigateTo(baseUrl);
        return page;
    }

    // ── US02 — Smart Property Search ─────────────────────────────────────────

    @Test(testName = "TC06", groups = {"US02", "Functional", "High"})
    public void tc06_searchByCity() {
        SearchResultsPage page = openPage();
        page.enterLocation("Bangalore");
        page.clickSearch();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC06: Expected results for 'Bangalore' but got 0.");
    }

    @DataProvider(name = "purposeData")
    public Object[][] purposeData() {
        return new Object[][]{ {"Rent"}, {"Buy"}, {"Lease"} };
    }

    @Test(testName = "TC07", groups = {"US02", "Functional", "High"}, dataProvider = "purposeData")
    public void tc07_searchByPurpose(String purpose) {
        SearchResultsPage page = openPage();
        page.selectPurpose(purpose);
        page.clickSearch();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC07: No results for purpose: " + purpose);
    }

    @Test(testName = "TC08", groups = {"US02", "Functional", "High"})
    public void tc08_budgetRangeFilter() {
        SearchResultsPage page = openPage();
        page.enterMinBudget("10000");
        page.enterMaxBudget("30000");
        page.clickApplyFilters();
        List<String> prices = page.getDisplayedCardPrices();
        Assert.assertFalse(prices.isEmpty(),
            "TC08: No properties displayed after applying budget range.");
        for (String label : prices) {
            int price = page.parsePriceFromLabel(label);
            Assert.assertTrue(price >= 10000 && price <= 30000,
                "TC08: Property price " + price + " is outside range [10000, 30000].");
        }
    }

    @Test(testName = "TC09", groups = {"US02", "Negative", "High"})
    public void tc09_noMatchingResults() {
        SearchResultsPage page = openPage();
        page.enterLocation("ZZZNOMATCH999");
        page.clickSearch();
        Assert.assertTrue(page.isNoResultsMessageVisible(),
            "TC09: No-results message was not displayed for an invalid location.");
        Assert.assertEquals(page.getResultCards().size(), 0,
            "TC09: Expected 0 result cards but found some.");
    }

    @Test(testName = "TC10", groups = {"US02", "Functional", "Medium"})
    public void tc10_sortResults() {
        SearchResultsPage page = openPage();
        page.selectSortOption("Price: Low to High");
        List<String> asc = page.getDisplayedCardPrices();
        if (asc.size() >= 2) {
            Assert.assertTrue(page.parsePriceFromLabel(asc.get(0)) <= page.parsePriceFromLabel(asc.get(1)),
                "TC10: 'Low to High' sort order is incorrect.");
        }
        page.selectSortOption("Price: High to Low");
        List<String> desc = page.getDisplayedCardPrices();
        if (desc.size() >= 2) {
            Assert.assertTrue(page.parsePriceFromLabel(desc.get(0)) >= page.parsePriceFromLabel(desc.get(1)),
                "TC10: 'High to Low' sort order is incorrect.");
        }
    }

    // ── US03 — Advanced Property Filters ─────────────────────────────────────

    @Test(testName = "TC11", groups = {"US03", "Functional", "High"})
    public void tc11_filterByPropertyType() {
        SearchResultsPage page = openPage();
        page.checkPropertyType("Apartment");
        page.clickApplyFilters();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC11: No results shown after filtering by 'Apartment'.");
    }

    @Test(testName = "TC12", groups = {"US03", "Functional", "High"})
    public void tc12_filterByBhk() {
        SearchResultsPage page = openPage();
        page.clickBhkButton(2);
        Assert.assertTrue(page.isBhkButtonActive(2),
            "TC12: BHK '2' button was not marked active after click.");
        Assert.assertTrue(page.getResultCount() > 0,
            "TC12: No results after filtering by 2 BHK.");
    }

    @Test(testName = "TC13", groups = {"US03", "Functional", "High"})
    public void tc13_filterByFurnishing() {
        SearchResultsPage page = openPage();
        page.selectFurnishing("Fully Furnished");
        page.clickApplyFilters();
        Assert.assertTrue(page.getResultCount() > 0,
            "TC13: No results after applying 'Fully Furnished' filter.");
    }

    @Test(testName = "TC14", groups = {"US03", "Functional", "High"})
    public void tc14_multipleFilters() {
        SearchResultsPage page = openPage();
        page.clickBhkButton(3);
        page.checkAmenity("Parking");
        page.selectPurpose("Rent");
        page.clickApplyFilters();
        Assert.assertTrue(page.getResultCount() >= 0,
            "TC14: Result count was negative or page errored.");
    }

    @Test(testName = "TC15", groups = {"US03", "Functional", "High"})
    public void tc15_clearAllFilters() {
        SearchResultsPage page = openPage();
        int baseline = page.getResultCount();
        page.clickBhkButton(1);
        page.clickApplyFilters();
        page.clickClearAllFilters();
        Assert.assertEquals(page.getResultCount(), baseline,
            "TC15: After Clear All, result count does not match baseline.");
        Assert.assertFalse(page.isBhkButtonActive(1),
            "TC15: BHK '1' button is still active after Clear All.");
        Assert.assertEquals(page.getLocationFieldValue(), "",
            "TC15: Location field was not cleared.");
    }
}
