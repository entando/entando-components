package org.entando.entando.plugins.dashboard.aps.system.services.iot;

import org.entando.entando.plugins.dashboard.web.iot.IoTExceptionHandler;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

  //region exception handlers
  public static List<HandlerExceptionResolver> createHandlerExceptionResolver() {
    List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList();
    ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = createExceptionResolver();
    handlerExceptionResolvers.add(exceptionHandlerExceptionResolver);
    return handlerExceptionResolvers;
  }

  public static ExceptionHandlerExceptionResolver createExceptionResolver() {
    final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("rest/messages");
    messageSource.setUseCodeAsDefaultMessage(true);
    ExceptionHandlerExceptionResolver exceptionResolver = new ExceptionHandlerExceptionResolver() {
      protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception) {
        Method method = (new ExceptionHandlerMethodResolver(IoTExceptionHandler.class)).resolveMethod(exception);
        IoTExceptionHandler validationHandler = new IoTExceptionHandler();
        validationHandler.setMessageSource(messageSource);
        return new ServletInvocableHandlerMethod(validationHandler, method);
      }
    };
    exceptionResolver.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    exceptionResolver.afterPropertiesSet();
    return exceptionResolver;
  }

  //endregion


}
