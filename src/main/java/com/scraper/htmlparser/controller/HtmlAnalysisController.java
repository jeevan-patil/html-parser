package com.scraper.htmlparser.controller;

import com.scraper.htmlparser.domain.WebsiteData;
import com.scraper.htmlparser.service.HtmlAnalysisService;
import com.scraper.htmlparser.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = AppConstants.HTML_ANALYSIS_API_ENDPOINT)
@Api(value = "htmlanalysis", description = "Operations pertaining to analysis of HTML content")
public class HtmlAnalysisController {

  private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
      .getLogger(HtmlAnalysisController.class);

  @Autowired
  private HtmlAnalysisService htmlAnalysisService;

  @ApiOperation(value = "Analyse the HTML content of provided website.",
      notes = "API to analyse the HTML content of website and returns JSON of collected data.",
      response = WebsiteData.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Analysis of the HTML content is successful.")
  })
  @GetMapping(value = "analyse", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<WebsiteData> analyseWebsite(
      @RequestParam(value = "url") final String webUrl) {
    LOG.info("Incoming request to analyse - {}", webUrl);
    return new ResponseEntity<>(htmlAnalysisService.analyseWebsite(webUrl), HttpStatus.OK);
  }
}
