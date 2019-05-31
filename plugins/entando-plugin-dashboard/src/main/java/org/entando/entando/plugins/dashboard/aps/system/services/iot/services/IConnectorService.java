package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.RestListRequest;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IConnectorService {

	DashboardConfigDto pingDevice(DashboardConfigDto device, String datasourceCode);

	<T extends DashboardConfigDto> boolean pingServer(T dashboardConfigDto) throws IOException;

	List<DatasourcesConfigDto> getAllDevices(DashboardConfigDto dashboardConfigDto);

	void setDeviceMeasurementSchema(DashboardConfigDto dto, String datasourceCode);

	void saveDeviceMeasurement(DashboardConfigDto dashboardDatasourceDto,
      String datasourceCode, String measure);

	List<Map<String, Object>> getDeviceMeasurements(DashboardConfigDto dto,
      String datasourceCode, Date startDate, Date endDate,
      RestListRequest restListRequest);

	MeasurementConfig getMeasurementsConfig(DashboardConfigDto dto, String datasourceCode);

	MeasurementTemplate getDeviceMeasurementSchema(DashboardConfigDto dto,
      String datasourceCode);

	List<ServerType> getDashboardTypes();

	DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest);

  DashboardConfigDto refreshMetadata(DashboardConfigDto dto, String datasourceCode);

  void setGeodataDeviceStatuses(DashboardConfigDto dto, String datasourceCode,
      JsonObject body) throws ApsSystemException;

  DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode)
      throws ApsSystemException;

  boolean isParcheggioAvailable(DashboardConfigDto dto, String datasourceCode);
}
