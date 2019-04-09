package org.entando.entando.plugins.dashboard.aps.system.services.iot.exception;

import org.springframework.stereotype.Component;

@Component
public class ApiResourceNotAvailableException extends RuntimeException{

  String errorCode;
  String message;

  public ApiResourceNotAvailableException(String errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
    this.message = message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}