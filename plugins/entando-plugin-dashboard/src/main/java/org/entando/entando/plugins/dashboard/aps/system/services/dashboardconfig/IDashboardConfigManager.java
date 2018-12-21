/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardConfigManager {

	public DashboardConfig getDashboardConfig(int id) throws ApsSystemException;

	public List<Integer> getDashboardConfigs() throws ApsSystemException;

	public List<Integer> searchDashboardConfigs(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardConfig(DashboardConfig dashboardConfig) throws ApsSystemException;

	public void updateDashboardConfig(DashboardConfig dashboardConfig) throws ApsSystemException;

	public void deleteDashboardConfig(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardConfig> getDashboardConfigs(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}