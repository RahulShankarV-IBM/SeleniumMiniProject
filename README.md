# PropFind – Selenium UI Test Automation Suite

> End-to-end automated test suite for the **PropFind** property listing platform.  
> Covers **75 test cases** across **15 user stories** and **9 pages**, with integrated defect detection, rich HTML reporting, and a CI/CD pipeline that publishes the live test report to GitHub Pages on every push.

---

## Technology Stack

| Component | Technology | Version |
|---|---|---|
| Language | Java | 21 |
| Browser Automation | Selenium WebDriver | 4.27.0 |
| BDD Framework | Cucumber | 7.34.7 |
| Test Runner | TestNG | 7.10.2 |
| Dependency Injection | Cucumber PicoContainer | 7.34.7 |
| Reporting | Extent Reports (Spark) | 5.1.2 |
| Build Tool | Maven | 3.9+ |
| CI/CD | GitHub Actions | — |

> **ChromeDriver is managed automatically** by Selenium Manager (bundled in Selenium 4.27).  
> You do not need to download or configure it manually.

---

## Test Coverage

| Page | User Story | Test Cases | Owner | Status |
|---|---|---|---|---|
| `login.html` | US01 — Login & Registration | TC01–TC05 | Raman | ✅ Complete |
| `search-results.html` | US02 — Smart Property Search · US03 — Advanced Filters | TC06–TC15 | Prabavathi | ✅ Complete |
| `map-view.html` | US04 — Map View | TC16–TC20 | Raman | ✅ Complete |
| `property-detail.html` | US05–US06 — Property Detail & Locality · US09–US10 — Visit & Inquiry · US14 — Trust & Safety | TC21–TC30, TC41–TC50, TC66–TC70 | Rahul · Rohit | ✅ Complete |
| `dashboard.html` | US07 — User Dashboard · US11 — Preferences | TC31–TC35, TC51–TC55 | Nived | ✅ Complete |
| `compare.html` | US08 — Compare Properties | TC36–TC40 | Nithyasri | ✅ Complete |
| `budget-planner.html` | US12 — Budget Planner | TC56–TC60 | Nithyasri | ✅ Complete |
| `add-listing.html` | US13 — Add Listing | TC61–TC65 | Sherin | ✅ Complete |
| `moving-assistant.html` | US15 — Moving Assistant | TC71–TC75 | Sherin | ✅ Complete |

**75 test cases total · 15 user stories · 9 pages · 7 engineers**

---

## Prerequisites

| Tool | Version | Notes |
|---|---|---|
| JDK | 21 | [Adoptium Temurin](https://adoptium.net) recommended |
| Maven | 3.9+ | [Download](https://maven.apache.org/download.cgi) |
| Google Chrome | Latest stable | [Download](https://www.google.com/chrome) |
| IntelliJ IDEA | Any recent | Community edition is free |

Verify before running:

```bash
java -version   # openjdk 21...
mvn -version    # Apache Maven 3.9...
```

---

## Quick Start

```bash
# 1. Clone
git clone <repo-url>
cd SeleniumMiniProject

# 2. Resolve dependencies
mvn dependency:resolve

# 3. Run the full suite (headless on CI, headed locally)
mvn test
```

Reports are written to `target/` after every run — see [Reports](#reports) below.

---

## Running Tests

### Run the full suite
```bash
mvn test
```

### Run a single test case
```bash
mvn test -Dcucumber.filter.tags="@TC06"
```

### Run all cases for a user story
```bash
mvn test -Dcucumber.filter.tags="@US02"
```

### Run by priority
```bash
mvn test -Dcucumber.filter.tags="@High"
```

### Force headless / headed mode
```bash
mvn test -Dheadless=true    # headless (default on CI)
mvn test -Dheadless=false   # headed even on CI
```

### Point at a remote or local server
```bash
mvn test -DbaseUrl=http://localhost:8080/
```

---

## Reports

Three report formats are produced after every run:

| Report | Path | Description |
|---|---|---|
| **Extent Report** | `target/extent-reports/ExtentReport_<timestamp>.html` | Rich interactive dashboard — named scenarios, tags, pass/fail, screenshots on failure |
| **Cucumber HTML** | `target/cucumber-reports/<page>/index.html` | Per-feature BDD report with step-level results |
| **TestNG Surefire** | `target/surefire-reports/index.html` | Lightweight per-method summary |

The **Extent Report** is the primary output. Every Cucumber scenario appears under its real name (e.g. `TC01 – Register with valid details`) with its `@TCxx`, `@USxx`, and priority tags assigned as categories, making it trivial to filter and export for a defect report.

### Live CI report
The latest Extent Report from the `main` branch is automatically published to **GitHub Pages** by the CI pipeline:

```
https://<org>.github.io/<repo>/
```

---

## Project Structure

```
SeleniumMiniProject/
│
├── pom.xml                                      ← Maven build + dependency declarations
│
├── propfind-website/                            ← Static HTML/JS application under test
│   ├── index.html
│   ├── login.html
│   ├── search-results.html
│   ├── map-view.html
│   ├── property-detail.html
│   ├── dashboard.html
│   ├── compare.html
│   ├── budget-planner.html
│   ├── add-listing.html
│   ├── moving-assistant.html
│   ├── css/style.css
│   └── js/properties.js                        ← Shared property data + Auth helpers
│
├── docs/
│   ├── ELEMENT_ID_REGISTRY.md                  ← Every element ID on every page
│   ├── TEAM_ASSIGNMENTS.md                     ← TC ownership and status
│   └── Rearranged_testcases.xlsx               ← Full test case definitions (TC01–TC75)
│
└── src/test/
    ├── resources/
    │   ├── test.xml                             ← TestNG suite — all 10 runners registered
    │   └── features/                           ← Cucumber .feature files (one per page)
    │       ├── Login.feature
    │       ├── MapView.feature
    │       ├── PropertyDetail.feature
    │       ├── Dashboard.feature
    │       ├── Compare.feature
    │       ├── BudgetPlanner.feature
    │       ├── AddListing.feature
    │       └── MovingAssistant.feature
    │
    └── java/com/propfind/
        ├── base/
        │   └── BaseTestClass.java              ← Shared WebDriver lifecycle for plain TestNG tests
        ├── config/
        │   └── SiteConfig.java                 ← baseUrl resolution (-DbaseUrl or bundled file://)
        ├── driver/
        │   └── DriverFactory.java              ← ChromeDriver factory; headless auto-detection
        ├── listeners/
        │   ├── ExtentReportListener.java       ← TestNG listener; delegates to ExtentReportManager
        │   └── ExtentReportManager.java        ← Singleton report instance shared by all hooks
        ├── hooks/
        │   ├── CucumberExtentHooks.java        ← Global Cucumber hook; names every scenario in the report
        │   ├── LoginHooks.java
        │   ├── MapViewHooks.java
        │   ├── PropertyDetailHooks.java
        │   ├── DashboardHooks.java
        │   ├── CompareHooks.java
        │   ├── BudgetPlannerHooks.java
        │   ├── AddListingHooks.java
        │   └── MovingAssistantHooks.java
        ├── context/                            ← PicoContainer DI contexts (one per page)
        ├── pages/                              ← Page Object Model classes (one per page)
        ├── steps/                              ← Cucumber step definitions (one per page)
        └── runners/                            ← TestNG Cucumber runners (one per page)
```

---

## Architecture

Each page is covered by a strict vertical slice of six files:

```
Feature file  →  Runner  →  Hooks  →  Steps  →  Page Object
                                  ↑
                         TestContext  (PicoContainer DI)
```

| Layer | Responsibility |
|---|---|
| **Feature** | Business-readable Gherkin scenarios tagged `@TCxx @USxx @Priority` |
| **Runner** | `@CucumberOptions` wiring — feature path, glue package, report output paths |
| **Hooks** | `@Before`/`@After` per scenario — browser teardown, screenshot on failure |
| **Steps** | Maps Gherkin steps to Page Object calls; holds all `Assert` statements |
| **Page Object** | All locators and Selenium interactions — zero assertions |
| **TestContext** | PicoContainer-managed shared state; lazily creates `WebDriver` and `Page` |

### Key design decisions

- **PicoContainer DI** — `TestContext` is injected into both `Steps` and `Hooks`, eliminating static state and ensuring per-scenario driver isolation.
- **Lazy driver init** — `getDriver()` opens Chrome only when the first step runs; tag-filtered or dry-run scenarios never touch the browser.
- **`localStorage` auth bypass** — Auth-protected pages are seeded via `JavascriptExecutor` (`loginAs()` / `seedUser()`), avoiding fragile UI login flows in test setup.
- **Explicit waits only** — `implicitlyWait` is `0`; all synchronisation uses `WebDriverWait` (10 s). `Thread.sleep` is prohibited.
- **Named Extent Report nodes** — `CucumberExtentHooks` intercepts every scenario via a global `@Before(order = -1)` / `@After(order = -1)` pair and creates a node labelled `TC01 – Register with valid details` with full tag categories, replacing the generic `runScenario` entries that Cucumber/TestNG produce by default.
- **Headless CI** — `DriverFactory` auto-enables `--headless=new` when the `GITHUB_ACTIONS` environment variable is present, or when `-Dheadless=true` is passed.

---

## CI/CD Pipeline

The [`.github/workflows/continuous-testing.yaml`](.github/workflows/continuous-testing.yaml) pipeline runs on every push to `main`:

1. Checks out the repository
2. Sets up **Java 21 (Temurin)**
3. Caches the Maven local repository
4. Installs **Google Chrome** via `browser-actions/setup-chrome`
5. Runs `mvn clean test` in headless mode (`GITHUB_ACTIONS=true` is set automatically)
6. Copies the latest Extent Report HTML to `report-site/index.html`
7. Deploys it to **GitHub Pages**

The pipeline uses `continue-on-error: true` on the test step so the report is always published even when tests fail — the live report reflects the actual pass/fail state.

---

## Coding Conventions

| Rule | Detail |
|---|---|
| Package | `package com.propfind.<subpackage>;` for every Java file |
| Locator priority | `By.id` → `By.cssSelector` → `By.name` → `By.xpath` (last resort) |
| Waits | `WebDriverWait` (10 s) only — **never `Thread.sleep`** |
| Assertions | `org.testng.Assert` in step definitions only — **never inside a Page Object** |
| Driver access | Only inside Page Objects — **never in step definitions** |
| Select options | Pass exact visible `<option>` text to `selectByVisibleText()` |
| Auth-protected pages | Use `loginAs()` via `JavascriptExecutor` — do not fill the login form |
| Furnishing filter | Locate with `By.cssSelector("input[name='furnish'][value='...']")` — no individual IDs |

---

## Common Issues

| Symptom | Cause | Fix |
|---|---|---|
| `WebDriverException: Chrome not reachable` | Chrome not installed or wrong version | Install/update Google Chrome to latest stable |
| `SessionNotCreatedException` | Chrome/ChromeDriver mismatch | Update Chrome; Selenium Manager auto-downloads the matching driver |
| `NoSuchElementException` | Wrong or missing element ID | Cross-check `docs/ELEMENT_ID_REGISTRY.md` against the HTML source |
| `TimeoutException` on page load | JS-rendered content not ready | Wait for a reliable landmark in `navigateTo()`, not just page load |
| `localStorage` / session empty | Auth not seeded before navigation | Use `loginAs()` or `seedUser()` in the `@Before` hook |
| `No features found` | Wrong path in `@CucumberOptions` | Path must be relative to the project root, e.g. `src/test/resources/features/Login.feature` |
| Compilation error — symbol not found | JAR not downloaded | Run `mvn dependency:resolve` |
| `NoSuchWindowException` after a test | Previous `driver.quit()` didn't run | Verify the `@After` hook in the relevant `Hooks` class calls `ctx.getDriver().quit()` |
| All Cucumber scenarios show as `runScenario` in report | Old listener without `CucumberExtentHooks` | Ensure `CucumberExtentHooks.java` is on the Cucumber glue path (`com.propfind`) |

---

## Reference Files

| File | What it demonstrates |
|---|---|
| [`Login.feature`](src/test/resources/features/Login.feature) | Tag structure, `Background`, `Scenario Outline`, `Examples` table |
| [`LoginPage.java`](src/test/java/com/propfind/pages/LoginPage.java) | Locator constants, `WebDriverWait` pattern, `navigateTo()`, `seedUser()` |
| [`LoginSteps.java`](src/test/java/com/propfind/steps/LoginSteps.java) | PicoContainer injection, step annotations, `Assert` usage |
| [`LoginTestContext.java`](src/test/java/com/propfind/context/LoginTestContext.java) | Lazy driver init, `SiteConfig`, no-arg constructor |
| [`LoginHooks.java`](src/test/java/com/propfind/hooks/LoginHooks.java) | `@Before`/`@After`, screenshot capture, `driver.quit()` |
| [`LoginTest.java`](src/test/java/com/propfind/runners/LoginTest.java) | `@CucumberOptions`, report output paths, `AbstractTestNGCucumberTests` |
| [`CucumberExtentHooks.java`](src/test/java/com/propfind/hooks/CucumberExtentHooks.java) | Global hook pattern; named scenario nodes in Extent Report |
| [`ExtentReportManager.java`](src/test/java/com/propfind/listeners/ExtentReportManager.java) | Singleton report lifecycle shared across TestNG listener and Cucumber hooks |
