# PropFind – Team Instruction Manual

Everything you need to know from unzipping the project to running your tests.

---

## Step 1 — Open the project

1. Extract the zip to any folder on your machine.
2. Open **IntelliJ IDEA** → **Open** → select the extracted folder (the one containing `pom.xml`).
3. IntelliJ will detect the Maven project and download dependencies automatically. Wait for the progress bar in the bottom-right to finish.

---

## Step 2 — Verify your Java and Chrome versions

| Requirement | Minimum |
|---|---|
| Java (JDK) | 21 |
| Google Chrome | any recent version |
| ChromeDriver | **must match your Chrome version** |

To check your Chrome version: open Chrome → `⋮` menu → Help → About Google Chrome.

ChromeDriver is managed automatically by Selenium 4 — no manual download needed.

---

## Step 3 — Generate your test files

1. Open a **Bob AI chat** session inside IntelliJ (or your AI assistant).
2. Type `@src\Assist\GENERATE_TESTS.md` and press Enter.
3. When asked **"What is your name?"**, type your first name exactly as listed below:

| Name | Pages | Test Cases |
|---|---|---|
| Raman | `login.html`, `map-view.html` | TC01–TC05, TC16–TC20 |
| Prabavathi | `search-results.html` | TC06–TC15 |
| Nithyasri | `compare.html`, `budget-planner.html` | TC36–TC40, TC56–TC60 |
| Sherin | `add-listing.html`, `moving-assistant.html` | TC61–TC65, TC71–TC75 |
| Nived | `dashboard.html` | TC31–TC35, TC51–TC55 |
| Rahul | `property-detail.html` (Part A) | TC21–TC30 |
| Rohit | `property-detail.html` (Part B) | TC41–TC50, TC66–TC70 |

4. The AI will confirm your assigned TC IDs — review and reply **"Yes"** to proceed.
5. The AI generates **3 files** for you:
   - `src/Assist/features/<Page>.feature` — Gherkin BDD scenarios
   - `src/test/java/pages/<Page>Page.java` — Page Object class
   - `src/test/java/tests/<Page>Test.java` — TestNG test class
6. Copy each generated file into the correct location in the project.

---

## Step 4 — Run your tests

### Option A — Run from IntelliJ

Right-click `src/test/test.xml` → **Run**.

### Option B — Run from Maven terminal

```
mvn test
```

Both options run the full suite and produce an HTML report automatically.

---

## Step 5 — View the HTML report

After the run completes, open:

```
target/propfind-report.html
```

Open it in any browser. It shows:
- A summary bar — total / passed / failed / skipped / duration
- A full results table — TC ID, method name, status badge, duration
- For each failure — a collapsible error message and full stack trace

---

## Step 6 — Understand a failure

When a test fails, check the report's **Error** column first. There are two types of failures:

| Type | Meaning | What to do |
|---|---|---|
| **Website defect** | The app does not behave as the TC expects | Document the defect; the test is correct |
| **Test code defect** | The test assertion or locator is wrong | Fix the generated test file |

Refer to `src/Assist/ELEMENT_ID_REGISTRY.md` to verify that the element IDs used in your Page Object exist in the HTML.

---

## Step 7 — Reset the project (instructor only)

To wipe all generated files and return to a clean state for the next team:

1. Open a Bob AI chat session.
2. Type `@src\Assist\RESET.md` and press Enter.
3. The AI will list all files it will delete and ask for confirmation.
4. Reply **"Yes"** — it deletes only generated files and leaves all reference/config files intact.

The following files are **never deleted** by reset:

- `src/Assist/features/SearchResults.feature` — sample Gherkin reference
- `src/Assist/selenium/pages/SearchResultsPage.java` — sample Page Object reference
- `src/Assist/selenium/tests/SearchResultsTest.java` — sample Test Class reference
- `src/test/java/base/BaseTest.java` — shared driver setup
- `src/test/java/reporter/TestListener.java` — HTML report generator
- All `src/Assist/` documentation files

---

## Reference files

| File | Purpose |
|---|---|
| `src/Assist/Guide.md` | Full project overview and architecture |
| `src/Assist/TEAM_ASSIGNMENTS.md` | Name → TC assignment table |
| `src/Assist/ELEMENT_ID_REGISTRY.md` | All stable HTML element IDs per page |
| `src/Assist/Rearranged_testcases.xlsx` | Master test case definitions (TC01–TC75) |
| `src/Assist/selenium/pages/SearchResultsPage.java` | Sample Page Object to copy style from |
| `src/Assist/selenium/tests/SearchResultsTest.java` | Sample Test Class to copy style from |
| `src/Assist/features/SearchResults.feature` | Sample Gherkin feature to copy style from |

---

## Quick-reference checklist

- [ ] JDK 21 installed and selected in IntelliJ
- [ ] Maven dependencies downloaded (no red imports)
- [ ] Typed `@src\Assist\GENERATE_TESTS.md` and provided my name
- [ ] Copied the 3 generated files into the correct folders
- [ ] Ran `mvn test` or ran `test.xml` from IntelliJ
- [ ] Opened `target/propfind-report.html` and reviewed results
