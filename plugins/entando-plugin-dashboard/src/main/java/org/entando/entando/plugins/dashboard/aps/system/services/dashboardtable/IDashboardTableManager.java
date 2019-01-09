/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardTableManager {

	public DashboardTable getDashboardTable(int id) throws ApsSystemException;

	public List<Integer> getDashboardTables() throws ApsSystemException;

	public List<Integer> searchDashboardTables(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardTable(DashboardTable dashboardTable) throws ApsSystemException;

	public void updateDashboardTable(DashboardTable dashboardTable) throws ApsSystemException;

	public void deleteDashboardTable(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardTable> getDashboardTables(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}