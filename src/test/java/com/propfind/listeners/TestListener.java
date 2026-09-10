package com.propfind.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TestNG ITestListener that builds a self-contained HTML report.
 * Registered in test.xml alongside ExtentReportListener.
 * Output: target/propfind-report.html (created/overwritten after each suite run)
 */
public class TestListener implements ITestListener {

    // ── Internal record ───────────────────────────────────────────────────────

    private static class Result {
        final String tcId;
        final String methodName;
        final String status;       // PASS | FAIL | SKIP
        final long   durationMs;
        final String errorMessage;
        final String stackTrace;

        Result(String tcId, String methodName, String status,
               long durationMs, String errorMessage, String stackTrace) {
            this.tcId         = tcId;
            this.methodName   = methodName;
            this.status       = status;
            this.durationMs   = durationMs;
            this.errorMessage = errorMessage;
            this.stackTrace   = stackTrace;
        }
    }

    // ── State ─────────────────────────────────────────────────────────────────

    private final List<Result> results = new ArrayList<>();
    private String suiteName = "PropFind";
    private long   suiteStart;

    // ── ITestListener callbacks ───────────────────────────────────────────────

    @Override
    public void onStart(ITestContext context) {
        suiteName  = context.getSuite().getName();
        suiteStart = System.currentTimeMillis();
        results.clear();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        results.add(buildResult(result, "PASS", null, null));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable t = result.getThrowable();
        String msg   = t != null ? t.getMessage()  : "Unknown error";
        String trace = t != null ? stackTrace(t)   : "";
        results.add(buildResult(result, "FAIL", msg, trace));
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        results.add(buildResult(result, "SKIP", "Test skipped", ""));
    }

    @Override
    public void onFinish(ITestContext context) {
        writeReport();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Result buildResult(ITestResult r, String status, String msg, String trace) {
        String tcId  = r.getTestName() != null ? r.getTestName() : r.getMethod().getMethodName();
        long   dur   = r.getEndMillis() - r.getStartMillis();
        return new Result(tcId, r.getMethod().getMethodName(), status, dur, msg, trace);
    }

    private String stackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private void writeReport() {
        long totalMs = System.currentTimeMillis() - suiteStart;
        long passed  = results.stream().filter(r -> "PASS".equals(r.status)).count();
        long failed  = results.stream().filter(r -> "FAIL".equals(r.status)).count();
        long skipped = results.stream().filter(r -> "SKIP".equals(r.status)).count();
        String runTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss"));

        StringBuilder rows = new StringBuilder();
        for (Result r : results) {
            String badgeClass = switch (r.status) {
                case "PASS" -> "pass";
                case "FAIL" -> "fail";
                default     -> "skip";
            };
            String errorCell = "";
            if (r.errorMessage != null && !r.errorMessage.isBlank()) {
                errorCell = "<details><summary>" + escape(r.errorMessage) + "</summary>"
                        + "<pre>" + escape(r.stackTrace) + "</pre></details>";
            }
            rows.append("<tr>")
                .append("<td>").append(escape(r.tcId)).append("</td>")
                .append("<td>").append(escape(r.methodName)).append("</td>")
                .append("<td><span class=\"badge ").append(badgeClass).append("\">")
                    .append(r.status).append("</span></td>")
                .append("<td>").append(r.durationMs).append(" ms</td>")
                .append("<td>").append(errorCell).append("</td>")
                .append("</tr>\n");
        }

        String html = buildHtml(runTime, totalMs, passed, failed, skipped, rows.toString());

        try {
            Path outDir = Path.of("target");
            Files.createDirectories(outDir);
            File out = outDir.resolve("propfind-report.html").toFile();
            try (FileWriter fw = new FileWriter(out)) {
                fw.write(html);
            }
            System.out.println("\n==============================================");
            System.out.println("  PropFind HTML Report → " + out.getAbsolutePath());
            System.out.println("==============================================\n");
        } catch (IOException e) {
            System.err.println("[TestListener] Failed to write HTML report: " + e.getMessage());
        }
    }

    private String buildHtml(String runTime, long totalMs,
                             long passed, long failed, long skipped, String rows) {
        return "<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\"/>" +
               "<title>PropFind Test Report</title>" +
               "<style>" +
               "*{box-sizing:border-box;margin:0;padding:0}" +
               "body{font-family:-apple-system,'Segoe UI',system-ui,sans-serif;font-size:14px;" +
               "     line-height:1.6;background:#f7f8fa;color:#1f2328}" +
               "header{background:#1a56db;color:#fff;padding:1.25rem 2rem;display:flex;" +
               "       justify-content:space-between;align-items:center}" +
               "header h1{font-size:1.25rem;font-weight:700}" +
               "header small{opacity:.8;font-size:.82rem}" +
               ".summary{display:flex;gap:1rem;padding:1.25rem 2rem;background:#fff;" +
               "         border-bottom:1px solid #e5e7eb;flex-wrap:wrap}" +
               ".card{border-radius:8px;padding:.65rem 1.2rem;font-size:.88rem;font-weight:600;" +
               "      min-width:110px;text-align:center}" +
               ".card span{display:block;font-size:1.7rem;font-weight:800;line-height:1}" +
               ".c-pass{background:#d1fae5;color:#065f46}" +
               ".c-fail{background:#fee2e2;color:#991b1b}" +
               ".c-skip{background:#fef9c3;color:#854d0e}" +
               ".c-total{background:#e0e7ff;color:#3730a3}" +
               ".c-time{background:#f1f5f9;color:#475569}" +
               ".wrap{padding:1.5rem 2rem}" +
               "table{width:100%;border-collapse:collapse;background:#fff;" +
               "      border-radius:8px;overflow:hidden;box-shadow:0 1px 3px rgba(0,0,0,.08)}" +
               "th{background:#1a56db;color:#fff;padding:10px 14px;text-align:left;" +
               "   font-size:.82rem;text-transform:uppercase;letter-spacing:.5px}" +
               "td{padding:9px 14px;border-bottom:1px solid #e5e7eb;vertical-align:top;font-size:.87rem}" +
               "tr:last-child td{border-bottom:none}" +
               "tr:hover td{background:#f0f4ff}" +
               ".badge{display:inline-block;padding:2px 10px;border-radius:20px;font-size:.78rem;font-weight:700}" +
               ".pass{background:#d1fae5;color:#065f46}" +
               ".fail{background:#fee2e2;color:#991b1b}" +
               ".skip{background:#fef9c3;color:#854d0e}" +
               "details summary{cursor:pointer;color:#991b1b;font-size:.84rem;white-space:pre-wrap;word-break:break-word}" +
               "details pre{margin-top:8px;background:#f8f8f8;border:1px solid #e5e7eb;border-radius:6px;" +
               "            padding:10px;font-size:.78rem;white-space:pre-wrap;word-break:break-word;" +
               "            max-height:260px;overflow:auto;color:#374151}" +
               "footer{text-align:center;padding:1.5rem;font-size:.78rem;color:#57606a;" +
               "       border-top:1px solid #e5e7eb;margin-top:2rem}" +
               "</style></head><body>" +
               "<header><h1>PropFind — Selenium Test Report</h1>" +
               "<small>Run: " + escape(runTime) + "</small></header>" +
               "<div class=\"summary\">" +
               "<div class=\"card c-total\"><span>" + results.size() + "</span>Total</div>" +
               "<div class=\"card c-pass\"><span>" + passed  + "</span>Passed</div>" +
               "<div class=\"card c-fail\"><span>" + failed  + "</span>Failed</div>" +
               "<div class=\"card c-skip\"><span>" + skipped + "</span>Skipped</div>" +
               "<div class=\"card c-time\"><span>" + totalMs + " ms</span>Duration</div>" +
               "</div><div class=\"wrap\">" +
               "<table><thead><tr>" +
               "<th>TC ID</th><th>Method</th><th>Status</th><th>Duration</th><th>Error</th>" +
               "</tr></thead><tbody>" + rows + "</tbody></table></div>" +
               "<footer>Generated by TestListener &nbsp;·&nbsp; PropFind Selenium Suite</footer>" +
               "</body></html>";
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
