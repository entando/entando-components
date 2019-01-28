/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardBarChartDAO {

	public List<Integer> searchDashboardBarCharts(FieldSearchFilter[] filters);
	
	public DashboardBarChart loadDashboardBarChart(int id);

	public List<Integer> loadDashboardBarCharts();

	public void removeDashboardBarChart(int id);
	
	public void updateDashboardBarChart(DashboardBarChart dashboardBarChart);

	public void insertDashboardBarChart(DashboardBarChart dashboardBarChart);

    public int countDashboardBarCharts(FieldSearchFilter[] filters);
}