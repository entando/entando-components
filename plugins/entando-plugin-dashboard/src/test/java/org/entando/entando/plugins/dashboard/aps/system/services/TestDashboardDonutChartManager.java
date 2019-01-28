/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.IDashboardDonutChartManager;

public class TestDashboardDonutChartManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardDonutChart() {
		//TODO complete test
		assertNotNull(this._dashboardDonutChartManager);
	}

	public void testGetDashboardDonutCharts() {
		//TODO complete test
		assertNotNull(this._dashboardDonutChartManager);
	}
	
	public void testSearchDashboardDonutCharts() {
		//TODO complete test
		assertNotNull(this._dashboardDonutChartManager);
	}

	public void testAddDashboardDonutChart() {
		//TODO complete test
		assertNotNull(this._dashboardDonutChartManager);
	}

	public void testUpdateDashboardDonutChart() {
		//TODO complete test
		assertNotNull(this._dashboardDonutChartManager);
	}

	public void testDeleteDashboardDonutChart() {
		//TODO complete test
		assertNotNull(this._dashboardDonutChartManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardDonutChartManager = (IDashboardDonutChartManager) this.getService("dashboardDashboardDonutChartManager");
	}
	
	private IDashboardDonutChartManager _dashboardDonutChartManager;
}

