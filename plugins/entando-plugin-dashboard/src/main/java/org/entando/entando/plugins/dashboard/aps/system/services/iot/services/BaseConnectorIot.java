package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class BaseConnectorIot implements IConnectorFactory {

  @Override
  public boolean supports(String connectorType) {
    return false;
  }

  @Override
  public DashboardConfigDto pingDevice(DashboardConfigDto dashboardDatasourceDto,
      String datasourceCode) {
    return null;
  }

  @Override
  public List<DatasourcesConfigDto> getAllDevices(DashboardConfigDto dashboardConfigDto)
      throws ApiResourceNotAvailableException {
    return null;
  }

  @Override
  public void saveMeasurementTemplate(DashboardConfigDto dashboardDatasource, String datasourceCode) {

  }

  @Override
  public void saveDeviceMeasurement(DashboardConfigDto dashboardDatasourceDto,
      String datasourceCode, String measurementBody) {

  }

  @Override
  public List<Map<String, Object>> getMeasurements(DashboardConfigDto dto, String datasourceCode,
      Date startDate, Date endDate, RestListRequest restListRequest) throws RuntimeException {
    return null;
  }

  @Override
  public MeasurementConfig getMeasurementConfig(DashboardConfigDto dto, String datasourceCode) {
    return null;
  }

  @Override
  public MeasurementTemplate getDeviceMeasurementSchema(DashboardConfigDto dto,
      String datasourceCode) throws ApiResourceNotAvailableException {
    return null;
  }

  @Override
  public ServerType getServerType() {
    return null;
  }

  @Override
  public DatasourceType getDatasourceType() {
    return null;
  }

  @Override
  public DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest) {
    return null;
  }

  @Override
  public DashboardConfigDto refreshMetadata(DashboardConfigDto dto, String datasourceCode) {
    return null;
  }

  @Override
  public boolean isParcheggioAvailable(DashboardConfigDto dto, String datasourceCode) {
    return false;
  }

  @Override
  public DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode) {
    return null;
  }

  @Override
  public void setGeodataDeviceStatuses(DashboardConfigDto dto, String datasourceCode,
      JsonObject body) {

  }
}
