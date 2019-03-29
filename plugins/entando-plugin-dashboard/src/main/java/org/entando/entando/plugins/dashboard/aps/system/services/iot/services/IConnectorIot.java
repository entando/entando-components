package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IConnectorIot {

	/**
	 * Returns true is the implementation of this processor fits the provided widget
	 * code
	 * 
	 * @param connectorType
	 * @return
	 */
	boolean supports(String connectorType);

	boolean pingDevice(IDashboardDatasourceDto dashboardDatasourceDto) throws IOException;

	List<DatasourcesConfigDto> getAllDevices(DashboardConfigDto dashboardConfigDto);

	void saveMeasurementTemplate(IDashboardDatasourceDto dashboardDatasource) throws ApsSystemException;

	void saveDeviceMeasurement(IDashboardDatasourceDto dashboardDatasourceDto, String measurementBody);

	PagedMetadata<MeasurementObject> getMeasurements(IDashboardDatasourceDto dashboardSitewhereDatasourceDto, Long nMeasurements,
			Date startDate, Date endDate, RestListRequest restListRequest)  throws RuntimeException;

	MeasurementConfig getMeasurementConfig(IDashboardDatasourceDto dto);

	MeasurementTemplate getDeviceMeasurementSchema(IDashboardDatasourceDto dto);
}
