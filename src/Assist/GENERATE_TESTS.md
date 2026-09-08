# PropFind – Test Generation Prompt

You are helping automate QA testing for the **PropFind** website — a property listing platform built with plain HTML, CSS, and JavaScript.

## Project context

- All HTML files are in the workspace root (e.g. `login.html`, `search-results.html`, `property-detail.html`, etc.)
- All interactive elements already have stable `id` attributes added specifically for Selenium testing
- Dynamic elements generated inside JS template literals also follow stable ID patterns (e.g. `fav-btn-${p.id}`, `compare-chk-${p.id}`, `btn-remove-${p.id}`)
- **`Assist/ELEMENT_ID_REGISTRY.md`** — pre-extracted table of every element `id` per page; use this in Step 3 to avoid re-reading HTML files for every generation run
- **`Assist/TEAM_ASSIGNMENTS.md`** — maps each team member's name to their assigned TC IDs; used in the Start step below
- The test case definitions are in `Assist/Rearranged_testcases.xlsx`
- An example of the expected output already exists — **read all three reference files before generating anything new**:
  - `Assist/features/SearchResults.feature` — Gherkin reference (style, tags, Background, Scenario Outline)
  - `src/test/java/pages/SearchResultsPage.java` — Page Object reference (locator constants, wait strategy, method naming)
  - `src/test/java/tests/SearchResultsTest.java` — TestNG test class reference (assertion messages, DataProvider, openPage() helper)
  - `src/test/java/base/BaseTest.java` — shared base class (**DO NOT rewrite or duplicate this**)

## Established conventions

- **Base URL:** resolved automatically from `src/test/resources/TargetWebsite/` — no manual path needed; override via `<parameter name="baseUrl" .../>` in `src/test/test.xml` if required
- **Package structure:** `pages.*`, `tests.*`, `base.BaseTest`
- **Language:** Java 17+, Selenium 4, TestNG 7
- **Locator priority:** `By.id` → `By.cssSelector` → `By.name` → `By.xpath` (last resort only)
- **Page Object rules:** no assertions inside Page Objects; one method per user action; query methods return `String`, `int`, `boolean`, or `List<WebElement>`; always wrap interactions in `WebDriverWait`
- **Test class rules:** extends `BaseTest`; one `@Test` method per TC named `tcXX_camelCaseDescription()`; `@DataProvider` for multi-data scenarios; all element access through the Page Object only — no raw `driver.findElement` calls in test methods
- **`@Test` annotation format:** `testName="TCXX"`, `groups={"USXX", "Classification", "Priority"}`
- **Existing page coverage:** if the target page already has a Page Object (e.g. `LoginPage.java`, `MapViewPage.java`, `SearchResultsPage.java`), read the existing file first and **add only the missing locators and methods** — do not regenerate the whole class

---

## Start

Ask the user:

> **What is your name?**
> Type your first name exactly as it appears in the team (e.g. `Raman`, `Prabavathi`, `Nithyasri`, `Sherin`, `Nived`, `Rahul`, `Rohit`).

Once the user provides their name, do the following **before** Step 1:

### Name Lookup
1. Open `Assist/TEAM_ASSIGNMENTS.md` and find the row where the **Person** column matches the name provided (case-insensitive).
2. Read the **Test Cases** column for that row — expand any ranges (e.g. `TC01–TC05` → TC01, TC02, TC03, TC04, TC05).
3. Confirm the discovered TC list with the user:
   > *"I found your assignments: **[TC IDs]** covering **[pages]**. Shall I proceed?"*
4. If the name is not found, respond: *"I couldn't find '[name]' in TEAM_ASSIGNMENTS.md. Please check your name or ask your instructor."* — do not proceed further.
5. Once confirmed, use those TC IDs for all steps below.

Once the TC IDs are confirmed, do the following steps **in order**:

### Step 1 — Read the test case definitions
Read `Assist/Rearranged_testcases.xlsx` and extract the rows matching the provided TC IDs.
Note the Test Scenario, Test Steps, Expected Result, Classification, Priority, and User Story for each.

### Step 2 — Identify the page(s)
From the TC IDs and their User Stories, determine which HTML file(s) they belong to:

| User Story | HTML File | Existing Page Object? |
|---|---|---|
| US01 | `login.html` | – |
| US02, US03 | `search-results.html` | `SearchResultsPage.java` ✓ |
| US04 | `map-view.html` | – |
| US05, US06, US09, US10, US14 | `property-detail.html` | – |
| US07, US11 | `dashboard.html` | – |
| US08 | `compare.html` | – |
| US12 | `budget-planner.html` | – |
| US13 | `add-listing.html` | – |
| US15 | `moving-assistant.html` | – |

### Step 3 — Collect element IDs and read any existing Page Object(s)
- Open `Assist/ELEMENT_ID_REGISTRY.md` and copy out the ID table for the page(s) you identified in Step 2.
- Only fall back to reading the raw HTML file if an ID you need is missing from the registry (and add it to the registry when you do).
- If the **"Existing Page Object?"** column shows `✓`, attempt to read the file from `src/test/java/pages/`.
  - **If the file exists** → add only the missing locators and methods; do not regenerate the whole class.
  - **If the file does not exist** → generate a complete new Page Object from scratch; do not report an error or pause.
- If the column shows `–`, generate a complete new Page Object from scratch.
- The IDs from the registry are the **only** locators to use in the generated code.
- Cross-check: every `By.id("...")` you write must appear in the registry table before you emit any code.

### Step 4 — Generate File 1: Gherkin feature file
- Output location: `src/Assist/features/<PageName>.feature`
- If a `.feature` file already exists for this page, **append** the new scenarios to it instead of recreating it
- One `Feature:` block per file
- `Background:` navigates to the correct page
- One `Scenario` or `Scenario Outline` (with `Examples`) per TC
- Tags per scenario: `@TCXX @USXX @Classification @Priority`
- Steps use visible UI label text, not internal IDs

### Step 5 — Generate File 2: Page Object class
- Output location: `src/test/java/pages/<PageName>Page.java`, package `pages`
- If the file already exists, **add only new locators and methods** needed for the requested TCs
- `private static final By` constant per interactive element using `By.id("...")`
- Constructor: `public <PageName>Page(WebDriver driver)`
- `navigateTo(String baseUrl)` method
- One public method per user action; query methods for assertions
- All wait calls use `WebDriverWait` with a 10-second timeout — never `Thread.sleep`
- **Auth-protected pages** — before generating, `grep` the HTML file for `Auth.requireLogin()`. If found, the page redirects to `login.html` when no session exists, so opening it directly in Selenium will immediately redirect away and every locator wait will time out. Add a `loginAs(String baseUrl, String username, String password)` helper to the Page Object that:
  1. Loads any page on the same origin first (e.g. `login.html`) so the storage APIs are available.
  2. Uses `JavascriptExecutor` to seed `localStorage` (`prop_users`) **and** `sessionStorage` (`prop_current_user`) with a well-formed user object — do **not** rely on filling the login form, because `localStorage` is blank in every fresh browser session and `Auth.login()` will return `{ ok: false }`.
  3. Navigates to the protected page and waits for a reliable landmark element to confirm the page rendered.
  - The injected user object shape (from `Auth.register()`): `{id, name, email, username, password, favorites:[], recentlyViewed:[], appointments:[]}`.
  - Call `loginAs()` from the test class `@BeforeMethod` instead of `navigateTo()`.

### Step 6 — Generate File 3: TestNG test class
- Output location: `src/test/java/tests/<PageName>Test.java`, package `tests`
- If the file already exists, **add only the new `@Test` methods** for the requested TCs
- `extends BaseTest`
- One `@Test` method per TC
- `@DataProvider` where a scenario runs with multiple inputs
- All assertions use `TestNG Assert` with a descriptive failure message
- No `driver.findElement` in test methods — all access via the Page Object
- **Auth-protected pages** — if the target page has `Auth.requireLogin()`, the test class `@BeforeMethod` must call `loginAs()` (not `navigateTo()`) so that every `@Test` method starts with an authenticated session already in place.
- **`Select` option text** — always pass the **full visible label** of the `<option>` to `selectByVisibleText()`, not the `value` attribute. Before writing any `selectByVisibleText("...")` call, open the HTML and copy the exact text node of the target `<option>` (e.g. the BHK dropdown renders `<option value="2">2 BHK</option>`, so the correct call is `selectByVisibleText("2 BHK")`, not `selectByVisibleText("2")`). Use `selectByValue()` only when you intentionally want to match the `value` attribute.

### Step 7 — Self-review before output
Before presenting any code, verify:
- [ ] Every `By.id("...")` value exists in the HTML file you read in Step 3
- [ ] Every `@Test` method name follows `tcXX_camelCaseDescription()`
- [ ] No `Thread.sleep` is used — only `WebDriverWait`
- [ ] No assertions inside Page Object methods
- [ ] No `driver.findElement` calls inside test methods
- [ ] `BaseTest` is not modified or re-emitted
- [ ] If the page contains `Auth.requireLogin()` → `loginAs()` helper exists in the Page Object and `@BeforeMethod` calls it instead of `navigateTo()`
- [ ] Every `selectByVisibleText("...")` argument matches the exact visible text of the `<option>` element as it appears in the HTML, not the `value` attribute

### Step 8 — Confirm output
List the files generated (new files vs. files with additions), and ask if any changes are needed.
