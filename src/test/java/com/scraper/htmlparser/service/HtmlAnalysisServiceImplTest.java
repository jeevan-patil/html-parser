package com.scraper.htmlparser.service;

import static org.junit.Assert.*;

import com.scraper.htmlparser.domain.WebsiteData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner.StrictStubs;

@RunWith(StrictStubs.class)
public class HtmlAnalysisServiceImplTest {

  @InjectMocks
  private HtmlAnalysisServiceImpl cut;

  @Test(expected = IllegalArgumentException.class)
  public void analyseWebsiteFailedEmptyUrl() {
    final String webUrl = "";

    cut.analyseWebsite(webUrl);
  }

  @Test
  public void analyseWebsite() {
    final String webUrl = "https://www.quora.com/";
    WebsiteData websiteData = cut.analyseWebsite(webUrl);

    assertNotNull(websiteData);
    assertTrue(websiteData.getTitle().contains("Quora"));
    assertTrue(websiteData.getHeadings().size() > 0);
    assertTrue(websiteData.isLoginPage());
    assertTrue(websiteData.getHyperlinks().size() > 0);
  }
}
