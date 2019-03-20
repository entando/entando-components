package org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.service;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.sitewhere.dto.DashboardSitewhereDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.sitewhere.service.ISitewhereConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorIot;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementTemplateService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.JSON_FIELD_SEPARATOR;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.KAA_SERVER_TYPE;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.SITEWHERE_TENANT;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.SITEWHERE_URL_BASE_PATH;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.getObjectFromJson;

@Service
public class SitewhereConnectorService implements ISitewhereConnectorService, IConnectorIot {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  IMeasurementConfigService measurementConfigService;

  @Autowired
  IMeasurementTemplateService measurementTemplateService;

  @Override
  public <T extends AbstractDashboardDatasourceDto> boolean pingDevice(T dashboardDatasourceDto)
      throws IOException {

    DashboardSitewhereDatasourceDto sitewhereConn = IoTUtils
        .getDashboardDataSourceConnecDto(dashboardDatasourceDto,
            DashboardSitewhereDatasourceDto.class);

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
  public <T extends AbstractDashboardDatasourceDto> JsonObject saveDeviceMeasurement(
      T dashboardDatasourceDto,
      JsonArray measurementBody) throws ApsSystemException {

    DashboardSitewhereDatasourceDto sitewhereConn = IoTUtils
        .getDashboardDataSourceConnecDto(dashboardDatasourceDto,
            DashboardSitewhereDatasourceDto.class);

    String assignmentId = sitewhereConn.getSitewhereDatasourceConfigDto()
        .getAssignmentId();

    String datasourceCode = sitewhereConn.getSitewhereDatasourceConfigDto()
        .getDatasourceCode();

    String url = StringUtils
        .join(dashboardDatasourceDto.getDashboardUrl(), SITEWHERE_URL_BASE_PATH,
            "/assignments/",
            assignmentId,
            "/measurements" + IoTConstants.ONE_RESULT_PAGINATION);

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
  public <T extends AbstractDashboardDatasourceDto> MeasurementTemplate getSchema(
      T dashboardSitewhereDatasourceDto) {
//    DashboardSitewhereDatasourceDto sitewhereConn = (DashboardSitewhereDatasourceDto) dashboardSitewhereDatasourceDto;
//
//    MeasurementTemplate measurementTemplate = null; //TODO
//
////    MeasurementTemplate measurementTemplate = measurementTemplateService
////        .getById(kaaConn.getDashboardConfigDto().getId(),
////            kaaConn.getKaaDatasourceConfigDto().getDatasourceCode());
//
//    logger.info("{} getSchema {} on {}", this.getClass().getSimpleName(),
//        sitewhereConn.getSitewhereDatasourceConfigDto().getDatasourceURI(),
//        sitewhereConn.getSitewhereDatasourceConfigDto().getDatasourceCode());
//
//    return measurementTemplate;

    /**
     * NON HA SENSO OTTENERE LO SCHEMA DI UN DEVICE, DATO CHE LO SCHEMA è GENERICO E PIù DEVICE POSSONO
     * AVERE LO STESSO SCHEMA O OGNI DEVICE HA PIù SCHEMA.
     * è STATO INTRODOTTO IL MEASUREMENTCONFIG, QUINDI OGNI DEVICE HA LA SUA CONFIGURAZIONE 
     * ALLA QUALE è COLLEGATO L'ID DEL TEMPLATE, L'ID DEL SERVER E L'ID DEL DEVICE, E UN BOOLEAN
     * PER VEDERE SE è LO SCHEMA ATTIVO O NO.
     * DUNQUE QUESTA FUNZIONE SARà SEMPRE INUTILE.
     */

    return null;
  }

  @Override
  public <T extends AbstractDashboardDatasourceDto> void saveMeasurementTemplate(
      T dashboardSitewhereDatasource)
      throws ApsSystemException {
    MeasurementTemplate measurementTemplate = new MeasurementTemplate();

    DashboardSitewhereDatasourceDto sitewhereConn = IoTUtils
        .getDashboardDataSourceConnecDto(dashboardSitewhereDatasource,
            DashboardSitewhereDatasourceDto.class);

    String url = StringUtils
        .join(sitewhereConn.getDashboardUrl(), SITEWHERE_URL_BASE_PATH,
            "/assignments/",
            sitewhereConn.getSitewhereDatasourceConfigDto().getAssignmentId(),
            "/measurements" + IoTConstants.ONE_RESULT_PAGINATION);

    ResponseEntity<String> result = IoTUtils
        .getRequestMethod(url, getSitewhereHeaders(sitewhereConn.getDashboardConfigDto()));

    JsonObject measurements = IoTUtils.getNestedFieldFromJson(result.getBody(),
        StringUtils.join("results", JSON_FIELD_SEPARATOR, "measurements")).getAsJsonObject();
    Iterable<Entry<String, JsonElement>> iterable = measurements.entrySet();

    iterable.forEach(e -> measurementTemplate.addField(e.getKey(), e.getValue().getAsString()));

    measurementTemplateService.save(measurementTemplate);
  }

  @Override
  public JsonArray getMeasurements(DashboardSitewhereDatasourceDto dashboardSitewhereDatasourceDto,
      Long nMeasurements, Instant startDate, Instant endDate) {

    String assignmentId = dashboardSitewhereDatasourceDto.getSitewhereDatasourceConfigDto()
        .getAssignmentId();

    String queries = IoTUtils.formatQueryUrl(new HashMap<String, Object>() {{
      put("endDate=", endDate);
      put("startDate=", startDate);
      put("take=", nMeasurements);
      put("pageSize", nMeasurements);
    }});

    String url = StringUtils
        .join(dashboardSitewhereDatasourceDto.getDashboardUrl(), SITEWHERE_URL_BASE_PATH,
            "/assignments/",
            assignmentId,
            "/measurements?",
            queries);

    ResponseEntity<String> result = IoTUtils.getRequestMethod(url,
        getSitewhereHeaders(dashboardSitewhereDatasourceDto.getDashboardConfigDto()));

    JsonObject json = getObjectFromJson(result.getBody(), new TypeToken<JsonObject>() {
    }.getType(), JsonObject.class);

    JsonArray finalArray = new JsonArray();
    for (JsonElement jsonElement : json.get("results").getAsJsonArray()) {
      finalArray.add(jsonElement.getAsJsonObject().get("measurements").getAsJsonObject());
    }

    return finalArray;
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

}
