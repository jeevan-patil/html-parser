package com.scraper.htmlparser.util;

/**
 * Application constants reside here.
 */
public final class AppConstants {

  public static final String API_VERSION = "v1";
  public static final String ENDPOINTS_FOR_SWAGGER = "/" + API_VERSION + ".*";
  public static final String HTML_ANALYSIS_API_ENDPOINT = "/" + API_VERSION + "/html/";

  public static final String HTML_5 = "HTML5";

  private AppConstants() {
  }
}
