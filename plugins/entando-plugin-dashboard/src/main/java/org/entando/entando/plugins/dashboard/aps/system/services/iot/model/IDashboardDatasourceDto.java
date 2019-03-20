package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;

public interface IDashboardDatasourceDto {

	DashboardConfigDto getDashboardConfigDto();

	String getDashboardUrl();

}
