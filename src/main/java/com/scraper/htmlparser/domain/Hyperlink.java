package com.scraper.htmlparser.domain;

public class Hyperlink {

  private String url;
  private Boolean reachable;

  // remark could be the information in case link is unreachable
  private String remark;
  private String type;

  public Hyperlink(String url, Boolean reachable, String remark) {
    this.url = url;
    this.reachable = reachable;
    this.remark = remark;
  }

  public String getUrl() {
    return url;
  }

  public Boolean getReachable() {
    return reachable;
  }

  public String getRemark() {
    return remark;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Hyperlink{" +
        "url='" + url + '\'' +
        ", reachable=" + reachable +
        ", remark='" + remark + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
