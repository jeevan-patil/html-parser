package com.scraper.htmlparser.validator;

import com.scraper.htmlparser.domain.Hyperlink;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * Hyperlink validator. This class uses CompletableFuture to process the  hyperlinks.
 */
public class HyperlinkValidator {

  private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
      .getLogger(HyperlinkValidator.class);

  private static final Set<Integer> URL_REDIRECT_STATUS_CODES = new HashSet<>(
      Arrays.asList(200, 301, 302));
  private String url;

  public HyperlinkValidator(String url) {
    this.url = url;
  }

  /**
   * This method validates the url and collects data related to redirection.
   *
   * @return {@code {@link CompletableFuture}} Completable future of {@code {@link Hyperlink}}
   */
  public CompletableFuture<Hyperlink> validate() {
    CompletableFuture<Hyperlink> completableFuture = new CompletableFuture<>();

    Executors.newCachedThreadPool().submit(() -> {
      try {
        LOG.info("Validate URL {}", url);
        Response response = Jsoup.connect(url).followRedirects(false).execute();
        boolean reachable = URL_REDIRECT_STATUS_CODES.contains(response.statusCode());
        completableFuture.complete(new Hyperlink(url, reachable, null));
      } catch (Exception e) {
        LOG.error("Error in reaching the website {}", url);
        completableFuture.complete(new Hyperlink(url, false, e.getMessage()));
      }
    });

    return completableFuture;
  }

}
