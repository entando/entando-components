/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.model.DashboardMapDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardmap.model.DashboardMapRequest;

public interface IDashboardMapService {

    public String BEAN_NAME = "dashboardDashboardMapService";

    public PagedMetadata<DashboardMapDto> getDashboardMaps(RestListRequest requestList);

    public DashboardMapDto updateDashboardMap(DashboardMapRequest dashboardMapRequest);

    public DashboardMapDto addDashboardMap(DashboardMapRequest dashboardMapRequest);

    public void removeDashboardMap(int id);

    public DashboardMapDto getDashboardMap(int  id);

}

