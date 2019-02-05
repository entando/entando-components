/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardMapManager {

	public DashboardMap getDashboardMap(int id) throws ApsSystemException;

	public List<Integer> getDashboardMaps() throws ApsSystemException;

	public List<Integer> searchDashboardMaps(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addDashboardMap(DashboardMap dashboardMap) throws ApsSystemException;

	public void updateDashboardMap(DashboardMap dashboardMap) throws ApsSystemException;

	public void deleteDashboardMap(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<DashboardMap> getDashboardMaps(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}