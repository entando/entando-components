/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import com.agiletec.aps.system.common.FieldSearchFilter;

import java.util.List;

public interface IDashboardConfigDAO {

    List<Integer> searchDashboardConfigs(FieldSearchFilter[] filters);

    DashboardConfig loadDashboardConfig(int id);

    List<Integer> loadDashboardConfigs();

    void removeDashboardConfig(int id);

    void updateDashboardConfig(DashboardConfig dashboardConfig);

    void insertDashboardConfig(DashboardConfig dashboardConfig);

    int countDashboardConfigs(FieldSearchFilter[] filters);
}