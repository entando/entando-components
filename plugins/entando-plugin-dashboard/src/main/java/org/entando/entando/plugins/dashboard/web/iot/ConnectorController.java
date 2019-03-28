package org.entando.entando.plugins.dashboard.web.iot;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping(value = "/plugins/dashboard")
public class ConnectorController {

  @Autowired
  IDashboardConfigService dashboardConfigService;

  @Autowired
  IConnectorService connectorService;

  protected IConnectorService getConnectorService() {
    return connectorService;
  }

  public void setConnectorService(IConnectorService connectorService) {
    this.connectorService = connectorService;
  }

  @RequestMapping(value = "/server/{serverId}/datasource{datasourceCode}", method = RequestMethod.POST)
  public ResponseEntity<?> saveMeasurement(@PathVariable int serverId,
      @PathVariable String datasourceCode,
      @RequestBody JsonArray jsonElements) throws Exception {

    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(serverId);
    DashboardDatasourceDto dto = IoTUtils
        .getDashboardDatasourceDto(dashboardDto, datasourceCode);

    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    connectorService.saveDeviceMeasurement(null, jsonElements);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/server/{serverId}/datasource/{datasourceCode}/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PagedRestResponse<MeasurementObject>> getMeasurement(
      @PathVariable int serverId,
      @PathVariable String datasourceCode,
      @RequestParam(value = "nMeasurements", required = false) long nMeasurements,
      @RequestParam(value = "startDate", required = false) Date startDate,
      @RequestParam(value = "endDate", required = false) Date endDate,
      RestListRequest requestList) throws Exception {

    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(serverId);
    DashboardDatasourceDto dto = IoTUtils.getDashboardDatasourceDto(dashboardDto, datasourceCode);
    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    try {
      PagedMetadata<MeasurementObject> pagedMetadata = this.connectorService
          .getDeviceMeasurements(dto, nMeasurements, startDate, endDate, requestList);
      return new ResponseEntity(new PagedRestResponse(pagedMetadata), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @RequestMapping(value = "/setTemplate/server/{serverId}/datasource/{datasourceCode}", method = RequestMethod.POST)
  public ResponseEntity<?> setMeasurementTemplate(@PathVariable int serverId,
      @PathVariable String datasourceCode)
      throws ApsSystemException {
    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(serverId);
    DashboardDatasourceDto dto = IoTUtils.getDashboardDatasourceDto(dashboardDto, datasourceCode);
    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    connectorService.setDeviceMeasurementSchema(dto);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/getTemplate/server/{serverId}/datasource/{datasourceCode}", method = RequestMethod.POST)
  public ResponseEntity<SimpleRestResponse<MeasurementTemplate>> getMeasurementTemplate(
      @PathVariable int serverId, @PathVariable String datasourceCode) {
    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(serverId);
    DashboardDatasourceDto dto = IoTUtils.getDashboardDatasourceDto(dashboardDto, datasourceCode);
    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    MeasurementTemplate template = connectorService
        .getDeviceMeasurementSchema(dto);

    return new ResponseEntity<>(new SimpleRestResponse(template), HttpStatus.OK);
  }


  @RequestMapping(value = "server/{serverId}/datasource/{datasourceCode}/config")
  public ResponseEntity<?> getMeasurementConfig(@PathVariable int serverId,
      @PathVariable String datasourceCode) {
    if (!dashboardConfigService.existsById(serverId)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(serverId);
    DashboardDatasourceDto dto = IoTUtils.getDashboardDatasourceDto(dashboardDto, datasourceCode);
    if (dto.getDatasourcesConfigDto() == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    MeasurementConfig config = connectorService.getMeasurementsConfig(dto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}
