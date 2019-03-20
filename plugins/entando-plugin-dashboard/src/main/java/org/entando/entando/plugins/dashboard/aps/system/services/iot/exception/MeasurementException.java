package org.entando.entando.plugins.dashboard.aps.system.services.iot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MeasurementException extends RuntimeException{

  public MeasurementException(String message) {
    super(message);
  }
}
