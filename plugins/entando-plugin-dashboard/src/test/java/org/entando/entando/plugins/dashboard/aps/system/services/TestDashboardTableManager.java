/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.IDashboardTableManager;

public class TestDashboardTableManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardTable() {
		//TODO complete test
		assertNotNull(this._dashboardTableManager);
	}

	public void testGetDashboardTables() {
		//TODO complete test
		assertNotNull(this._dashboardTableManager);
	}
	
	public void testSearchDashboardTables() {
		//TODO complete test
		assertNotNull(this._dashboardTableManager);
	}

	public void testAddDashboardTable() {
		//TODO complete test
		assertNotNull(this._dashboardTableManager);
	}

	public void testUpdateDashboardTable() {
		//TODO complete test
		assertNotNull(this._dashboardTableManager);
	}

	public void testDeleteDashboardTable() {
		//TODO complete test
		assertNotNull(this._dashboardTableManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardTableManager = (IDashboardTableManager) this.getService("dashboardDashboardTableManager");
	}
	
	private IDashboardTableManager _dashboardTableManager;
}

