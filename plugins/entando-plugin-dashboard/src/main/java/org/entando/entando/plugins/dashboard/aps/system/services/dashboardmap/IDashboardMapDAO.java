/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IDashboardMapDAO {

	public List<Integer> searchDashboardMaps(FieldSearchFilter[] filters);
	
	public DashboardMap loadDashboardMap(int id);

	public List<Integer> loadDashboardMaps();

	public void removeDashboardMap(int id);
	
	public void updateDashboardMap(DashboardMap dashboardMap);

	public void insertDashboardMap(DashboardMap dashboardMap);

    public int countDashboardMaps(FieldSearchFilter[] filters);
}