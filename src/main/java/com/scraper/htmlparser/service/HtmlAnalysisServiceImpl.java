package com.scraper.htmlparser.service;

import com.scraper.htmlparser.domain.Hyperlink;
import com.scraper.htmlparser.domain.WebsiteData;
import com.scraper.htmlparser.exception.ApplicationException;
import com.scraper.htmlparser.exception.ErrorResponseEnum;
import com.scraper.htmlparser.util.AppConstants;
import com.scraper.htmlparser.validator.HyperlinkValidator;
import com.scraper.htmlparser.validator.LinkValidator;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
public class HtmlAnalysisServiceImpl implements HtmlAnalysisService {

  private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
      .getLogger(HtmlAnalysisServiceImpl.class);

  @Override
  public WebsiteData analyseWebsite(final String webUrl) {
    Assert.notNull(webUrl, "Website URL can not be null.");
    Document document = getDocument(webUrl);

    String title = document.title();
    WebsiteData websiteData = new WebsiteData();
    //websiteData.setHyperlinks(processLinks(webUrl, document));
    websiteData.setHyperlinks(fetchAndGroupHyperlinks(webUrl, document));
    websiteData.setUrl(webUrl);
    websiteData.setTitle(title);
    websiteData.setVersion(getHtmlVersion(document));
    websiteData.setHeadings(fetchAndGroupHeadings(document));
    websiteData.setLoginPage(isLoginPage(document));
    return websiteData;
  }

  /**
   * Get the jsoup document object to analyse the content.
   */
  private Document getDocument(final String webUrl) {
    try {
      return Jsoup.connect(webUrl).get();
    } catch (MalformedURLException mfe) {
      LOG.error("The URL {} is malformed. Error is {}", webUrl, mfe.getMessage());
      throw new ApplicationException(ErrorResponseEnum.MALFORMED_URL);
    } catch (IOException ioe) {
      LOG.error("The URL {} is not reachable. Error is {}", webUrl, ioe.getMessage());
      throw new ApplicationException(ErrorResponseEnum.INVALID_URL);
    }
  }

  /**
   * This method determines if the website has login box or fields. Logic - Search for text fields
   * with password type. If there is password field, there is possibility that it has login box. I
   * could have checked for username field, or submit, login buttons as well. But it is not always
   * possible that field names will be similar to what we guessed.
   */
  private boolean isLoginPage(final Document document) {
    Elements links = document.select("input[type=password]");
    return (links != null && links.isEmpty());
  }

  /**
   * Fetch headings and group them accordingly.
   */
  private Map<String, List<String>> fetchAndGroupHeadings(final Document document) {
    Elements hTags = document.select("h1, h2, h3, h4, h5, h6");

    return hTags.parallelStream()
        .collect(Collectors
            .groupingBy(Element::tagName, Collectors.mapping(Element::text, Collectors.toList())));
  }

  /**
   * This method processes the hyperlinks and uses completable futures to process in async way.
   */
  private Map<String, List<Hyperlink>> fetchAndGroupHyperlinks(final String webUrl,
      final Document document) {
    long start = System.nanoTime();
    Elements links = document.select("a");

    Set<String> uniqueLinks = new HashSet<>();

    // create list of hyperlink validators and submit to validate
    List<CompletableFuture<Hyperlink>> hyperlinkValidators = links.parallelStream()
        .map(element -> element.attr("abs:href"))
        .filter(link -> !StringUtils.isEmpty(link))
        .filter(link -> uniqueLinks.add(link))
        .map(s -> new HyperlinkValidator(s).validate())
        .collect(Collectors.toList());

    // process hyperlink here, determine the url type and group them accordingly.
    Map<String, List<Hyperlink>> hyperlinks = hyperlinkValidators.parallelStream()
        .map(CompletableFuture::join)
        .peek(hyperlink -> hyperlink.setType(hyperlinkType(webUrl, hyperlink.getUrl())))
        .collect(Collectors.groupingBy(Hyperlink::getType));

    long duration = (System.nanoTime() - start) / 1_000_000;
    LOG.info("Processed {} links in {} millis", hyperlinkValidators.size(), duration);
    return hyperlinks;
  }

  /**
   * Method to get the html version.
   */
  private String getHtmlVersion(final Document document) {
    List<Node> nodes = document.childNodes();
    return nodes.stream().filter(node -> node instanceof DocumentType)
        .map(this::generateHtmlVersion).collect(Collectors.joining());
  }

  private String generateHtmlVersion(final Node node) {
    DocumentType documentType = (DocumentType) node;
    final String docType = documentType.toString();

    if ("<!DOCTYPE html>".equalsIgnoreCase(docType)) {
      return AppConstants.HTML_5;
    }

    return docType.substring(docType.indexOf("HTML"), docType.indexOf("Transitional")).trim();
  }

  /**
   * Determine the hyperlink type. Either internal or external.
   */
  private String hyperlinkType(String baseUrl, String hyperlink) {
    try {
      return new URI(baseUrl).getHost().equalsIgnoreCase(new URI(hyperlink).getHost()) ? "internal"
          : "external";
    } catch (URISyntaxException e) {
      return "unknown";
    }
  }

  /**
   * This method processes all the hyperlinks. This uses the executor services to process the links
   * in different threads. This method is not in use.
   */
  private Map<String, List<Hyperlink>> processLinks(final String webUrl, final Document document) {
    long start = System.nanoTime();
    Elements links = document.select("a");

    ExecutorService executor = Executors.newFixedThreadPool(Math.min(links.size(), 10));

    List<Future<Hyperlink>> futures = new ArrayList<>();
    for (Element link : links) {

      String hyperlink = link.attr("abs:href");

      if (!StringUtils.isEmpty(hyperlink)) {
        Future<Hyperlink> future = executor.submit(new Callable<Hyperlink>() {
          @Override
          public Hyperlink call() throws Exception {
            return new LinkValidator(hyperlink).call();
          }
        });

        futures.add(future);
      }
    }

    List<Hyperlink> hyperlinks = new ArrayList<>();

    for (Future future : futures) {
      try {
        Hyperlink link = (Hyperlink) future.get();
        link.setType(hyperlinkType(webUrl, link.getUrl()));
        hyperlinks.add(link);
      } catch (InterruptedException | ExecutionException e) {
        LOG.error(
            "Exception occurred while generating mails by notification type", e);
      }
    }

    Map<String, List<Hyperlink>> linkGroup = hyperlinks.parallelStream()
        .collect(Collectors.groupingBy(Hyperlink::getType));

    executor.shutdown();

    long duration = (System.nanoTime() - start) / 1_000_000;
    LOG.info("Processed {} links in {} millis", links.size(), duration);
    return linkGroup;
  }
}
