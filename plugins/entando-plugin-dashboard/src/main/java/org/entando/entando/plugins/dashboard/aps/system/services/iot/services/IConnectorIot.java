package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;

import java.io.IOException;
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

	List<? extends AbstractDashboardDatasourceDto> getAllDevices(DashboardConfigDto dashboardConfigDto);

	void saveMeasurementTemplate(IDashboardDatasourceDto dashboardDatasource) throws ApsSystemException;

	JsonObject saveDeviceMeasurement(IDashboardDatasourceDto dashboardDatasourceDto, JsonArray measurementBody)
			throws Exception;

  IDashboardDatasourceDto getDashboardDatasourceDtoByIdAndCode(DashboardConfigDto dashboardConfigDto, String datasourceCode);
}
