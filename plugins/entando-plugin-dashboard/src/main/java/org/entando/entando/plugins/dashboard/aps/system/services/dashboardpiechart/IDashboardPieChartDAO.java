/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardPieChartDAO {

	public List<Integer> searchDashboardPieCharts(FieldSearchFilter[] filters);
	
	public DashboardPieChart loadDashboardPieChart(int id);

	public List<Integer> loadDashboardPieCharts();

	public void removeDashboardPieChart(int id);
	
	public void updateDashboardPieChart(DashboardPieChart dashboardPieChart);

	public void insertDashboardPieChart(DashboardPieChart dashboardPieChart);

    public int countDashboardPieCharts(FieldSearchFilter[] filters);
}