/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable;

import com.agiletec.aps.system.common.FieldSearchFilter;

import java.util.List;

public interface IDashboardTableDAO {

    List<Integer> searchDashboardTables(FieldSearchFilter[] filters);

    DashboardTable loadDashboardTable(int id);

    List<Integer> loadDashboardTables();

    void removeDashboardTable(int id);

    void updateDashboardTable(DashboardTable dashboardTable);

    void insertDashboardTable(DashboardTable dashboardTable);

    int countDashboardTables(FieldSearchFilter[] filters);
}