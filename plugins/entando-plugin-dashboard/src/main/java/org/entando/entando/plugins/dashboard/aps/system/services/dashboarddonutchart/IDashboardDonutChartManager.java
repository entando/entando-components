/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardDonutChartManager {

	public DashboardDonutChart getDashboardDonutChart(int id) throws ApsSystemException;

	public List<Integer> getDashboardDonutCharts() throws ApsSystemException;

	public List<Integer> searchDashboardDonutCharts(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardDonutChart(DashboardDonutChart dashboardDonutChart) throws ApsSystemException;

	public void updateDashboardDonutChart(DashboardDonutChart dashboardDonutChart) throws ApsSystemException;

	public void deleteDashboardDonutChart(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardDonutChart> getDashboardDonutCharts(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}