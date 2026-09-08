# PropFind – Reset Prompt

You are resetting this project back to its **clean starting state** — removing all student-generated test files so a fresh team can begin from scratch.

---

## What "generated" means

Generated files are any `.java` files in `src/test/java/pages/` or `src/test/java/tests/` that were produced by a team member running `GENERATE_TESTS.md`, plus any `.feature` files in `src/Assist/features/` that were created alongside them.

---

## Whitelist — files that must NEVER be deleted

These three files exist inside otherwise-generated folders but are **permanent samples** — skip them unconditionally, even when deleting everything else in their folder:

| Whitelisted File | Reason |
|---|---|
| `src/Assist/features/SearchResults.feature` | Permanent Gherkin sample — reference for generated `.feature` files |
| `src/Assist/selenium/pages/SearchResultsPage.java` | Permanent Page Object sample — reference for generated page classes |
| `src/Assist/selenium/tests/SearchResultsTest.java` | Permanent Test Class sample — reference for generated test classes |

> **Rule:** Before deleting any file, check it against this whitelist. If it matches, skip it silently — do not delete, do not ask.

---

## Files to DELETE

Delete every file that matches these patterns, **unless it is on the whitelist above**:

### Java — Page Objects (generated)
```
src/test/java/pages/*.java
```

### Java — Test Classes (generated)
```
src/test/java/tests/*.java
```

### Gherkin — Feature files (generated)
```
src/Assist/features/*.feature
```
— **except** `SearchResults.feature` (whitelisted above)

---

## Files to PRESERVE — never delete these

| File | Why |
|---|---|
| `src/test/java/base/BaseTest.java` | Shared driver lifecycle — not generated |
| `src/test/java/reporter/TestListener.java` | HTML reporter — not generated |
| `src/test/java/SampleTest.java` | Original scaffold test — not generated |
| `src/test/test.xml` | TestNG suite config — not generated |
| `src/test/resources/TargetWebsite/**` | The website under test — never touch |
| `src/Assist/GENERATE_TESTS.md` | AI prompt — not generated |
| `src/Assist/RESET.md` | This file — not generated |
| `src/Assist/TEAM_ASSIGNMENTS.md` | Assignment table — not generated |
| `src/Assist/ELEMENT_ID_REGISTRY.md` | Element ID reference — not generated |
| `src/Assist/Guide.md` | Project guide — not generated |
| `src/Assist/Rearranged_testcases.xlsx` | Master test case sheet — not generated |
| `src/Assist/selenium/base/BaseTest.java` | Sample base class — not generated |
| `src/Assist/selenium/pages/SearchResultsPage.java` | **Whitelisted sample** — not generated |
| `src/Assist/selenium/tests/SearchResultsTest.java` | **Whitelisted sample** — not generated |
| `src/Assist/features/SearchResults.feature` | **Whitelisted sample** — not generated |
| `pom.xml` | Maven build config — not generated |
| `.gitignore` | Git config — not generated |

---

## Steps

1. List all files matching the delete patterns above and confirm with the user:
   > *"I will delete the following N generated files. Shall I proceed?"*
   > Show the full list grouped by type (features / pages / tests).

2. On confirmation, delete all listed files — nothing else.

3. Verify that all **preserve** targets still exist after deletion.

4. Report back:
   > *"Reset complete. Deleted N files. The following preserved files are intact: [list]."*

---

## What is NOT reset

- `pom.xml` — unchanged
- `src/test/test.xml` — unchanged (still discovers `tests.*` via package scan)
- `src/Assist/` docs — unchanged
- `src/test/resources/TargetWebsite/` — unchanged
- Git history — this reset only removes working-tree files; it does not `git reset`
