# PropFind – Selenium Test Automation

Automated UI test suite for the **PropFind** property listing platform.
Built with **Java 21 · Selenium 4.27 · Cucumber 7.34 · TestNG 7.10 · Extent Reports 5.1**.

---

## Team assignments

| Person | Pages | User Stories | Test Cases | Status |
|---|---|---|---|---|
| Raman | `login.html`, `map-view.html` | US01, US04 | TC01–TC05, TC16–TC20 | ✅ Done |
| Prabavathi | `search-results.html` | US02, US03 | TC06–TC15 | ✅ Done |
| Nithyasri | `compare.html`, `budget-planner.html` | US08, US12 | TC36–TC40, TC56–TC60 | – |
| Sherin | `add-listing.html`, `moving-assistant.html` | US13, US15 | TC61–TC65, TC71–TC75 | – |
| Nived | `dashboard.html` | US07, US11 | TC31–TC35, TC51–TC55 | – |
| Rahul | `property-detail.html` (Part A) | US05, US06 | TC21–TC30 | ✅ Done |
| Rohit | `property-detail.html` (Part B) | US09, US10, US14 | TC41–TC50, TC66–TC70 | ✅ Done |

When you finish your tests, mark your row **✅ Done** in both this table and in `docs/TEAM_ASSIGNMENTS.md`.

---

## Prerequisites

Install these before doing anything else.

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

### 4 — Smoke-test the existing suite
```bash
mvn test
```
Chrome should open, run the Login and Map View scenarios, then close.
Reports land in `target/` — see the [Viewing reports](#viewing-reports) section below.

---

## Project structure

```
SeleniumMiniProject/
│
├── pom.xml                                    ← dependencies + Maven Surefire config
│
├── propfind-website/                          ← the live HTML site under test
│   ├── index.html
│   ├── login.html
│   ├── map-view.html
│   ├── search-results.html
│   ├── property-detail.html
│   ├── dashboard.html
│   ├── compare.html
│   ├── budget-planner.html
│   ├── add-listing.html
│   ├── moving-assistant.html
│   ├── css/style.css
│   └── js/properties.js
│
├── docs/                                      ← planning & reference docs
│   ├── GENERATE_TESTS.md                      ← AI-assisted test generation guide (start here)
│   ├── ELEMENT_ID_REGISTRY.md                 ← every element ID on every page
│   ├── TEAM_ASSIGNMENTS.md                    ← who owns which test cases
│   └── Rearranged_testcases.xlsx              ← full test case definitions (TC01–TC75)
│
└── src/test/
    ├── resources/
    │   ├── test.xml                           ← TestNG suite — controls which tests run
    │   └── features/
    │       ├── Login.feature                  ← TC01–TC05  (Raman ✅)
    │       ├── MapView.feature                ← TC16–TC20  (Raman ✅)
    │       └── <YourPage>.feature             ← add yours here
    │
    └── java/com/propfind/
        ├── base/
        │   └── BaseTestClass.java             ← shared WebDriver setup (do not modify)
        ├── driver/
        │   └── DriverFactory.java             ← central ChromeDriver factory (do not modify)
        ├── listeners/
        │   └── ExtentReportListener.java      ← Extent Reports hook (do not modify)
        ├── pages/
        │   ├── LoginPage.java                 ─┐ Login reference implementation
        │   └── MapViewPage.java               ─┘ Map View reference implementation
        ├── steps/
        │   ├── LoginSteps.java                ─┐
        │   └── MapViewSteps.java              ─┘
        ├── context/
        │   ├── LoginTestContext.java          ─┐
        │   └── MapViewTestContext.java        ─┘
        ├── hooks/
        │   ├── LoginHooks.java                ─┐
        │   └── MapViewHooks.java              ─┘
        └── runners/
            ├── LoginTest.java                 ─┐
            └── MapViewTest.java               ─┘
```

---

## Your workflow — step by step

### Step 1 — Generate your test files with AI

Open Bob (or your AI assistant) and tag the guide:

```
@docs\GENERATE_TESTS.md
```

When prompted, type your **first name exactly as it appears** in the team list (e.g. `Prabavathi`).
The assistant will:
1. Look up your assigned TC IDs in `docs/TEAM_ASSIGNMENTS.md`
2. Read your test case definitions from `docs/Rearranged_testcases.xlsx`
3. Pull element IDs from `docs/ELEMENT_ID_REGISTRY.md`
4. Generate all 6 files for each page you own (feature, Page Object, steps, context, hooks, runner)
5. Add your `<test>` block to `src/test/resources/test.xml`

> You do **not** need to write any boilerplate by hand — the AI handles it.
> Review every generated file before running, and correct anything that looks wrong.

---

### Step 2 — Enable only your tests in test.xml

`src/test/resources/test.xml` controls which runners execute when you run `mvn test`.
Before running, **comment out every block that isn't yours** so you don't run other people's
(potentially unfinished) tests.

Find your block — it will look like this (already uncommented by the AI):

```xml
<!-- US02, US03  Search Results  (Prabavathi) -->
<test name="Search Results Tests">
    <classes>
        <class name="com.propfind.runners.SearchResultsTest"/>
    </classes>
</test>
```

Comment out every other active `<test>` block that isn't yours. For example:

```xml
<!-- Not my tests — comment out during development -->
<!--
<test name="Login Tests">
    <classes>
        <class name="com.propfind.runners.LoginTest"/>
    </classes>
</test>
-->
```

> **Important:** restore the commented-out blocks before pushing to `main` so the full suite
> continues to work for everyone.

---

### Step 3 — Run your tests

```bash
mvn test
```

Chrome opens headlessly, runs your scenarios, then closes.

To run a **single test case** without editing `test.xml`:
```bash
mvn test -Dcucumber.filter.tags="@TC06"
```

To run **all cases for your user story**:
```bash
mvn test -Dcucumber.filter.tags="@US02"
```

To run **only high-priority** cases:
```bash
mvn test -Dcucumber.filter.tags="@High"
```

---

### Step 4 — Read the reports

After every run, check these files:

| Report | Path |
|---|---|
| **Extent Reports** (main dashboard) | `target/extent-reports/ExtentReport_<timestamp>.html` |
| **Cucumber HTML** (per page) | `target/cucumber-reports/<pagename>/index.html` |
| **TestNG built-in** | `target/surefire-reports/index.html` |

Open any of them in your browser. The Extent Report is the most readable — it shows pass/fail,
step-by-step breakdown, and a screenshot attachment for every failed scenario.

---

### Step 5 — Triage failures

When a test fails, work out **which layer** the problem is in before touching any code.

#### Is it a website bug or a Selenium bug?

| Symptom | Likely cause | What to do |
|---|---|---|
| Test fails with `NoSuchElementException` | Wrong element ID in test, or ID missing from page | Check `docs/ELEMENT_ID_REGISTRY.md` — compare with the actual HTML in `propfind-website/` |
| Test fails with `TimeoutException` waiting for element | JS hasn't rendered the element yet | Increase wait, or wait for a different landmark in `navigateTo()` |
| Test fails but the manual flow in the browser works fine | Selenium timing issue | Add an explicit `WebDriverWait` for the element or condition that's racing |
| Test fails AND the manual flow also fails | **Website bug** — the feature is broken | Fix the HTML/JS in `propfind-website/`, re-run to confirm test passes |
| Assert fails with wrong text/state | Website behaviour changed, or wrong assertion | Re-read the Expected Result in `docs/Rearranged_testcases.xlsx` and align the assertion |
| `SessionNotCreatedException` | Chrome/ChromeDriver version mismatch | Update Google Chrome to latest stable |
| `localStorage` is empty / auth redirects unexpectedly | `--allow-file-access-from-files` not set | This flag is already in `DriverFactory` — do not bypass it |

**Rule of thumb:** open the failed scenario's screenshot from the Extent Report first.
If the screenshot shows the page in a state the feature should handle, it's a Selenium problem.
If it shows a broken or blank page, it's a website problem.

---

### Step 6 — Update the docs when you're done

Once all your scenarios pass:

1. **`docs/TEAM_ASSIGNMENTS.md`** — change your `Status` column from `–` to `✅ Done`
2. **`README.md`** — change your row in the [Team assignments](#team-assignments) table to `✅ Done`
3. Commit and push:

```bash
git add .
git commit -m "feat: add tests for <YourPage> (TC_XX–TC_YY)"
git push
```

> If your push is rejected because a teammate pushed first, rebase:
> ```bash
> git fetch origin
> git rebase origin/main
> # resolve any conflicts, then:
> git push
> ```

---

## Coding conventions (quick reference)

| Rule | Detail |
|---|---|
| Package | All Java files use `package com.propfind.<subpackage>;` |
| Locator priority | `By.id` → `By.cssSelector` → `By.name` → `By.xpath` (last resort) |
| Waits | `WebDriverWait` (10 s) only — **never `Thread.sleep`** |
| Assertions | `org.testng.Assert` in step definitions only — **never inside a Page Object** |
| Driver access | Only inside Page Objects — **never in step definitions** |
| Select options | Always pass the exact visible `<option>` text to `selectByVisibleText()` |
| Auth-protected pages | Use `loginAs()` via `JavascriptExecutor` — do **not** fill the login form |
| Base classes | `BaseTestClass`, `DriverFactory`, `ExtentReportListener` — **do not modify** |

Full conventions are in `docs/GENERATE_TESTS.md` under **Established conventions**.

---

## Reference implementations

Read these before writing anything new:

| File | What it demonstrates |
|---|---|
| [`Login.feature`](src/test/resources/features/Login.feature) | Tags, `Background`, `Scenario Outline`, `Examples` |
| [`LoginPage.java`](src/test/java/com/propfind/pages/LoginPage.java) | Locator constants, wait strategy, `navigateTo()`, query methods, `seedUser()` |
| [`LoginSteps.java`](src/test/java/com/propfind/steps/LoginSteps.java) | PicoContainer injection, `@Given/@When/@And/@Then`, `Assert` style |
| [`LoginTestContext.java`](src/test/java/com/propfind/context/LoginTestContext.java) | No-arg constructor, `DriverFactory`, `baseUrl` resolution |
| [`LoginHooks.java`](src/test/java/com/propfind/hooks/LoginHooks.java) | `@Before`/`@After`, screenshot on failure, `driver.quit()` |
| [`LoginTest.java`](src/test/java/com/propfind/runners/LoginTest.java) | `@CucumberOptions`, report paths, `AbstractTestNGCucumberTests` |

---

## Common issues

| Problem | Fix |
|---|---|
| `WebDriverException: Chrome not reachable` | Make sure Google Chrome is installed and up to date |
| `SessionNotCreatedException` | Update Chrome to latest stable — Selenium Manager will re-download the matching driver |
| Element not found / `NoSuchElementException` | Confirm the `id` exists in `docs/ELEMENT_ID_REGISTRY.md` and matches the HTML |
| `TimeoutException` waiting for element | The page renders via JS — wait for a reliable landmark in `navigateTo()`, not just page load |
| `localStorage` / session empty on auth-protected page | Use `loginAs()` (JS injection) from the `@Before` hook — never the login form in Cucumber Background |
| `No features found` | The `features` path in `@CucumberOptions` must be relative to the project root |
| Compilation error — symbol not found | Run `mvn dependency:resolve` to re-download all JARs |
| `NoSuchWindowException` after a test | A previous test's `driver.quit()` didn't run — check your Hooks `@After` method |
