/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.model.DashboardTableDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardtable.model.DashboardTableRequest;

public interface IDashboardTableService {

    public String BEAN_NAME = "dashboardDashboardTableService";

    public PagedMetadata<DashboardTableDto> getDashboardTables(RestListRequest requestList);

    public DashboardTableDto updateDashboardTable(DashboardTableRequest dashboardTableRequest);

    public DashboardTableDto addDashboardTable(DashboardTableRequest dashboardTableRequest);

    public void removeDashboardTable(int id);

    public DashboardTableDto getDashboardTable(int  id);

}

