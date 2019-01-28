/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.model.DashboardBarChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardbarchart.model.DashboardBarChartRequest;

public interface IDashboardBarChartService {

    public String BEAN_NAME = "dashboardDashboardBarChartService";

    public PagedMetadata<DashboardBarChartDto> getDashboardBarCharts(RestListRequest requestList);

    public DashboardBarChartDto updateDashboardBarChart(DashboardBarChartRequest dashboardBarChartRequest);

    public DashboardBarChartDto addDashboardBarChart(DashboardBarChartRequest dashboardBarChartRequest);

    public void removeDashboardBarChart(int id);

    public DashboardBarChartDto getDashboardBarChart(int  id);

}

