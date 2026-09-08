# ============================================================
#  Feature: Search Results Page
#  Page:    search-results.html
#  Covers:  US02 – Smart Property Search (TC06–TC10)
#           US03 – Advanced Property Filters (TC11–TC15)
# ============================================================
Feature: Search Results – Search and Filter Properties

  Background:
    Given the user is on the Search Results page

  # ── US02 – Smart Property Search ───────────────────────────

  @TC06 @US02 @Functional @High
  Scenario: TC06 – Search using city or locality
    When the user types "Bangalore" in the location search field
    And the user clicks the Search button
    Then properties matching "Bangalore" are displayed in the results grid

  @TC07 @US02 @Functional @High
  Scenario Outline: TC07 – Search using Buy / Rent / Lease purpose
    When the user selects "<purpose>" from the purpose dropdown
    And the user clicks the Search button
    Then only properties with purpose "<purpose>" are shown in the results

    Examples:
      | purpose |
      | Rent    |
      | Buy     |
      | Lease   |

  @TC08 @US02 @Functional @High
  Scenario: TC08 – Apply a budget range filter
    When the user enters "10000" in the minimum budget field
    And the user enters "30000" in the maximum budget field
    And the user clicks the Apply Filters button
    Then all displayed properties have a price between 10000 and 30000

  @TC09 @US02 @Negative @High
  Scenario: TC09 – Search with no matching results
    When the user types "ZZZNOMATCH999" in the location search field
    And the user clicks the Search button
    Then the no-results message is displayed
    And the results grid is empty

  @TC10 @US02 @Functional @Medium
  Scenario Outline: TC10 – Sort search results
    When the user selects "<sortOption>" from the sort dropdown
    Then the results are displayed in the expected "<sortOrder>" order

    Examples:
      | sortOption        | sortOrder          |
      | Price: Low to High | ascending by price |
      | Price: High to Low | descending by price |
      | Newest First       | newest available first |
      | Top Rated          | highest rated first |

  # ── US03 – Advanced Property Filters ───────────────────────

  @TC11 @US03 @Functional @High
  Scenario: TC11 – Filter by property type
    When the user checks the "Apartment" property type checkbox
    And the user clicks the Apply Filters button
    Then all displayed properties have the type "Apartment"

  @TC12 @US03 @Functional @High
  Scenario: TC12 – Filter by BHK
    When the user clicks the "2 BHK" BHK filter button
    Then all displayed properties have 2 BHK

  @TC13 @US03 @Functional @High
  Scenario: TC13 – Filter by furnishing status
    When the user selects the "Fully Furnished" furnishing radio button
    And the user clicks the Apply Filters button
    Then all displayed properties are "Fully Furnished"

  @TC14 @US03 @Functional @High
  Scenario: TC14 – Apply multiple filters simultaneously
    When the user clicks the "3 BHK" BHK filter button
    And the user checks the "Parking" amenity checkbox
    And the user selects "Rent" from the purpose dropdown
    And the user clicks the Apply Filters button
    Then all displayed properties satisfy all three criteria

  @TC15 @US03 @Functional @High
  Scenario: TC15 – Clear all filters
    Given the user has applied at least one filter
    When the user clicks the Clear All button
    Then all filters are reset to their default state
    And the full unfiltered property list is displayed
