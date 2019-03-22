package org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.service;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.KAA_SERVER_TYPE;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.KAA_URL_BASE_PATH;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.getHeaders;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.getObjectFromJson;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto.DashboardKaaDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto.KaaApplicationConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.LogAppender;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorIot;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementTemplateService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Service
public class KaaConnectorService implements IKaaConnectorService, IConnectorIot {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  IMeasurementConfigService measurementConfigService;

  IMeasurementTemplateService measurementTemplateService;

  ConnectorFactory connectorFactory;
  
  @Autowired
  public KaaConnectorService(
      IMeasurementConfigService MeasurementConfigService,
      IMeasurementTemplateService iMeasurementTemplateService,
      ConnectorFactory connectorFactory) {
    this.measurementConfigService = MeasurementConfigService;
    this.measurementTemplateService = iMeasurementTemplateService;
    this.connectorFactory = connectorFactory;
  }

  @Override
  public boolean pingDevice(IDashboardDatasourceDto dashboardDatasourceDto) {
    
    KaaApplicationConfigDto kaaConnection = connectorFactory.getDashboardDatasource(dashboardDatasourceDto.getServerType()).getDatasource();
    
    logger.info("{} pingDevice {}", this.getClass().getSimpleName(),
        kaaConnection.getDatasourceCode());

    String url = StringUtils.join(dashboardDatasourceDto.getDashboardConfigDto().getServerURI(),
        KAA_URL_BASE_PATH, "/application/", kaaConnection.getDatasourceCode());

    HttpEntity httpEntity = new HttpEntity(
        getHeaders(dashboardDatasourceDto.getDashboardConfigDto()));
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> result = restTemplate
        .exchange(url, HttpMethod.GET, httpEntity, String.class);
    DashboardKaaDatasourceDto device = getObjectFromJson(result.getBody(),
        new TypeToken<DashboardKaaDatasourceDto>() {
        }.getType(), DashboardKaaDatasourceDto.class);
    boolean res = device != null;

    logger.info("{} pingDevice {} results {}", this.getClass().getSimpleName(),
        kaaConnection.getDatasourceCode(), res);

    return res;
  }

  @Override
  public List<? extends AbstractDashboardDatasourceDto> getAllDevices(
      DashboardConfigDto dashboardConfigDto) {

    logger.info("{} getAllDevices {}", this.getClass().getSimpleName(),
        dashboardConfigDto.getServerURI());

    String url = StringUtils
        .join(dashboardConfigDto.getServerURI(), KAA_URL_BASE_PATH, "/applications");
    HttpEntity entity = new HttpEntity(getHeaders(dashboardConfigDto));
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> result = restTemplate
        .exchange(url, HttpMethod.GET, entity, String.class);
    List<DashboardKaaDatasourceDto> devices = getObjectFromJson(result.getBody(),
        new TypeToken<List<DashboardKaaDatasourceDto>>() {
        }.getType(), ArrayList.class);

    logger.info("{} getAllDevices {} results {}", this.getClass().getSimpleName(),
        dashboardConfigDto.getServerURI(), devices != null);

    return devices;
  }

 

  @Override
  public void saveMeasurementTemplate(
		  IDashboardDatasourceDto dashboardKaaDataSource) throws ApsSystemException {
	  
    DashboardKaaDatasourceDto kaaConn = (DashboardKaaDatasourceDto) connectorFactory.getDashboardDatasource(dashboardKaaDataSource.getServerType());

    String loggerId = kaaConn.getKaaDatasourceConfigDto().getLoggerId();

    String url = StringUtils
        .join(dashboardKaaDataSource.getDashboardUrl(), KAA_URL_BASE_PATH,
            "/CTL/getSchemaById?id=",
            loggerId);

    ResponseEntity<String> result = IoTUtils
        .getRequestMethod(url, getHeaders(dashboardKaaDataSource.getDashboardConfigDto()));

    //TODO refactor
    //JsonObject contenente l'intero messaggio
    JsonObject json = getObjectFromJson(result.getBody(), new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);

    JsonElement jsonElement = json.get("body");

    //JsonElement contenente il campo body{} che al suo interno contiene i fields
    json = getObjectFromJson(jsonElement.getAsString(), new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);

    jsonElement = json.get("fields");

    //JsonArray contenent i fields che sarebbero le misure che ci interessano
    JsonArray jsonArray = getObjectFromJson(jsonElement.toString(), new TypeToken<JsonArray>() {
    }.getType(), JsonArray.class);

    List<MeasurementType> measurementTypes = getObjectFromJson(jsonArray.toString(),
        new TypeToken<List<MeasurementType>>() {
        }.getType(), ArrayList.class);

    MeasurementTemplate measurementTemplate = new MeasurementTemplate();
    measurementTemplate.setFields(measurementTypes);

    measurementTemplateService.save(measurementTemplate);
  }

  @Override
  public JsonObject saveDeviceMeasurement(
      IDashboardDatasourceDto dashboardDatasourceDto,
      JsonArray measurementBody) throws Exception {

    DashboardKaaDatasourceDto kaaConn = (DashboardKaaDatasourceDto) connectorFactory.getDashboardDatasource(dashboardDatasourceDto.getServerType());

    logger.info("{} saveDeviceMeasurement {} on {}", this.getClass().getSimpleName(),
        kaaConn.getKaaDatasourceConfigDto().getDatasourceURI(),
        kaaConn.getKaaDatasourceConfigDto().getDatasourceCode());

    String datasourceCode = kaaConn.getKaaDatasourceConfigDto()
        .getDatasourceCode();

    MeasurementConfig conf = measurementConfigService
        .getByDashboardIdAndDatasourceCodeAndMeasurementTemplateId(kaaConn.getDashboardConfigDto().getId(),
            datasourceCode, "");
    
    JsonObject returnedJson = new JsonObject();

    for (JsonElement jsonElement : measurementBody) {
      for (Entry<String, JsonElement> e : jsonElement.getAsJsonObject().entrySet()) {
        returnedJson.addProperty(conf.getMappingKey(e.getKey()), e.getValue().getAsString());
      }
    }
    return returnedJson;
  }

  @Override
  public IDashboardDatasourceDto getDashboardDatasourceDtoByIdAndCode(
      DashboardConfigDto dashboardConfigDto, String datasourceCode) {
    return null;
  }

  @Override
  public List<MeasurementObject> getMeasurements(
      IDashboardDatasourceDto dashboardKaaDatasourceDto, Long nMeasurements, Date startDate,
      Date endDate) throws RuntimeException {
    return null;//getMeasurements From DB
  }

  @Override
  public List<LogAppender> getLogAppenders(DashboardKaaDatasourceDto dashboardDatasourceDto) {

    KaaApplicationConfigDto kaaConn = dashboardDatasourceDto.getKaaDatasourceConfigDto();

    logger.info("{} getLogAppenders {} on {}", this.getClass().getSimpleName(),
        kaaConn.getDatasourceURI(), kaaConn.getDatasourceCode());

    String url = StringUtils
        .join(dashboardDatasourceDto.getDashboardUrl(), KAA_URL_BASE_PATH, "/logAppenders/",
            kaaConn.getDatasourceCode());

    HttpEntity httpEntity = new HttpEntity(
        getHeaders(dashboardDatasourceDto.getDashboardConfigDto()));

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> result = restTemplate
        .exchange(url, HttpMethod.GET, httpEntity, String.class);

    List<LogAppender> logAppenders = getObjectFromJson(result.getBody(),
        new TypeToken<List<LogAppender>>() {
        }.getType(), ArrayList.class);

    logger.info("{} getLogAppenders {} on {} result {}", this.getClass().getSimpleName(),
        kaaConn.getDatasourceURI(), kaaConn.getDatasourceCode(), logAppenders != null);

    return logAppenders;
  }

  @Override
  public boolean supports(String connectorType) {
    if (connectorType.equals(KAA_SERVER_TYPE)) {
      return true;
    }
    return false;
  }

}
