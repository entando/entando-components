package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IConnectorService {

	LinkedHashMap<String, String> getConnectorTypes() throws IOException;

	DashboardConfigDto pingDevice(DashboardConfigDto device, String datasourceCode)
      throws IOException, ApsSystemException;

	<T extends DashboardConfigDto> boolean pingServer(T dashboardConfigDto) throws IOException;

	List<DatasourcesConfigDto> getAllDevices(DashboardConfigDto dashboardConfigDto);

	/**
	 * TODO Lista struttura Misura per configurazione tramite interfaccioa web
	 */
	//    <T extends DashboardDatasourceDto> MeasurementConfig getDeviceMeasurementSchema(T dashboardDatasourceDto, String loggerId);

	void setDeviceMeasurementSchema(DashboardConfigDto dto, String datasourceCode) throws ApsSystemException;

	void saveDeviceMeasurement(DashboardConfigDto dashboardDatasourceDto,
      String datasourceCode, String measure);

	PagedMetadata<Map<String, Object>> getMeasurements(RestListRequest requestList);

	List<Map<String, Object>> getDeviceMeasurements(DashboardConfigDto dto,
      String datasourceCode, Date startDate, Date endDate,
      RestListRequest restListRequest, DatasourceType type);

	MeasurementConfig getMeasurementsConfig(DashboardConfigDto dto, String datasourceCode, DatasourceType type);

	MeasurementTemplate getDeviceMeasurementSchema(DashboardConfigDto dto,
      String datasourceCode, DatasourceType type);

	List<ServerType> getDashboardTypes();

	DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest)
      throws ApsSystemException;

  DashboardConfigDto refreshMetadata(DashboardConfigDto dto, String datasourceCode) throws ApsSystemException;

  void setDeviceStatuses(DashboardConfigDto dto, String datasourceCode,
      JsonObject body);

  DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode);
}
