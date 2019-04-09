package org.entando.entando.plugins.dashboard.web.iot;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.DashboardConfigController;
import org.entando.entando.web.common.handlers.RestExceptionHandler;
import org.entando.entando.web.common.model.ErrorRestResponse;
import org.entando.entando.web.common.model.RestError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.ws.rs.Produces;

@ControllerAdvice(basePackageClasses = {DashboardConfigController.class, ConnectorController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class IoTExceptionHandler {

  private final Logger logger = LoggerFactory.getLogger(getClass());


  @ExceptionHandler(value = ApiResourceNotAvailableException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @Produces("application/json")
  @ResponseBody
  public ErrorRestResponse processApiResourceNotAvailableException(ApiResourceNotAvailableException ex) {
    logger.debug("Handling {} error", ex.getClass().getSimpleName());
    RestError error = new RestError(ex.getErrorCode(), ex.getMessage());
    return new ErrorRestResponse(error);
  }

  @ExceptionHandler(value = ApsSystemException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @Produces("application/json")
  @ResponseBody
  public ErrorRestResponse processApiResourceNotAvailableException(ApsSystemException ex) {
    logger.debug("Handling {} error", ex.getClass().getSimpleName());
    RestError error = new RestError("500", ex.getMessage());
    return new ErrorRestResponse(error);
  }

  
  
  
}
