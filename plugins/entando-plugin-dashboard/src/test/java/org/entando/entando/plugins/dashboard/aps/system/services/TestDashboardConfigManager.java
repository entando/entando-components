/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigManager;

public class TestDashboardConfigManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardConfig() {
		//TODO complete test
		assertNotNull(this._dashboardConfigManager);
	}

	public void testGetDashboardConfigs() {
		//TODO complete test
		assertNotNull(this._dashboardConfigManager);
	}
	
	public void testSearchDashboardConfigs() {
		//TODO complete test
		assertNotNull(this._dashboardConfigManager);
	}

	public void testAddDashboardConfig() {
		//TODO complete test
		assertNotNull(this._dashboardConfigManager);
	}

	public void testUpdateDashboardConfig() {
		//TODO complete test
		assertNotNull(this._dashboardConfigManager);
	}

	public void testDeleteDashboardConfig() {
		//TODO complete test
		assertNotNull(this._dashboardConfigManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardConfigManager = (IDashboardConfigManager) this.getService("dashboardDashboardConfigManager");
	}
	
	private IDashboardConfigManager _dashboardConfigManager;
}

