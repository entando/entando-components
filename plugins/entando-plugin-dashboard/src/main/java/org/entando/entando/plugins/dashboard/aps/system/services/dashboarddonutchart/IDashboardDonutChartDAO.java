/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardDonutChartDAO {

	public List<Integer> searchDashboardDonutCharts(FieldSearchFilter[] filters);
	
	public DashboardDonutChart loadDashboardDonutChart(int id);

	public List<Integer> loadDashboardDonutCharts();

	public void removeDashboardDonutChart(int id);
	
	public void updateDashboardDonutChart(DashboardDonutChart dashboardDonutChart);

	public void insertDashboardDonutChart(DashboardDonutChart dashboardDonutChart);

    public int countDashboardDonutCharts(FieldSearchFilter[] filters);
}