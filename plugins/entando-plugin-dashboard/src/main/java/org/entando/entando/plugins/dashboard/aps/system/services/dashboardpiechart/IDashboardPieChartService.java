/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.model.DashboardPieChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardpiechart.model.DashboardPieChartRequest;

public interface IDashboardPieChartService {

    public String BEAN_NAME = "dashboardDashboardPieChartService";

    public PagedMetadata<DashboardPieChartDto> getDashboardPieCharts(RestListRequest requestList);

    public DashboardPieChartDto updateDashboardPieChart(DashboardPieChartRequest dashboardPieChartRequest);

    public DashboardPieChartDto addDashboardPieChart(DashboardPieChartRequest dashboardPieChartRequest);

    public void removeDashboardPieChart(int id);

    public DashboardPieChartDto getDashboardPieChart(int  id);

}

