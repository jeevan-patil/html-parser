package com.scraper.htmlparser.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.scraper.htmlparser.domain.WebsiteData;
import com.scraper.htmlparser.service.HtmlAnalysisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner.StrictStubs;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(StrictStubs.class)
public class HtmlAnalysisControllerTest {

  @InjectMocks
  private HtmlAnalysisController cut;

  @Mock
  private HtmlAnalysisService mockHtmlAnalysisService;

  @Test
  public void analyseWebsite() {
    final String webUrl = "https://www.quora.com/";
    WebsiteData websiteData = new WebsiteData();
    websiteData.setLoginPage(false);
    websiteData.setUrl(webUrl);

    ResponseEntity<WebsiteData> responseEntity = new ResponseEntity<WebsiteData>(websiteData,
        HttpStatus.OK);

    when(mockHtmlAnalysisService.analyseWebsite(webUrl)).thenReturn(websiteData);

    ResponseEntity<WebsiteData> response = cut.analyseWebsite(webUrl);

    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertEquals(webUrl, response.getBody().getUrl());

    verify(mockHtmlAnalysisService).analyseWebsite(webUrl);
    verifyNoMoreInteractions(mockHtmlAnalysisService);
  }

}
