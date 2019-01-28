/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.model.DashboardDonutChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboarddonutchart.model.DashboardDonutChartRequest;

public interface IDashboardDonutChartService {

    public String BEAN_NAME = "dashboardDashboardDonutChartService";

    public PagedMetadata<DashboardDonutChartDto> getDashboardDonutCharts(RestListRequest requestList);

    public DashboardDonutChartDto updateDashboardDonutChart(DashboardDonutChartRequest dashboardDonutChartRequest);

    public DashboardDonutChartDto addDashboardDonutChart(DashboardDonutChartRequest dashboardDonutChartRequest);

    public void removeDashboardDonutChart(int id);

    public DashboardDonutChartDto getDashboardDonutChart(int  id);

}

