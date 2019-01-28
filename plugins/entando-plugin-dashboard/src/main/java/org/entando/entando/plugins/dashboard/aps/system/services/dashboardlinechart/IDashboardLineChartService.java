/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.model.DashboardLineChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardlinechart.model.DashboardLineChartRequest;

public interface IDashboardLineChartService {

    public String BEAN_NAME = "dashboardDashboardLineChartService";

    public PagedMetadata<DashboardLineChartDto> getDashboardLineCharts(RestListRequest requestList);

    public DashboardLineChartDto updateDashboardLineChart(DashboardLineChartRequest dashboardLineChartRequest);

    public DashboardLineChartDto addDashboardLineChart(DashboardLineChartRequest dashboardLineChartRequest);

    public void removeDashboardLineChart(int id);

    public DashboardLineChartDto getDashboardLineChart(int  id);

}

