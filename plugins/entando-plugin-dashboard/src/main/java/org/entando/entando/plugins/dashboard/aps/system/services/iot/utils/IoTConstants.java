package org.entando.entando.plugins.dashboard.aps.system.services.iot.utils;

public class IoTConstants {
  
  public static final String NO_SERVER_INSTANCE = "No server Instance";
  
  public static final int CONNECTION_TIMEOUT = 500;

  public static final String JSON_FIELD_SEPARATOR = "->";
  
  public static final String MEASUREMENT_TYPE_EXCEPTION = "Invalid MeasurementPayload Type";

  public static final int MINIMUM_TIMEOUT_CONNECTION = 1000;


  //EXCEPTIONS AND LOGGER
  public static final String API_EX_DASHBOARD_DATASOURCE = "%s error dashboard id %s, datasource code %s, can't communicate to Api Service";
  public static final String LOG_API_EX_DASHBOARD_DATASOURCE = "{} obtained {} calling dashboard code {}, datasource code {}";
  public static final String API_EX_DASHBOARD= "%s error dashboard id %s, can't communicate to Api Service";
  public static final String LOG_API_EX_DASHBOARD = "{} obtained {} calling dashboard id {}";
  public static final String UNABLE_TO_PARSE_DASHBOARD = "unable to getJsonPaths dashboard";
  public static final String UNABLE_TO_PARSE_DATASOURCE = "unable to getJsonPaths datasource";
  public static final String EX_INACTIVE_SERVER = "Server %s is not active";
  public static final String DATASOURCE_TYPE_GENERIC = "GENERIC";
  public static final String DATASOURCE_TYPE_GEODATA = "GEODATA";
  public static final String DATASOURCE_STATUS_ONLINE = "online";
  public static final String DATASOURCE_STATUS_OFFLINE = "offline";
}
