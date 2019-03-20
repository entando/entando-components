package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;

public interface IDashboardDatasourceDto {

	DashboardConfigDto getDashboardConfigDto();

	String getDashboardUrl();

	String getServerType();
	
	int getDashboardId();
	
	<T extends DatasourcesConfigDto> T getDatasource();
	
	String getDatasourceCode();

	boolean supports(String serverType);
}
