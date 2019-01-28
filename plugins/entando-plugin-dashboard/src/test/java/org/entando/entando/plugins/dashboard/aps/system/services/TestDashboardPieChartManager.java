/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.IDashboardPieChartManager;

public class TestDashboardPieChartManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardPieChart() {
		//TODO complete test
		assertNotNull(this._dashboardPieChartManager);
	}

	public void testGetDashboardPieCharts() {
		//TODO complete test
		assertNotNull(this._dashboardPieChartManager);
	}
	
	public void testSearchDashboardPieCharts() {
		//TODO complete test
		assertNotNull(this._dashboardPieChartManager);
	}

	public void testAddDashboardPieChart() {
		//TODO complete test
		assertNotNull(this._dashboardPieChartManager);
	}

	public void testUpdateDashboardPieChart() {
		//TODO complete test
		assertNotNull(this._dashboardPieChartManager);
	}

	public void testDeleteDashboardPieChart() {
		//TODO complete test
		assertNotNull(this._dashboardPieChartManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardPieChartManager = (IDashboardPieChartManager) this.getService("dashboardDashboardPieChartManager");
	}
	
	private IDashboardPieChartManager _dashboardPieChartManager;
}

