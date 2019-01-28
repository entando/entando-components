/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardGaugeChartDAO {

	public List<Integer> searchDashboardGaugeCharts(FieldSearchFilter[] filters);
	
	public DashboardGaugeChart loadDashboardGaugeChart(int id);

	public List<Integer> loadDashboardGaugeCharts();

	public void removeDashboardGaugeChart(int id);
	
	public void updateDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart);

	public void insertDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart);

    public int countDashboardGaugeCharts(FieldSearchFilter[] filters);
}