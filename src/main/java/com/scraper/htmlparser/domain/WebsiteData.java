package com.scraper.htmlparser.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class WebsiteData {

  private String url;
  private String version;
  private String title;
  private Map<String, List<String>> headings;
  private Map<String, List<Hyperlink>> hyperlinks;
  private boolean loginPage;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Map<String, List<String>> getHeadings() {
    return headings;
  }

  public void setHeadings(Map<String, List<String>> headings) {
    this.headings = headings;
  }

  public Map<String, List<Hyperlink>> getHyperlinks() {
    return hyperlinks;
  }

  public void setHyperlinks(
      Map<String, List<Hyperlink>> hyperlinks) {
    this.hyperlinks = hyperlinks;
  }

  public boolean isLoginPage() {
    return loginPage;
  }

  public void setLoginPage(boolean loginPage) {
    this.loginPage = loginPage;
  }
}
