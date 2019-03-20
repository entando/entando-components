package org.entando.entando.plugins.dashboard.aps.system.services.iot.utils;

public class IoTConstants {
  
  public static final String NO_SERVER_INSTANCE = "No server Instance";
  
  public static final int CONNECTION_TIMEOUT = 500;

  public static final String JSON_FIELD_SEPARATOR = "->";
  
  public static final String MEASUREMENT_TYPE_EXCEPTION = "Invalid Measurement Type";
  //KKA CONSTANTS
  public static final String KAA_URL_BASE_PATH = "/rest/api";
  public static final String KAA_SERVER_TYPE = "KAA";
  
  
  //SITEWHERE CONSTANTS
  public static final String SITEWHERE_URL_BASE_PATH = "/api";
  public static final String SITEWHERE_TENANT = "X-Sitewhere-Tenant";
  public static final String SITEWHERE_SERVER_TYPE = "SITEWHERE";
  public static final String ONE_RESULT_PAGINATION = "?take=1&skip=0&page=1&pageSize=1";
  public static final String CUSTOM_RESULT_PAGINATION = "?take=%s&pageSize=%s";
}
