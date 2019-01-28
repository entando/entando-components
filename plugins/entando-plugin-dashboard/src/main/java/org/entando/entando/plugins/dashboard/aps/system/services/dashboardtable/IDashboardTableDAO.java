/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardTableDAO {

	public List<Integer> searchDashboardTables(FieldSearchFilter[] filters);
	
	public DashboardTable loadDashboardTable(int id);

	public List<Integer> loadDashboardTables();

	public void removeDashboardTable(int id);
	
	public void updateDashboardTable(DashboardTable dashboardTable);

	public void insertDashboardTable(DashboardTable dashboardTable);

    public int countDashboardTables(FieldSearchFilter[] filters);
}