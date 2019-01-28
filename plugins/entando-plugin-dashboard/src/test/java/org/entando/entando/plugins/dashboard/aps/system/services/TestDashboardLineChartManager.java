/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.IDashboardLineChartManager;

public class TestDashboardLineChartManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardLineChart() {
		//TODO complete test
		assertNotNull(this._dashboardLineChartManager);
	}

	public void testGetDashboardLineCharts() {
		//TODO complete test
		assertNotNull(this._dashboardLineChartManager);
	}
	
	public void testSearchDashboardLineCharts() {
		//TODO complete test
		assertNotNull(this._dashboardLineChartManager);
	}

	public void testAddDashboardLineChart() {
		//TODO complete test
		assertNotNull(this._dashboardLineChartManager);
	}

	public void testUpdateDashboardLineChart() {
		//TODO complete test
		assertNotNull(this._dashboardLineChartManager);
	}

	public void testDeleteDashboardLineChart() {
		//TODO complete test
		assertNotNull(this._dashboardLineChartManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardLineChartManager = (IDashboardLineChartManager) this.getService("dashboardDashboardLineChartManager");
	}
	
	private IDashboardLineChartManager _dashboardLineChartManager;
}

