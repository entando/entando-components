/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardLineChartDAO {

	public List<Integer> searchDashboardLineCharts(FieldSearchFilter[] filters);
	
	public DashboardLineChart loadDashboardLineChart(int id);

	public List<Integer> loadDashboardLineCharts();

	public void removeDashboardLineChart(int id);
	
	public void updateDashboardLineChart(DashboardLineChart dashboardLineChart);

	public void insertDashboardLineChart(DashboardLineChart dashboardLineChart);

    public int countDashboardLineCharts(FieldSearchFilter[] filters);
}