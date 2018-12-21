/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardConfigDAO {

	public List<Integer> searchDashboardConfigs(FieldSearchFilter[] filters);
	
	public DashboardConfig loadDashboardConfig(int id);

	public List<Integer> loadDashboardConfigs();

	public void removeDashboardConfig(int id);
	
	public void updateDashboardConfig(DashboardConfig dashboardConfig);

	public void insertDashboardConfig(DashboardConfig dashboardConfig);

    public int countDashboardConfigs(FieldSearchFilter[] filters);
}