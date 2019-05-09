package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.ParkingStatuses;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.RestListRequest;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IConnectorIot {

  /**
   * Returns true is the implementation of this processor fits the provided widget code
   */
  boolean supports(String connectorType);

  DashboardConfigDto pingDevice(DashboardConfigDto dashboardDatasourceDto, String datasourceCode) throws IOException;

  List<DatasourcesConfigDto> getAllDevices(DashboardConfigDto dashboardConfigDto) throws ApiResourceNotAvailableException;

  void saveMeasurementTemplate(DashboardConfigDto dashboardDatasource,
      String datasourceCode)
      throws ApsSystemException;

  void saveDeviceMeasurement(DashboardConfigDto dashboardDatasourceDto,
      String datasourceCode, String measurementBody);

  List<Map<String, Object>> getMeasurements(DashboardConfigDto dashboardSitewhereDatasourceDto,
      String datasourceCode, Date startDate, Date endDate,
      RestListRequest restListRequest) throws RuntimeException;

  MeasurementConfig getMeasurementConfig(DashboardConfigDto dto, String datasourceCode);

  MeasurementTemplate getDeviceMeasurementSchema(DashboardConfigDto dto,
      String datasourceCode) throws ApiResourceNotAvailableException;

  ServerType getServerType();

  DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest)
      throws ApsSystemException;

  DashboardConfigDto refreshMetadata(DashboardConfigDto dto, String datasourceCode) throws ApsSystemException;

  DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode);

  void setDeviceStatuses(DashboardConfigDto dto, String datasourceCode, ParkingStatuses parkingStatuses);
}
