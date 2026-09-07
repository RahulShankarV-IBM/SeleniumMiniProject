# PropFind – Test Generation Guide

You are helping automate QA testing for the **PropFind** website — a property listing platform built with plain HTML, CSS, and JavaScript.

## Project context

- All HTML files are in `src/test/resources/TargetWebsite - Copy/` (e.g. `login.html`, `search-results.html`, `property-detail.html`, etc.)
- All interactive elements already have stable `id` attributes added specifically for Selenium testing
- Dynamic elements generated inside JS template literals also follow stable ID patterns (e.g. `fav-btn-${p.id}`, `compare-chk-${p.id}`, `btn-remove-${p.id}`)
- **`AssistingFiles/ELEMENT_ID_REGISTRY.md`** — pre-extracted table of every element `id` per page; use this in Step 3 to avoid re-reading HTML files for every generation run
- **`AssistingFiles/TEAM_ASSIGNMENTS.md`** — maps each team member's name to their assigned TC IDs; used in the Start step below
- The test case definitions are in `AssistingFiles/Rearranged_testcases.xlsx`
- The reference implementation already exists — **read these files before generating anything new**:
  - `src/test/resources/features/Login.feature` — Gherkin reference (tags, Background, Scenario Outline, Examples)
  - `src/test/java/LoginPage.java` — Page Object reference (locator constants, wait strategy, seedUser, query methods)
  - `src/test/java/LoginSteps.java` — step definitions reference (@Given/@When/@And/@Then, assertion style)
  - `src/test/java/LoginTestContext.java` — PicoContainer context reference (WebDriver + Page Object per scenario)
  - `src/test/java/LoginHooks.java` — hooks reference (@Before/@After, screenshot on failure, driver quit)
  - `src/test/java/LoginTest.java` — runner reference (@CucumberOptions, AbstractTestNGCucumberTests)
  - `src/test/java/BaseTestClass.java` — shared base class (**DO NOT rewrite or duplicate this**)

---

## Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Browser automation | Selenium 4.27 |
| Test runner | TestNG 7.10 |
| BDD | Cucumber 7.34 (cucumber-java, cucumber-testng, cucumber-picocontainer) |
| Reporting | Extent Reports 5.1 + Cucumber HTML/JSON built-in |

---

## Project layout

```
src/test/
  java/                        ← all Java files, default package (no subfolders)
    BaseTestClass.java         ← WebDriver setup/teardown, pageUrl() helper
    ExtentReportListener.java  ← TestNG ITestListener; registered in test.xml
    <Page>Page.java            ← Page Object per page
    <Page>Steps.java           ← Cucumber step definitions per page
    <Page>TestContext.java     ← PicoContainer context per page
    <Page>Hooks.java           ← Cucumber @Before/@After per page
    <Page>Test.java            ← @CucumberOptions runner per page
  resources/
    features/
      <Page>.feature           ← Gherkin scenarios per page
    TargetWebsite - Copy/      ← the live HTML site under test
test.xml                       ← TestNG suite — one <test> block per runner class
```

---

## Established conventions

- **Package:** all Java files have **no package declaration** (default package)
- **Base URL:** resolved automatically from `src/test/resources/TargetWebsite - Copy/` inside `<Page>TestContext`; the `file://` URI is built with `new File(...).toURI().toString()`
- **Locator priority:** `By.id` → `By.cssSelector` → `By.name` → `By.xpath` (last resort only)
- **Page Object rules:**
  - `private static final By` constant per element, using `By.id("...")`
  - Constructor: `public <Page>Page(WebDriver driver)` — stores driver and creates a `WebDriverWait` with a 10-second timeout
  - `navigateTo(String baseUrl)` — loads the page and waits for a reliable landmark element
  - One public method per user action
  - Query methods return `String`, `boolean`, or `int` — never `WebElement`
  - **No assertions** inside Page Objects
  - **Never `Thread.sleep`** — always `WebDriverWait`
- **Step definition rules:**
  - Constructor receives `<Page>TestContext` via PicoContainer injection
  - All Selenium calls go through the Page Object — no `driver` access in steps
  - All assertions use `org.testng.Assert` with a descriptive failure message
- **Context class rules (`<Page>TestContext`):**
  - No-arg constructor — creates `ChromeDriver` with `--start-maximized` and `--allow-file-access-from-files`
  - Zero implicit wait; 30-second page load timeout
  - Exposes `getDriver()`, `get<Page>Page()`, and `static getBaseUrl()`
  - One instance per scenario (PicoContainer lifecycle)
- **Hooks class rules (`<Page>Hooks`):**
  - Constructor receives `<Page>TestContext` via PicoContainer injection
  - `@After` — on failure, captures screenshot as `byte[]` and attaches via `scenario.attach()`; always calls `driver.quit()`
- **Runner class rules (`<Page>Test`):**
  - Extends `AbstractTestNGCucumberTests`
  - `@CucumberOptions`: `features` = path to the `.feature` file, `glue` = `""` (default package), `plugin` includes `"pretty"`, `"html:target/cucumber-reports/<page>/index.html"`, `"json:target/cucumber-reports/<page>/cucumber.json"`
  - `monochrome = true`
- **`Select` option text** — always pass the **full visible label** to `selectByVisibleText()`, not the `value` attribute. Open the HTML and copy the exact `<option>` text node before writing the call.

---

## Auth-protected pages

Before generating for any page, `grep` the HTML file for `Auth.requireLogin()`.

**If found:** the page redirects to `login.html` when no session exists. Add a `loginAs(String baseUrl, String username, String password)` method to the Page Object that:
1. Navigates to `login.html` first (so `localStorage`/`sessionStorage` APIs are available on the `file://` origin)
2. Uses `JavascriptExecutor` to seed both `localStorage` (`prop_users`) **and** `sessionStorage` (`prop_current_user`) with a well-formed user object — do **not** fill the login form, because `localStorage` is blank in every fresh browser session and `Auth.login()` will return `{ ok: false }`
3. Navigates to the protected page and waits for a reliable landmark element

User object shape (from `Auth.register()` in `properties.js`):
```json
{ "id": 99999, "name": "Test User", "email": "", "username": "<username>",
  "password": "<password>", "favorites": [], "recentlyViewed": [], "appointments": [] }
```

Call `loginAs()` from the `@Before` hook in `<Page>Hooks` instead of navigating in the Background step.

**If not found:** `navigateTo()` is sufficient; no session seeding needed. (`login.html`, `search-results.html`, `map-view.html`, `add-listing.html`, `budget-planner.html`, and `moving-assistant.html` do **not** have `Auth.requireLogin()`.)

---

## Page → file mapping

| User Story | HTML File | Existing files? |
|---|---|---|
| US01 | `login.html` | `LoginPage.java`, `LoginSteps.java`, `LoginTestContext.java`, `LoginHooks.java`, `LoginTest.java`, `Login.feature` ✓ |
| US02, US03 | `search-results.html` | – |
| US04 | `map-view.html` | – |
| US05, US06 | `property-detail.html` | `PropertyDetailPage.java`, `PropertyDetailSteps.java`, `PropertyDetailTestContext.java`, `PropertyDetailHooks.java`, `PropertyDetailTest.java`, `PropertyDetail.feature` ✓ |
| US09, US10, US14 | `property-detail.html` | – (Rohit — adds to same files) |
| US07, US11 | `dashboard.html` | – |
| US08 | `compare.html` | – |
| US12 | `budget-planner.html` | – |
| US13 | `add-listing.html` | – |
| US15 | `moving-assistant.html` | – |

---

## Start

Ask the user:

> **What is your name?**
> Type your first name exactly as it appears in the team (e.g. `Raman`, `Prabavathi`, `Nithyasri`, `Sherin`, `Nived`, `Rahul`, `Rohit`).

Once the user provides their name, do the following **before** Step 1:

### Name Lookup
1. Open `AssistingFiles/TEAM_ASSIGNMENTS.md` and find the row where the **Person** column matches the name provided (case-insensitive).
2. Read the **Test Cases** column for that row — expand any ranges (e.g. `TC01–TC05` → TC01, TC02, TC03, TC04, TC05).
3. Confirm the discovered TC list with the user:
   > *"I found your assignments: **[TC IDs]** covering **[pages]**. Shall I proceed?"*
4. If the name is not found, respond: *"I couldn't find '[name]' in TEAM_ASSIGNMENTS.md. Please check your name or ask your instructor."* — do not proceed further.
5. Once confirmed, use those TC IDs for all steps below.

---

## Step 1 — Read the test case definitions

Read `AssistingFiles/Rearranged_testcases.xlsx` and extract the rows matching the confirmed TC IDs.
Record the **Test Scenario**, **Test Steps**, **Expected Result**, **Classification**, **Priority**, and **User Story** for each row.

---

## Step 2 — Identify the page(s)

Use the User Story column from Step 1 and the Page → file mapping table above to determine which HTML file(s) the TCs belong to.

---

## Step 3 — Collect element IDs

- Open `AssistingFiles/ELEMENT_ID_REGISTRY.md` and extract the ID table for the page(s) identified in Step 2.
- Only fall back to reading the raw HTML file if an ID you need is missing from the registry. If you do read the HTML, add the missing ID to the registry before emitting any code.
- If the **"Existing files?"** column shows `✓`, read the existing files from `src/test/java/` and `src/test/resources/features/` first — add only what is missing; do not regenerate complete files.
- The IDs from the registry are the **only** locators to use. Cross-check: every `By.id("...")` you write must appear in the registry before you emit any code.

---

## Step 4 — Generate File 1: Gherkin feature file

- **Output:** `src/test/resources/features/<PageName>.feature`
- If the file already exists, **append** the new scenarios — do not recreate it
- Structure:
  ```gherkin
  @USXX
  Feature: <Feature name>
    As a user ...
    I want to ...
    So that ...

    Background:
      Given I am on the <page name> page

    # TCXX ──────────────────────────────────────────────────────────────
    @TCXX @USXX @Classification @Priority
    Scenario: <Test Scenario from Excel>
      When ...
      Then ...
  ```
- Use `Scenario Outline` + `Examples` for any TC where multiple data rows make sense
- Tags per scenario: `@TCXX @USXX @Classification @Priority` (Classification and Priority as they appear in the Excel, e.g. `@Functional`, `@Negative`, `@Validation`, `@High`, `@Medium`)
- Step text uses visible UI label text — never internal IDs or Java method names

---

## Step 5 — Generate File 2: Page Object

- **Output:** `src/test/java/<PageName>Page.java`, no package declaration
- If the file exists, add only the missing locators and methods
- Template structure (follow `LoginPage.java` exactly):
  ```java
  import org.openqa.selenium.By;
  import org.openqa.selenium.WebDriver;
  import org.openqa.selenium.WebElement;
  import org.openqa.selenium.support.ui.ExpectedConditions;
  import org.openqa.selenium.support.ui.WebDriverWait;
  import java.time.Duration;

  public class <PageName>Page {

      // ── Locators ──────────────────────────────────────────────────────────
      private static final By ELEMENT = By.id("element-id");

      // ── State ─────────────────────────────────────────────────────────────
      private final WebDriver driver;
      private final WebDriverWait wait;

      public <PageName>Page(WebDriver driver) {
          this.driver = driver;
          this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
      }

      // ── Navigation ────────────────────────────────────────────────────────
      public void navigateTo(String baseUrl) {
          driver.get(baseUrl + "<page>.html");
          wait.until(ExpectedConditions.visibilityOfElementLocated(LANDMARK));
      }

      // ── Actions ───────────────────────────────────────────────────────────
      // one public method per user action

      // ── Query methods ─────────────────────────────────────────────────────
      // return String / boolean / int — never WebElement, never assertions
  }
  ```
- Add `seedUser()` / `loginAs()` only if the page is auth-protected (see Auth section above)

---

## Step 6 — Generate File 3: Step definitions

- **Output:** `src/test/java/<PageName>Steps.java`, no package declaration
- If the file exists, add only the missing step methods
- Template structure (follow `LoginSteps.java` exactly):
  ```java
  import io.cucumber.java.en.*;
  import org.testng.Assert;

  public class <PageName>Steps {

      private final <PageName>Page page;

      public <PageName>Steps(<PageName>TestContext ctx) {
          this.page = ctx.get<PageName>Page();
      }

      @Given("...")  public void ...() { page.navigateTo(<PageName>TestContext.getBaseUrl()); }
      @When("...")   public void ...() { page.someAction(); }
      @Then("...")   public void ...() { Assert.assertTrue(page.someQuery(), "message"); }
  }
  ```

---

## Step 7 — Generate File 4: Test context

- **Output:** `src/test/java/<PageName>TestContext.java`, no package declaration
- If the file exists, do not regenerate it
- Template structure (follow `LoginTestContext.java` exactly):
  ```java
  import org.openqa.selenium.WebDriver;
  import org.openqa.selenium.chrome.ChromeDriver;
  import org.openqa.selenium.chrome.ChromeOptions;
  import java.io.File;
  import java.time.Duration;

  public class <PageName>TestContext {

      private final WebDriver driver;
      private final <PageName>Page page;
      private static String baseUrl;

      public <PageName>TestContext() {
          if (baseUrl == null || baseUrl.isBlank()) {
              File siteRoot = new File("src/test/resources/TargetWebsite - Copy");
              baseUrl = siteRoot.toURI().toString();
          }
          ChromeOptions options = new ChromeOptions();
          options.addArguments("--start-maximized", "--allow-file-access-from-files");
          driver = new ChromeDriver(options);
          driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
          driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
          page = new <PageName>Page(driver);
      }

      public WebDriver getDriver()            { return driver; }
      public <PageName>Page get<PageName>Page() { return page; }
      public static String getBaseUrl()       { return baseUrl; }
  }
  ```

---

## Step 8 — Generate File 5: Hooks

- **Output:** `src/test/java/<PageName>Hooks.java`, no package declaration
- If the file exists, do not regenerate it
- Template structure (follow `LoginHooks.java` exactly):
  ```java
  import io.cucumber.java.After;
  import io.cucumber.java.Before;
  import io.cucumber.java.Scenario;

  public class <PageName>Hooks {

      private final <PageName>TestContext ctx;

      public <PageName>Hooks(<PageName>TestContext ctx) { this.ctx = ctx; }

      @Before
      public void beforeScenario(Scenario scenario) { }

      @After
      public void afterScenario(Scenario scenario) {
          if (scenario.isFailed()) {
              try {
                  byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) ctx.getDriver())
                          .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                  scenario.attach(screenshot, "image/png", "Failure screenshot");
              } catch (Exception ignored) { }
          }
          ctx.getDriver().quit();
      }
  }
  ```

---

## Step 9 — Generate File 6: Cucumber–TestNG runner

- **Output:** `src/test/java/<PageName>Test.java`, no package declaration
- If the file exists, do not regenerate it
- Template structure (follow `LoginTest.java` exactly):
  ```java
  import io.cucumber.testng.AbstractTestNGCucumberTests;
  import io.cucumber.testng.CucumberOptions;

  @CucumberOptions(
      features = "src/test/resources/features/<PageName>.feature",
      glue     = "",
      plugin   = {
          "pretty",
          "html:target/cucumber-reports/<pagename>/index.html",
          "json:target/cucumber-reports/<pagename>/cucumber.json"
      },
      monochrome = true
  )
  public class <PageName>Test extends AbstractTestNGCucumberTests {
  }
  ```
- After creating this file, add a `<test>` block to `src/test/test.xml`:
  ```xml
  <test name="<PageName> Tests">
      <classes>
          <class name="<PageName>Test"/>
      </classes>
  </test>
  ```
  The `<listeners>` block and `<parameter name="baseUrl">` are already defined at suite level — do not duplicate them.

---

## Step 10 — Self-review before output

Before presenting any code, verify:
- [ ] Every `By.id("...")` value exists in `AssistingFiles/ELEMENT_ID_REGISTRY.md`
- [ ] No `Thread.sleep` — only `WebDriverWait`
- [ ] No assertions inside Page Object methods
- [ ] No `driver` access inside step definition methods
- [ ] `BaseTestClass.java` and `ExtentReportListener.java` are not modified or re-emitted
- [ ] Auth-protected pages have `loginAs()` in the Page Object and call it from `@Before` in Hooks
- [ ] Every `selectByVisibleText("...")` argument matches the exact visible `<option>` text from the HTML
- [ ] Runner class added to `src/test/test.xml`

---

## Step 11 — Confirm output

List every file generated (new vs. appended), and ask if any changes are needed.
