package com.scraper.htmlparser.validator;

import com.scraper.htmlparser.domain.Hyperlink;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * Hyperlink validator. This object is submitted to executor service for async process.
 */
public class LinkValidator implements Callable<Hyperlink> {

  private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
      .getLogger(LinkValidator.class);

  private static final Set<Integer> URL_REDIRECT_STATUS_CODES = new HashSet<>(
      Arrays.asList(200, 301, 302));
  private String url;

  public LinkValidator(String url) {
    this.url = url;
  }

  @Override
  public Hyperlink call() {
    try {
      LOG.info("Validate URL {}", url);
      Response response = Jsoup.connect(url).followRedirects(false).execute();
      boolean reachable = URL_REDIRECT_STATUS_CODES.contains(response.statusCode());
      return new Hyperlink(url, reachable, null);
    } catch (Exception e) {
      LOG.error("Error in reaching the website {}", url);
      return new Hyperlink(url, false, e.getMessage());
    }
  }
}
