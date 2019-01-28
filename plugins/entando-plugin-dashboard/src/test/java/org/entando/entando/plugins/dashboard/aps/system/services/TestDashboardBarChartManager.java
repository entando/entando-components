/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.IDashboardBarChartManager;

public class TestDashboardBarChartManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardBarChart() {
		//TODO complete test
		assertNotNull(this._dashboardBarChartManager);
	}

	public void testGetDashboardBarCharts() {
		//TODO complete test
		assertNotNull(this._dashboardBarChartManager);
	}
	
	public void testSearchDashboardBarCharts() {
		//TODO complete test
		assertNotNull(this._dashboardBarChartManager);
	}

	public void testAddDashboardBarChart() {
		//TODO complete test
		assertNotNull(this._dashboardBarChartManager);
	}

	public void testUpdateDashboardBarChart() {
		//TODO complete test
		assertNotNull(this._dashboardBarChartManager);
	}

	public void testDeleteDashboardBarChart() {
		//TODO complete test
		assertNotNull(this._dashboardBarChartManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardBarChartManager = (IDashboardBarChartManager) this.getService("dashboardDashboardBarChartManager");
	}
	
	private IDashboardBarChartManager _dashboardBarChartManager;
}

