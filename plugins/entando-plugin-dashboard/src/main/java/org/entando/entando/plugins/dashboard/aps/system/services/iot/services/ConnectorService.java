package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ConnectorService extends AbstractConnectorService implements IConnectorService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  ConnectorFactory connectorFactory;

  protected ConnectorFactory getConnectorFactory() {
    return connectorFactory;
  }

  public void setConnectorFactory(
      ConnectorFactory connectorFactory) {
    this.connectorFactory = connectorFactory;
  }

  @Override
  public LinkedHashMap<String, String> getConnectorTypes() throws IOException {
    return null;
  }

  @Override
  public boolean pingDevice(IDashboardDatasourceDto device) throws IOException {
    logger.info("{} pingDevice on {}", this.getClass().getSimpleName(), device.getDashboardUrl());
    return connectorFactory.getConnector(device.getDashboardConfigDto().getServerDescription()).pingDevice(device);
  }

  @Override
  public <T extends DashboardConfigDto> boolean pingServer(T dashboardConfigDto)
      throws IOException {
    logger.info("{} ping to {}", this.getClass().getSimpleName(), dashboardConfigDto.getServerURI());  
    return super.isServerReacheable(dashboardConfigDto);
  }

  @Override
  public <T extends DashboardConfigDto> List<? extends AbstractDashboardDatasourceDto> getAllDevices(
      T dashboardConfigDto) {
    logger.info("{} getAllDevices to {}", this.getClass().getSimpleName(), dashboardConfigDto.getServerURI());
    return connectorFactory.getConnector(dashboardConfigDto.getServerDescription()).getAllDevices(dashboardConfigDto);
  }

  @Override
  public void setDeviceMeasurementSchema(
      IDashboardDatasourceDto dashboardDatasourceDto) throws ApsSystemException {
    logger.info("{} getSchema to {}", this.getClass().getSimpleName(), dashboardDatasourceDto.getDashboardConfigDto().getServerURI());
    connectorFactory.getConnector(dashboardDatasourceDto.getDashboardConfigDto().getServerDescription()).saveMeasurementTemplate(dashboardDatasourceDto);;
  }

  @Override
  public JsonObject saveDeviceMeasurement(
      IDashboardDatasourceDto dashboardDatasourceDto, JsonArray measurementBody)
      throws Exception {
    logger.info("{} saveDeviceMeasurement to {}", this.getClass().getSimpleName(), dashboardDatasourceDto.getDashboardConfigDto().getServerURI());
    return connectorFactory.getConnector(dashboardDatasourceDto.getDashboardConfigDto().getServerDescription()).saveDeviceMeasurement(dashboardDatasourceDto, measurementBody);
  }

  @Override
  public IDashboardDatasourceDto getDashboardDatasourceDtobyIdAndCodeAndServerType(DashboardConfigDto dashboardConfigDto,
      String datasourceCode, String serverType) {
    logger.info("{} getDashboardDatasourceDtoByIdAndCode ids :{}, {}", this.getClass().getSimpleName(), dashboardConfigDto.getId() ,datasourceCode);
    return connectorFactory.getConnector(serverType).getDashboardDatasourceDtoByIdAndCode(dashboardConfigDto, datasourceCode);
  }

  @Override
  public JsonArray getDeviceMeasurements(IDashboardDatasourceDto dto, Long nMeasurements, Instant startDate, Instant endDate) {
    logger.info("{} getDeviceMeasurement By Dashboard and datasource ids :{}, {}", this.getClass().getSimpleName(), dto.getDashboardId() ,dto.getDatasourceCode());
    return connectorFactory.getConnector(dto.getServerType()).getMeasurements(dto,nMeasurements,startDate,endDate);
  }
}
