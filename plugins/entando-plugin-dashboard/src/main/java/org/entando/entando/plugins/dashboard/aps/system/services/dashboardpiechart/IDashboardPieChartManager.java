/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardPieChartManager {

	public DashboardPieChart getDashboardPieChart(int id) throws ApsSystemException;

	public List<Integer> getDashboardPieCharts() throws ApsSystemException;

	public List<Integer> searchDashboardPieCharts(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardPieChart(DashboardPieChart dashboardPieChart) throws ApsSystemException;

	public void updateDashboardPieChart(DashboardPieChart dashboardPieChart) throws ApsSystemException;

	public void deleteDashboardPieChart(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardPieChart> getDashboardPieCharts(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}