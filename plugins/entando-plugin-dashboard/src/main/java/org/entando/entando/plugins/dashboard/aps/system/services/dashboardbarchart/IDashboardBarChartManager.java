/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardBarChartManager {

	public DashboardBarChart getDashboardBarChart(int id) throws ApsSystemException;

	public List<Integer> getDashboardBarCharts() throws ApsSystemException;

	public List<Integer> searchDashboardBarCharts(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardBarChart(DashboardBarChart dashboardBarChart) throws ApsSystemException;

	public void updateDashboardBarChart(DashboardBarChart dashboardBarChart) throws ApsSystemException;

	public void deleteDashboardBarChart(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardBarChart> getDashboardBarCharts(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}