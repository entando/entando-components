/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.model.DashboardGaugeChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardgaugechart.model.DashboardGaugeChartRequest;

public interface IDashboardGaugeChartService {

    public String BEAN_NAME = "dashboardDashboardGaugeChartService";

    public PagedMetadata<DashboardGaugeChartDto> getDashboardGaugeCharts(RestListRequest requestList);

    public DashboardGaugeChartDto updateDashboardGaugeChart(DashboardGaugeChartRequest dashboardGaugeChartRequest);

    public DashboardGaugeChartDto addDashboardGaugeChart(DashboardGaugeChartRequest dashboardGaugeChartRequest);

    public void removeDashboardGaugeChart(int id);

    public DashboardGaugeChartDto getDashboardGaugeChart(int  id);

}

