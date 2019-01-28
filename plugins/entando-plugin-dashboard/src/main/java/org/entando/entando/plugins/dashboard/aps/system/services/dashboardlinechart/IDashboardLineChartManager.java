/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardLineChartManager {

	public DashboardLineChart getDashboardLineChart(int id) throws ApsSystemException;

	public List<Integer> getDashboardLineCharts() throws ApsSystemException;

	public List<Integer> searchDashboardLineCharts(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardLineChart(DashboardLineChart dashboardLineChart) throws ApsSystemException;

	public void updateDashboardLineChart(DashboardLineChart dashboardLineChart) throws ApsSystemException;

	public void deleteDashboardLineChart(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardLineChart> getDashboardLineCharts(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}