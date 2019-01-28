/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardGaugeChartManager {

	public DashboardGaugeChart getDashboardGaugeChart(int id) throws ApsSystemException;

	public List<Integer> getDashboardGaugeCharts() throws ApsSystemException;

	public List<Integer> searchDashboardGaugeCharts(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart) throws ApsSystemException;

	public void updateDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart) throws ApsSystemException;

	public void deleteDashboardGaugeChart(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardGaugeChart> getDashboardGaugeCharts(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}