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

    DashboardConfig getDashboardConfig(int id) throws ApsSystemException;

    List<Integer> getDashboardConfigs() throws ApsSystemException;

    List<Integer> searchDashboardConfigs(FieldSearchFilter filters[]) throws ApsSystemException;

    void addDashboardConfig(DashboardConfig dashboardConfig) throws ApsSystemException;

    void updateDashboardConfig(DashboardConfig dashboardConfig) throws ApsSystemException;

    void deleteDashboardConfig(int id) throws ApsSystemException;

    SearcherDaoPaginatedResult<DashboardConfig> getDashboardConfigs(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}