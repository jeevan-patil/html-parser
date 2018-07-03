package com.scraper.htmlparser.exception;

public class ApplicationException extends RuntimeException {

  private ErrorResponseEnum errorResponse;

  public ApplicationException(ErrorResponseEnum errorResponse) {
    super(errorResponse.getErrorText());
    this.errorResponse = errorResponse;
  }

  public ApplicationException(String errorMessage) {
    super(errorMessage);
  }

  public ApplicationException(ErrorResponseEnum errorResponse, Throwable throwable) {
    super(throwable);
    this.errorResponse = errorResponse;
  }

  public ErrorResponseEnum getErrorResponse() {
    return errorResponse;
  }
}
