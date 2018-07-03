package com.scraper.htmlparser.domain;

public class ErrorResponse {

  private int code;
  private String errorMessage;

  public ErrorResponse(int code, String errorMessage) {
    this.code = code;
    this.errorMessage = errorMessage;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    StringBuilder error = new StringBuilder();
    error.append("{ code : " + code);
    error.append(", errorMessage : " + errorMessage);
    error.append(" }");
    return error.toString();
  }
}
