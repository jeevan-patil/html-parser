package com.scraper.htmlparser.exception;

import org.springframework.http.HttpStatus;

public enum ErrorResponseEnum {
  GENERAL_ERROR(100, "An exception occurred while processing your request.",
      HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_URL(101, "Invalid url, could not reach it.", HttpStatus.NOT_FOUND),
  MALFORMED_URL(102, "The url is malformed. May be unknown protocol.", HttpStatus.BAD_REQUEST);

  private int code;
  private String errorText;
  private HttpStatus httpStatus;

  ErrorResponseEnum(int code, String errorText, HttpStatus httpStatus) {
    this.code = code;
    this.errorText = errorText;
    this.httpStatus = httpStatus;
  }

  public int getCode() {
    return code;
  }

  public String getErrorText() {
    return errorText;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}