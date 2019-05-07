package org.entando.entando.plugins.dashboard.aps.system.services.iot.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvalidFieldException extends RuntimeException{

//  @Value("invalid.field.exception")
  String message;

  public InvalidFieldException(String message) {
    super(message);
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}