package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
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

	JsonObject saveDeviceMeasurement(IDashboardDatasourceDto dashboardDatasourceDto, JsonArray measurementBody)
			throws Exception;

	PagedMetadata<Map<String, Object>> getMeasurements(RestListRequest requestList);

	PagedMetadata<MeasurementObject> getDeviceMeasurements(IDashboardDatasourceDto dto, Long nMeasurements, Date startDate, Date endDate, RestListRequest restListRequest);

	MeasurementConfig getMeasurementsConfig(IDashboardDatasourceDto dto);

}
