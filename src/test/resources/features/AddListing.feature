@US13
Feature: Add Property Listing
  As a property owner or agent
  I want to post a new property listing
  So that potential buyers or tenants can discover my property

  Background:
    Given I am on the add listing page

  # TC61 ──────────────────────────────────────────────────────────────────────
  @TC61 @US13 @Functional @High
  Scenario Outline: Create listing with valid details
    When I fill in the listing title "<title>"
    And I select the listing purpose "<purpose>"
    And I select the listing type "<type>"
    And I enter the listing price "<price>"
    And I select the listing BHK "<bhk>"
    And I select the listing city "<city>"
    And I enter the listing locality "<locality>"
    And I enter the listing pincode "<pincode>"
    And I enter the listing area "<area>"
    And I enter the owner name "<ownerName>"
    And I accept the listing terms
    And I submit the listing form
    Then the listing should be created successfully

    Examples:
      | title              | purpose | type      | price   | bhk   | city      | locality       | pincode | area | ownerName   |
      | Cozy Apartment     | Rent    | Apartment | 15000   | 2 BHK | Bangalore | Koramangala    | 560034  | 900  | Ravi Kumar  |
      | Spacious Villa     | Buy / Sale | Villa  | 5000000 | 3 BHK | Mumbai    | Andheri West   | 400058  | 2200 | Priya Mehta |

  # TC62 ──────────────────────────────────────────────────────────────────────
  @TC62 @US13 @Validation @High
  Scenario: Create listing with missing mandatory fields
    When I submit the listing form without filling any fields
    Then listing validation messages should be displayed

  # TC63 ──────────────────────────────────────────────────────────────────────
  @TC63 @US13 @Functional @Medium
  Scenario: Upload property photos
    When I fill in the listing title "Test Property"
    And I upload a property photo
    Then the photo preview should be displayed

  # TC64 ──────────────────────────────────────────────────────────────────────
  @TC64 @US13 @Functional @High
  Scenario: Edit existing listing
    When I fill in the listing title "Original Title"
    And I select the listing purpose "Rent"
    And I select the listing type "Apartment"
    And I enter the listing price "12000"
    And I select the listing BHK "1 BHK"
    And I select the listing city "Chennai"
    And I enter the listing locality "T Nagar"
    And I enter the listing pincode "600017"
    And I enter the listing area "600"
    And I enter the owner name "Test Owner"
    And I accept the listing terms
    And I submit the listing form
    And the listing should be created successfully
    And I navigate back to the add listing page
    And I fill in the listing title "Updated Title"
    And I enter the owner name "Updated Owner"
    And I accept the listing terms
    And I submit the listing form
    Then the listing should be created successfully

  # TC65 ──────────────────────────────────────────────────────────────────────
  @TC65 @US13 @Functional @High
  Scenario: Remove existing listing
    When I fill in the listing title "Listing To Remove"
    And I select the listing purpose "Lease"
    And I select the listing type "Studio"
    And I enter the listing price "8000"
    And I select the listing BHK "1 BHK"
    And I select the listing city "Pune"
    And I enter the listing locality "Hinjewadi"
    And I enter the listing pincode "411057"
    And I enter the listing area "400"
    And I enter the owner name "Owner To Remove"
    And I accept the listing terms
    And I submit the listing form
    Then the listing should be created successfully
    And the success modal should be visible
