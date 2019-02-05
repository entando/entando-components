/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.IDashboardMapManager;

public class TestDashboardMapManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardMap() {
		//TODO complete test
		assertNotNull(this._dashboardMapManager);
	}

	public void testGetDashboardMaps() {
		//TODO complete test
		assertNotNull(this._dashboardMapManager);
	}
	
	public void testSearchDashboardMaps() {
		//TODO complete test
		assertNotNull(this._dashboardMapManager);
	}

	public void testAddDashboardMap() {
		//TODO complete test
		assertNotNull(this._dashboardMapManager);
	}

	public void testUpdateDashboardMap() {
		//TODO complete test
		assertNotNull(this._dashboardMapManager);
	}

	public void testDeleteDashboardMap() {
		//TODO complete test
		assertNotNull(this._dashboardMapManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardMapManager = (IDashboardMapManager) this.getService("dashboardDashboardMapManager");
	}
	
	private IDashboardMapManager _dashboardMapManager;
}

