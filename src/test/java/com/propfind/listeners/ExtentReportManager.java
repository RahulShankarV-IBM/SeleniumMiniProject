package com.propfind.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Singleton holder for the shared {@link ExtentReports} instance and the
 * per-thread {@link ExtentTest} node.
 *
 * <ul>
 *   <li>{@link ExtentReportListener} calls {@link #init()} once at suite start
 *       and {@link #flush()} once at suite finish.</li>
 *   <li>{@link CucumberExtentHooks} calls {@link #startTest} / {@link #passTest}
 *       / {@link #failTest} so every Cucumber scenario gets a properly named,
 *       tagged node instead of the generic {@code runScenario} entry.</li>
 * </ul>
 */
public final class ExtentReportManager {

    private ExtentReportManager() {}

    private static ExtentReports extent;

    /** One named node per thread — safe for parallel Cucumber scenarios. */
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    /** Initialises the report. No-op if already initialised. */
    public static synchronized void init() {
        if (extent != null) return;

        String timestamp  = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String reportDir  = "target/extent-reports";
        String reportFile = reportDir + "/ExtentReport_" + timestamp + ".html";

        new File(reportDir).mkdirs();

        ExtentSparkReporter spark = new ExtentSparkReporter(reportFile);
        spark.config().setDocumentTitle("PropFind – Test Report");
        spark.config().setReportName("PropFind Selenium Suite");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setEncoding("UTF-8");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Project",     "PropFind");
        extent.setSystemInfo("Environment", "Local file://");
        extent.setSystemInfo("Browser",     "Chrome");
        extent.setSystemInfo("Java",        System.getProperty("java.version"));
    }

    /** Writes the report to disk. Called at suite finish. */
    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    /** Returns the underlying {@link ExtentReports} instance (may be null before init). */
    public static ExtentReports getExtent() {
        return extent;
    }

    // ── Per-test node API (used by both listener and Cucumber hooks) ──────────

    /**
     * Creates a new named test node for the current thread.
     *
     * @param name        Display name shown in the report sidebar
     * @param description Optional description shown in the detail panel
     * @param categories  Zero or more category/tag strings (e.g. "TC01", "High")
     */
    public static void startTest(String name, String description, String... categories) {
        if (extent == null) init();
        ExtentTest node = extent.createTest(name, description);
        for (String cat : categories) {
            if (cat != null && !cat.isBlank()) {
                node.assignCategory(cat);
            }
        }
        currentTest.set(node);
    }

    /** Logs a PASS result on the current thread's node. */
    public static void passTest(String message) {
        ExtentTest node = currentTest.get();
        if (node != null) {
            node.pass(message);
        }
    }

    /** Logs a FAIL result on the current thread's node. */
    public static void failTest(Throwable t) {
        ExtentTest node = currentTest.get();
        if (node != null) {
            node.fail(t);
        }
    }

    /** Logs a FAIL with a plain message on the current thread's node. */
    public static void failTest(String message) {
        ExtentTest node = currentTest.get();
        if (node != null) {
            node.fail(message);
        }
    }

    /** Logs a SKIP result on the current thread's node. */
    public static void skipTest(String message) {
        ExtentTest node = currentTest.get();
        if (node != null) {
            node.skip(message);
        }
    }

    /**
     * Attaches a Base64 screenshot to the current thread's node.
     * Safe to call even if no node has been started (silently ignored).
     */
    public static void attachScreenshot(String base64Png, String title) {
        ExtentTest node = currentTest.get();
        if (node != null) {
            try {
                node.addScreenCaptureFromBase64String(base64Png, title);
            } catch (Exception ignored) {
                // Don't let a screenshot error fail the report
            }
        }
    }

    /** Returns the current thread's active {@link ExtentTest} node, or null. */
    public static ExtentTest getCurrentTest() {
        return currentTest.get();
    }
}
