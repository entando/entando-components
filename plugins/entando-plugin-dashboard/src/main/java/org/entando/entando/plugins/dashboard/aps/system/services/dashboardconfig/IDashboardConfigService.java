/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.model.DashboardConfigRequest;

public interface IDashboardConfigService {

    public String BEAN_NAME = "dashboardDashboardConfigService";

    public PagedMetadata<DashboardConfigDto> getDashboardConfigs(RestListRequest requestList);

    public DashboardConfigDto updateDashboardConfig(DashboardConfigRequest dashboardConfigRequest);

    public DashboardConfigDto addDashboardConfig(DashboardConfigRequest dashboardConfigRequest);

    public void removeDashboardConfig(int id);

    public DashboardConfigDto getDashboardConfig(int  id);

}

