package com.scraper.htmlparser.service;

import com.scraper.htmlparser.domain.WebsiteData;

public interface HtmlAnalysisService {

  /**
   * This method analyses the website HTML content and sends back the website data.
   *
   * @param webUrl Website URL.
   * @return {@code {@link WebsiteData}} Website data.
   */
  WebsiteData analyseWebsite(final String webUrl);

}
