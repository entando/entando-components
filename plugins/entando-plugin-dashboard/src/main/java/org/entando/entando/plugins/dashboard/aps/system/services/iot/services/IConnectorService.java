package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
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

	boolean pingDevice(IDashboardDatasourceDto device) throws IOException;

	<T extends DashboardConfigDto> boolean pingServer(T dashboardConfigDto) throws IOException;

	List<DatasourcesConfigDto> getAllDevices(DashboardConfigDto dashboardConfigDto);

	/**
	 * TODO Lista struttura Misura per configurazione tramite interfaccioa web
	 */
	//    <T extends DashboardDatasourceDto> MeasurementConfig getDeviceMeasurementSchema(T dashboardDatasourceDto, String loggerId);

	void setDeviceMeasurementSchema(IDashboardDatasourceDto dashboardDatasourceDto) throws ApsSystemException;

	void saveDeviceMeasurement(IDashboardDatasourceDto dashboardDatasourceDto, String measure);

	PagedMetadata<Map<String, Object>> getMeasurements(RestListRequest requestList);

	List<Map<String, Object>> getDeviceMeasurements(IDashboardDatasourceDto dto, Date startDate, Date endDate, RestListRequest restListRequest);

	MeasurementConfig getMeasurementsConfig(IDashboardDatasourceDto dto);

	MeasurementTemplate getDeviceMeasurementSchema(IDashboardDatasourceDto dto);

	List<ServerType> getDashboardTypes();

	DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest)
      throws ApsSystemException;

  DashboardDatasourceDto refreshMetadata(DashboardDatasourceDto dto) throws ApsSystemException;
}
