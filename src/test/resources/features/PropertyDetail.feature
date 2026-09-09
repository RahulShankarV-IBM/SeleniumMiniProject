@US05 @US06
Feature: Property Detail Page
  As a property seeker
  I want to view detailed property information and nearby facilities
  So that I can make an informed decision about the listing

  Background:
    Given I am on the property detail page for property 1

  # TC21 ──────────────────────────────────────────────────────────────
  @TC21 @US05 @Functional @High
  Scenario: Open property details page
    Then the property detail content should be displayed

  # TC22 ──────────────────────────────────────────────────────────────
  @TC22 @US05 @Functional @High
  Scenario: Verify price, location, area and BHK
    Then the property price should be displayed
    And the property location should be displayed
    And the property area should be displayed
    And the property BHK should be displayed

  # TC23 ──────────────────────────────────────────────────────────────
  @TC23 @US05 @Functional @Medium
  Scenario: View property images
    Then the main property image should be visible

  # TC24 ──────────────────────────────────────────────────────────────
  @TC24 @US05 @Functional @Medium
  Scenario: View virtual tour
    When I click the virtual tour section
    Then the virtual tour content should be displayed

  # TC25 ──────────────────────────────────────────────────────────────
  @TC25 @US05 @Functional @Medium
  Scenario: View owner/agent information
    Then the contact owner button should be visible

  # TC26 ──────────────────────────────────────────────────────────────
  @TC26 @US06 @Functional @High
  Scenario: View nearby schools and hospitals
    Then the nearby facilities section should display schools
    And the nearby facilities section should display hospitals

  # TC27 ──────────────────────────────────────────────────────────────
  @TC27 @US06 @Functional @High
  Scenario: View pharmacies, police stations and fire stations
    Then the nearby facilities section should display a police station
    And the nearby facilities section should display a fire station

  # TC28 ──────────────────────────────────────────────────────────────
  @TC28 @US06 @Functional @Medium
  Scenario: View supermarkets, ATMs and transport
    Then the nearby facilities section should display supermarkets
    And the nearby facilities section should display metro stations

  # TC29 ──────────────────────────────────────────────────────────────
  @TC29 @US06 @Functional @Medium
  Scenario: View locality insights
    Then the locality insights section should be displayed

  # TC30 ──────────────────────────────────────────────────────────────
  @TC30 @US06 @Functional @High
  Scenario: Access emergency support
    Then the nearby facilities section should display emergency facilities
