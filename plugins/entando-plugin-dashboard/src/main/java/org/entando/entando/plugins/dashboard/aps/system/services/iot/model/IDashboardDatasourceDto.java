package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;

public interface IDashboardDatasourceDto {

	DashboardConfigDto getDashboardConfigDto();

	DatasourcesConfigDto getDatasourcesConfigDto();
	
	String getDashboardUrl();

	ServerType getServerType();
	
	int getDashboardId();
	
	String getDatasourceCode();

	boolean supports(String serverType);

	String getDatasourceUrl();
}
