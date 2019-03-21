package org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.service;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigManager;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.MeasurementException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementMapping;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorIot;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementTemplateService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto.DashboardSitewhereDatasourceDto;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto.SitewhereApplicationConfigDto;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.JSON_FIELD_SEPARATOR;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.KAA_SERVER_TYPE;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.getObjectFromJson;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.SitewhereConstants.ONE_RESULT_PAGINATION;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.SitewhereConstants.SITEWHERE_TENANT;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.SitewhereConstants.SITEWHERE_URL_BASE_PATH;

@Service
public class SitewhereConnectorService implements ISitewhereConnectorService, IConnectorIot {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  IMeasurementConfigService measurementConfigService;

  @Autowired
  IMeasurementTemplateService measurementTemplateService;

  @Autowired
  DashboardConfigManager dashboardConfigManager;

  @Autowired
  ConnectorFactory connectorFactory;

  @Override
  public boolean pingDevice(IDashboardDatasourceDto dashboardDatasourceDto)
      throws IOException {

    DashboardSitewhereDatasourceDto sitewhereConn = (DashboardSitewhereDatasourceDto) connectorFactory.getDashboardDatasource(dashboardDatasourceDto.getServerType());

    logger.info("{} pingDevice {}", this.getClass().getSimpleName(),
        sitewhereConn.getSitewhereDatasourceConfigDto().getName());

    String url = StringUtils.join(dashboardDatasourceDto.getDashboardConfigDto().getServerURI(),
        SITEWHERE_URL_BASE_PATH, "/devices/",
        sitewhereConn.getSitewhereDatasourceConfigDto().getDatasourceCode());

    ResponseEntity<String> result = IoTUtils
        .getRequestMethod(url, getSitewhereHeaders(dashboardDatasourceDto.getDashboardConfigDto()));

    JsonObject jsonObject = getObjectFromJson(result.getBody(), new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);
    String ret = IoTUtils.getNestedFieldFromJson(jsonObject, "hardwareId").getAsString();

    logger.info("{} pingDevice {} results {}", this.getClass().getSimpleName(),
        sitewhereConn.getSitewhereDatasourceConfigDto().getDatasourceCode(),
        StringUtils.isNotBlank(ret));

    return StringUtils.isNotBlank(ret);
  }

  @Override
  public JsonObject saveDeviceMeasurement(
      IDashboardDatasourceDto dashboardDatasourceDto,
      JsonArray measurementBody) throws ApsSystemException {

    DashboardSitewhereDatasourceDto sitewhereConn = (DashboardSitewhereDatasourceDto) connectorFactory.getDashboardDatasource(dashboardDatasourceDto.getServerType());

    String assignmentId = sitewhereConn.getSitewhereDatasourceConfigDto()
        .getAssignmentId();

    String datasourceCode = sitewhereConn.getSitewhereDatasourceConfigDto()
        .getDatasourceCode();

    String url = StringUtils
        .join(dashboardDatasourceDto.getDashboardUrl(), SITEWHERE_URL_BASE_PATH,
            "/assignments/",
            assignmentId,
            "/measurements" + ONE_RESULT_PAGINATION);

    ResponseEntity<String> result = IoTUtils
        .getRequestMethod(url, getSitewhereHeaders(sitewhereConn.getDashboardConfigDto()));

    JsonObject json = getObjectFromJson(result.getBody(), new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);

    MeasurementConfig conf = measurementConfigService.getByDashboardIdAndDatasourceCodeAndMeasurementTemplateId(
        sitewhereConn.getDashboardConfigDto().getId(),
        datasourceCode, "");

    JsonObject returnedJson = new JsonObject();

    for (Entry<String, JsonElement> e : json.entrySet()) {
      returnedJson.addProperty(conf.getMappingKey(e.getKey()), e.getValue().getAsString());
    }
    return returnedJson;
  }

  @Override
  public IDashboardDatasourceDto getDashboardDatasourceDtoByIdAndCode(DashboardConfigDto dashboardConfigDto,
      String datasourceCode) {

    DatasourcesConfigDto datasourcesConfigDto = dashboardConfigManager
        .getDatasourceByDatasourcecodeAndDashboard(dashboardConfigDto.getId(), datasourceCode);

    DashboardSitewhereDatasourceDto dto = new DashboardSitewhereDatasourceDto();
    dto.setSitewhereDatasourceConfigDto((SitewhereApplicationConfigDto) datasourcesConfigDto);
    dto.setDashboardConfigDto(dashboardConfigDto);
    return dto;
  }

  @Override
  public List<? extends AbstractDashboardDatasourceDto> getAllDevices(
      DashboardConfigDto dashboardConfigDto) {

    logger.info("{} getAllDevices {}", this.getClass().getSimpleName(),
        dashboardConfigDto.getServerURI());

    String url = StringUtils.join(dashboardConfigDto.getServerURI(),
        SITEWHERE_URL_BASE_PATH, "/devices/");

    ResponseEntity<String> result = IoTUtils
        .getRequestMethod(url, getSitewhereHeaders(dashboardConfigDto));

    JsonObject json = getObjectFromJson(result.getBody(), new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);

    List<DashboardSitewhereDatasourceDto> devices = getObjectFromJson(
        json.get("results").getAsJsonArray().toString(),
        new TypeToken<List<DashboardSitewhereDatasourceDto>>() {
        }.getType(), ArrayList.class);

    logger.info("{} getAllDevices {} results {}", this.getClass().getSimpleName(),
        dashboardConfigDto.getServerURI(), devices != null);

    return devices;
  }

  @Override
  public void saveMeasurementTemplate(IDashboardDatasourceDto dashboardDatasourceDto)
      throws ApsSystemException {
    MeasurementTemplate measurementTemplate = new MeasurementTemplate();

    DashboardSitewhereDatasourceDto sitewhereConn = (DashboardSitewhereDatasourceDto) connectorFactory.getDashboardDatasource(dashboardDatasourceDto.getServerType());

    String url = StringUtils
        .join(sitewhereConn.getDashboardUrl(), SITEWHERE_URL_BASE_PATH,
            "/assignments/",
            sitewhereConn.getSitewhereDatasourceConfigDto().getAssignmentId(),
            "/measurements" + ONE_RESULT_PAGINATION);

    ResponseEntity<String> result = IoTUtils
        .getRequestMethod(url, getSitewhereHeaders(sitewhereConn.getDashboardConfigDto()));

    JsonObject jsonObject = IoTUtils.getNestedFieldFromJson(result.getBody(),
        StringUtils.join("results", JSON_FIELD_SEPARATOR, "measurements")).getAsJsonArray().get(0).getAsJsonObject();

    for (String key :jsonObject.keySet()) {
      measurementTemplate.addField(key,jsonObject.get(key).getClass().getSimpleName());
    }
    measurementTemplateService.save(measurementTemplate);
  }

  @Override
  public List<MeasurementObject> getMeasurements(IDashboardDatasourceDto dashboardDatasourceDto,
      Long nMeasurements, Date startDate, Date endDate) throws RuntimeException {

    DashboardSitewhereDatasourceDto sitewhereConn = (DashboardSitewhereDatasourceDto) connectorFactory.getDashboardDatasource(dashboardDatasourceDto.getServerType());

    String assignmentId = sitewhereConn.getSitewhereDatasourceConfigDto()
        .getAssignmentId();

    String queries = IoTUtils.formatQueryUrl(new HashMap<String, Object>() {{
      put("endDate", endDate);
      put("startDate", startDate);
      put("take", nMeasurements);
      put("pageSize", nMeasurements);
    }});

    String url = StringUtils
        .join(sitewhereConn.getDashboardUrl(), SITEWHERE_URL_BASE_PATH,
            "/assignments/",
            assignmentId,
            "/measurements?",
            queries);

    ResponseEntity<String> result = IoTUtils.getRequestMethod(url,
        getSitewhereHeaders(sitewhereConn.getDashboardConfigDto()));

    JsonObject body = getObjectFromJson(result.getBody(), new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);

    JsonArray jsonArray = getJsonMeasurements(body);
    
    MeasurementTemplate measurementTemplate;
    MeasurementConfig config = null;
    try {
      measurementTemplate = measurementTemplateService.getByDashboardIdAndDatasourceCode(dashboardDatasourceDto.getDashboardId(), dashboardDatasourceDto.getDatasourceCode());
      config = measurementConfigService.getByDashboardIdAndDatasourceCodeAndMeasurementTemplateId(
          dashboardDatasourceDto.getDashboardId(), dashboardDatasourceDto.getDatasourceCode(),measurementTemplate.getId());

    } catch (Throwable t) {
      logger.error(String.format("Cannot find any Measurement Template or Measurement Config for dashboard and datasource ids {} {}", dashboardDatasourceDto.getDashboardId(), dashboardDatasourceDto.getDashboardConfigDto()));
      throw new MeasurementException(String.format("Cannot find any Measurement Template or Measurement Config for dashboard and datasource ids {} {}", dashboardDatasourceDto.getDashboardId(), dashboardDatasourceDto.getDashboardConfigDto()));
    }
    List<MeasurementObject> measurementObjects = new ArrayList<>();
    for (JsonElement element : jsonArray) {
      JsonObject jsonObject = element.getAsJsonObject();
      measurementObjects.add(getMeasurementObject(config,jsonObject,jsonObject.get("name").getAsString()));
    }
    return measurementObjects;
  }

  private JsonArray getJsonMeasurements(JsonObject body) {
    JsonArray jsonArray = new JsonArray();
    for (JsonElement jsonElement : body.get("results").getAsJsonArray()) {
      JsonObject measurements = jsonElement.getAsJsonObject().get("measurements").getAsJsonObject();
      for (String key :measurements.keySet()) {
        JsonObject json = new JsonObject();
        json.addProperty("name", key);
        json.addProperty(key,measurements.get(key).getAsString());
        json.addProperty("eventDate",jsonElement.getAsJsonObject().get("eventDate").getAsString());
        json.addProperty("swReceivedDate",jsonElement.getAsJsonObject().get("receivedDate").getAsString());
        jsonArray.add(json);
      }
    }
    return jsonArray;
  }


  private HttpHeaders getSitewhereHeaders(DashboardConfigDto dashboardConfigDto) {
    HttpHeaders header = IoTUtils
        .getHeaders(dashboardConfigDto);
    header.add(SITEWHERE_TENANT, dashboardConfigDto.getToken());

    return header;
  }

  @Override
  public boolean supports(String connectorType) {
    if (connectorType.equals(KAA_SERVER_TYPE)) {
      return true;
    }
    return false;
  }

  private MeasurementObject getMeasurementObject(MeasurementConfig config, JsonElement jsonElement, String key){
    MeasurementObject measurementObject = new MeasurementObject();
    measurementObject.setName(config.getMappingKey(key));
    
    if(jsonElement.isJsonObject()) {
      measurementObject
          .setMeasure(jsonElement.getAsJsonObject().get(key));
      
        if(config.getMappingforSourceName(key).isPresent()) {
          MeasurementMapping mapping = config
              .getMappingforSourceName(key).get();
          try {
            Class<?> transformerClass = Class.forName(mapping.getTransformerClass());
            try {
              measurementObject.setMeasure(
                  new Gson().toJson(
                      transformerClass.cast(jsonElement.getAsJsonObject().get(key).getAsString())));
            }
            catch (ClassCastException e) {
              
              Constructor<?> constructor = transformerClass.getConstructor(String.class);
              measurementObject.setMeasure(new Gson().toJson(constructor.newInstance(jsonElement.getAsJsonObject().get(key).getAsString())));

            }
          } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error(String.format("Class {} Not Found for measurementConfigId {}",
                mapping.getTransformerClass(), config.getMeasurementConfigId()));
          }
        }
      measurementObject.setEventDate(jsonElement.getAsJsonObject().get("eventDate").getAsString());
      measurementObject.setSwReceivedDate(jsonElement.getAsJsonObject().get("swReceivedDate").getAsString());
      measurementObject.setEntandoReceivedDate(new DateTime().toString("yyyy-MM-dd'T'hh:mm:ss"));
    }
    return measurementObject;
  }
}
