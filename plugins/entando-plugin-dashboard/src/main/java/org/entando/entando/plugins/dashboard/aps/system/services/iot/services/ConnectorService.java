package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
  public <T extends  AbstractDashboardDatasourceDto> boolean pingDevice(T device) throws IOException {
    logger.info("{} pingDevice on {}", this.getClass().getSimpleName(), device.getDashboardConfigDto().getServerURI());
    return connectorFactory.get(device.getDashboardConfigDto().getServerDescription()).pingDevice(device);
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
    return connectorFactory.get(dashboardConfigDto.getServerDescription()).getAllDevices(dashboardConfigDto);
  }

  @Override
  public <T extends IDashboardDatasourceDto> MeasurementConfig setDeviceMeasurementSchema(
      T dashboardDatasourceDto, String loggerId) {
    logger.info("{} getSchema to {}", this.getClass().getSimpleName(), dashboardDatasourceDto.getDashboardConfigDto().getServerURI());
//    return connectorFactory.get(dashboardDatasourceDto.getDashboardConfigDto().getServerDescription()).getSchema(dashboardDatasourceDto); //TODO inserire loggerId / specificationId
    return null;
  }

  @Override
  public <T extends IDashboardDatasourceDto> JsonObject saveDeviceMeasurement(
      T dashboardDatasourceDto, JsonArray measurementBody)
      throws Exception {
    logger.info("{} saveDeviceMeasurement to {}", this.getClass().getSimpleName(), dashboardDatasourceDto.getDashboardConfigDto().getServerURI());
    return connectorFactory.get(dashboardDatasourceDto.getDashboardConfigDto().getServerDescription()).saveDeviceMeasurement(dashboardDatasourceDto, measurementBody);
  }

//@Override
//public <T extends IDashboardDatasourceDto> MeasurementConfig setDeviceMeasurementSchema(T dashboardDatasourceDto,
//		String loggerId) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public <T extends IDashboardDatasourceDto> JsonObject saveDeviceMeasurement(T dashboardDatasourceDto,
//		JsonArray measurementBody) throws Exception {
//	// TODO Auto-generated method stub
//	return null;
//}

}
