/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;

public interface IDashboardConfigService {

    String BEAN_NAME = "dashboardDashboardConfigService";

    PagedMetadata<DashboardConfigDto> getDashboardConfigs(RestListRequest requestList);

    DashboardConfigDto updateDashboardConfig(DashboardConfigRequest dashboardConfigRequest);

    DashboardConfigDto addDashboardConfig(DashboardConfigRequest dashboardConfigRequest);

    void removeDashboardConfig(int id);

    DashboardConfigDto getDashboardConfig(int id);

}

