# PropFind – Selenium Test Automation

Automated UI test suite for the **PropFind** property listing platform.
Built with **Java 21 · Selenium 4 · Cucumber 7 · TestNG 7 · Extent Reports 5**.

---

## Team assignments

| Person | Pages | User Stories | Test Cases |
|---|---|---|---|
| Raman | `login.html`, `map-view.html` | US01, US04 | TC01–TC05, TC16–TC20 |
| Prabavathi | `search-results.html` | US02, US03 | TC06–TC15 |
| Nithyasri | `compare.html`, `budget-planner.html` | US08, US12 | TC36–TC40, TC56–TC60 |
| Sherin | `add-listing.html`, `moving-assistant.html` | US13, US15 | TC61–TC65, TC71–TC75 |
| Nived | `dashboard.html` | US07, US11 | TC31–TC35, TC51–TC55 |
| Rahul | `property-detail.html` (Part A) | US05, US06 | TC21–TC30 |
| Rohit | `property-detail.html` (Part B) | US09, US10, US14 | TC41–TC50, TC66–TC70 |

---

## Prerequisites

Install these on your machine before doing anything else.

| Tool | Version | Download |
|---|---|---|
| JDK | 21 | https://adoptium.net |
| Maven | 3.9+ | https://maven.apache.org/download.cgi |
| Google Chrome | Latest stable | https://www.google.com/chrome |
| IntelliJ IDEA | Any recent | https://www.jetbrains.com/idea (Community is free) |

> **ChromeDriver is managed automatically** by Selenium Manager (bundled in Selenium 4.27).
> You do **not** need to download or configure ChromeDriver manually.

Verify your setup:
```bash
java -version      # should print: openjdk 21...
mvn -version       # should print: Apache Maven 3.9...
```

---

## Getting started

### 1 — Clone the repository
```bash
git clone <repo-url>
cd SeleniumMiniProject
```

### 2 — Install dependencies
```bash
mvn dependency:resolve
```
Maven downloads all JARs (Selenium, Cucumber, TestNG, Extent Reports) into your local cache.
This only needs to happen once, or after `pom.xml` changes.

### 3 — Open in IntelliJ
- **File → Open** → select the `SeleniumMiniProject` folder
- IntelliJ detects the `pom.xml` and imports the project automatically
- Wait for the Maven sync to finish (progress bar bottom-right)

### 4 — Verify with the existing smoke tests
```bash
mvn test
```
Chrome should open, run the Login and PropertyDetail scenarios, then close.
After the run, open these reports:
- `target/extent-reports/ExtentReport_<timestamp>.html` — rich HTML dashboard
- `target/cucumber-reports/login/index.html` — Cucumber report for Login
- `target/cucumber-reports/propertydetail/index.html` — Cucumber report for Property Detail

If both suites pass you are ready to write your own tests.

---

## Project structure

```
SeleniumMiniProject/
│
├── pom.xml                              ← dependencies + Maven Surefire config
│
├── AssistingFiles/
│   ├── GENERATE_TESTS.md               ← step-by-step guide for writing your tests
│   ├── ELEMENT_ID_REGISTRY.md          ← every element ID on every page (read this first)
│   ├── TEAM_ASSIGNMENTS.md             ← who owns which test cases
│   └── Rearranged_testcases.xlsx       ← full test case definitions (TC01–TC75)
│
└── src/test/
    ├── test.xml                         ← TestNG suite runner
    ├── java/                            ← all Java source files (flat, no subfolders)
    │   ├── BaseTestClass.java           ← shared WebDriver setup/teardown
    │   ├── ExtentReportListener.java    ← auto-generates Extent HTML report
    │   │
    │   ├── LoginPage.java              ─┐
    │   ├── LoginSteps.java              │  Login reference implementation
    │   ├── LoginTestContext.java        │  (TC01–TC05)
    │   ├── LoginHooks.java              │
    │   ├── LoginTest.java              ─┘
    │   │
    │   ├── PropertyDetailPage.java     ─┐
    │   ├── PropertyDetailSteps.java     │  Property Detail (TC21–TC30)
    │   ├── PropertyDetailTestContext.java│
    │   ├── PropertyDetailHooks.java     │
    │   └── PropertyDetailTest.java     ─┘
    │
    └── resources/
        ├── features/
        │   ├── Login.feature            ← Gherkin scenarios TC01–TC05
        │   └── PropertyDetail.feature   ← Gherkin scenarios TC21–TC30
        └── TargetWebsite - Copy/        ← the HTML site under test (do not modify)
```

---

## How to add your tests

Read **`AssistingFiles/GENERATE_TESTS.md`** — it contains the complete step-by-step instructions.
The short version is:

For each page you are assigned, create **6 files** following the Login implementation as a template:

| # | File | Location |
|---|---|---|
| 1 | `<Page>.feature` | `src/test/resources/features/` |
| 2 | `<Page>Page.java` | `src/test/java/` |
| 3 | `<Page>Steps.java` | `src/test/java/` |
| 4 | `<Page>TestContext.java` | `src/test/java/` |
| 5 | `<Page>Hooks.java` | `src/test/java/` |
| 6 | `<Page>Test.java` | `src/test/java/` |

Then **uncomment your `<test>` block** in `src/test/test.xml`.

### Rules to follow

- **No package declarations** — all Java files sit in the default package (flat `src/test/java/`)
- **Never add assertions inside a Page Object** — only in step definitions
- **Never call `driver` directly inside step definitions** — always go through the Page Object
- **Never use `Thread.sleep`** — use `WebDriverWait` with a 10-second timeout
- **All element locators must exist in `ELEMENT_ID_REGISTRY.md`** before you use them
- **Copy exact `<option>` text** from the HTML when calling `selectByVisibleText()`
- **Auth-protected pages** (`dashboard.html`, `property-detail.html` for some flows) — see the auth section in `GENERATE_TESTS.md` for how to seed `localStorage` instead of using the login form

---

## Running tests

### Run the full suite
```bash
mvn test
```

### Run a single feature by tag
```bash
mvn test -Dcucumber.filter.tags="@TC01"
mvn test -Dcucumber.filter.tags="@US05"
mvn test -Dcucumber.filter.tags="@High"
```

### Run against a deployed URL instead of the local files
```bash
mvn test -DbaseUrl=http://localhost:8080/
```

---

## Viewing reports

| Report | Path | How to open |
|---|---|---|
| **Extent Reports** (main) | `target/extent-reports/ExtentReport_<timestamp>.html` | Open in any browser |
| **Cucumber HTML – Login** | `target/cucumber-reports/login/index.html` | Open in any browser |
| **Cucumber HTML – PropertyDetail** | `target/cucumber-reports/propertydetail/index.html` | Open in any browser |
| **TestNG built-in** | `target/surefire-reports/index.html` | Open in any browser |

> Reports are regenerated on every `mvn test` run.
> The Extent Report filename includes a timestamp so old runs are never overwritten.

---

## test.xml — activating your tests

`src/test/test.xml` controls which test classes run.
Currently **Login Tests** and **Property Detail Tests** are active.
All other blocks are commented out.

When your files are ready, find your block and remove the `<!--` / `-->` around it:

```xml
<!-- Remove the comment markers below when your tests are ready -->
<!--
<test name="Search Results Tests">
    <classes>
        <class name="SearchResultsTest"/>
    </classes>
</test>
-->
```

Becomes:

```xml
<test name="Search Results Tests">
    <classes>
        <class name="SearchResultsTest"/>
    </classes>
</test>
```

---

## Reference files

Before writing any code, read these existing implementations:

| File | What it shows |
|---|---|
| `Login.feature` | Correct Gherkin style: `@tags`, `Background`, `Scenario Outline`, `Examples` |
| `LoginPage.java` | Locator constants, wait strategy, `navigateTo()`, query methods |
| `LoginSteps.java` | Constructor injection, `@Given/@When/@And/@Then`, assertion style |
| `LoginTestContext.java` | How to create and expose the WebDriver and Page Object |
| `LoginHooks.java` | `@Before`/`@After`, screenshot on failure, `driver.quit()` |
| `LoginTest.java` | `@CucumberOptions` runner — copy and change the feature path and class name |

---

## Common issues

| Problem | Fix |
|---|---|
| `WebDriverException: Chrome not reachable` | Make sure Google Chrome is installed and up to date |
| `SessionNotCreatedException` | Chrome and ChromeDriver version mismatch — update Chrome to latest stable |
| Test times out waiting for an element | The page uses JS to render content; make sure your `navigateTo()` waits for a landmark element, not just page load |
| `localStorage` / `sessionStorage` empty | Run Chrome with `--allow-file-access-from-files` (already set in `BaseTestClass` and all `TestContext` classes) |
| Compilation error — symbol not found | Run `mvn dependency:resolve` to make sure all JARs are downloaded |
| `No features found` | Check the `features` path in `@CucumberOptions` — it must be relative to the project root |
