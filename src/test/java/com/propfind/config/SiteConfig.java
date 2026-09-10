package com.propfind.config;

import java.io.File;

/**
 * Single source of truth for the base URL of the site under test.
 *
 * Resolution order:
 *   1. System property  {@code -DbaseUrl=...}  (highest priority — CI / remote)
 *   2. Bundled {@code propfind-website/} folder at the project root  (default — local file://)
 *
 * Previously each TestContext class duplicated this logic with a shared mutable
 * {@code static String baseUrl} field.  That has been replaced by this class.
 */
public final class SiteConfig {

    private SiteConfig() {}

    /**
     * Returns the base URL, always ending with {@code /}.
     * Safe to call from multiple threads — no mutable state.
     */
    public static String resolveBaseUrl() {
        String override = System.getProperty("baseUrl");
        if (override != null && !override.isBlank()) {
            return override.endsWith("/") ? override : override + "/";
        }
        return new File("propfind-website").toURI().toString();
    }
}
