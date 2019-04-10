package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
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

	boolean pingDevice(IDashboardDatasourceDto dashboardDatasourceDto) throws IOException;

	List<DatasourcesConfigDto> getAllDevices(DashboardConfigDto dashboardConfigDto) throws ApiResourceNotAvailableException;
 
	void saveMeasurementTemplate(IDashboardDatasourceDto dashboardDatasource)
			throws ApsSystemException;

	void saveDeviceMeasurement(IDashboardDatasourceDto dashboardDatasourceDto,
			String measurementBody);

	List<Map<String, Object>> getMeasurements(IDashboardDatasourceDto dashboardSitewhereDatasourceDto,
			Date startDate, Date endDate, RestListRequest restListRequest) throws RuntimeException;

	MeasurementConfig getMeasurementConfig(IDashboardDatasourceDto dto);

	MeasurementTemplate getDeviceMeasurementSchema(IDashboardDatasourceDto dto) throws ApiResourceNotAvailableException;

	ServerType getServerType();

	DashboardConfigRequest setDevicesMetadata(DashboardConfigRequest dashboardConfigRequest)
      throws ApsSystemException;
}
