package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public interface IConnectorService {

	LinkedHashMap<String, String> getConnectorTypes() throws IOException;

	boolean pingDevice(IDashboardDatasourceDto device) throws IOException;

	<T extends DashboardConfigDto> boolean pingServer(T dashboardConfigDto) throws IOException;

	<T extends DashboardConfigDto> List<? extends AbstractDashboardDatasourceDto> getAllDevices(T dashboardConfigDto);

	/**
	 * TODO Lista struttura Misura per configurazione tramite interfaccioa web
	 */
//    <T extends AbstractDashboardDatasourceDto> MeasurementConfig getDeviceMeasurementSchema(T dashboardDatasourceDto, String loggerId);

	void setDeviceMeasurementSchema(IDashboardDatasourceDto dashboardDatasourceDto) throws ApsSystemException;

	JsonObject saveDeviceMeasurement(IDashboardDatasourceDto dashboardDatasourceDto, JsonArray measurementBody)
			throws Exception;

	IDashboardDatasourceDto getDashboardDatasourceDtobyIdAndCodeAndServerType(DashboardConfigDto dashboardConfigDto,
			String datasourceCode, String serverType);

	PagedMetadata<Map<String, Object>> getMeasurements(RestListRequest requestList);

	JsonArray getDeviceMeasurements(IDashboardDatasourceDto dto, Long nMeasurements, Date startDate, Date endDate);

//    getMe(){
//    	Apre connessione
//    	RichiedeMisure,
//    	TrasformaMisureSecondoSpecFrontEnd
//}

}
