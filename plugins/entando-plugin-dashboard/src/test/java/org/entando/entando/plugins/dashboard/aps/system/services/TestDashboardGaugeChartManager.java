/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services;

import org.entando.entando.plugins.dashboard.aps.DashboardBaseTestCase;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.IDashboardGaugeChartManager;

public class TestDashboardGaugeChartManager extends DashboardBaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetDashboardGaugeChart() {
		//TODO complete test
		assertNotNull(this._dashboardGaugeChartManager);
	}

	public void testGetDashboardGaugeCharts() {
		//TODO complete test
		assertNotNull(this._dashboardGaugeChartManager);
	}
	
	public void testSearchDashboardGaugeCharts() {
		//TODO complete test
		assertNotNull(this._dashboardGaugeChartManager);
	}

	public void testAddDashboardGaugeChart() {
		//TODO complete test
		assertNotNull(this._dashboardGaugeChartManager);
	}

	public void testUpdateDashboardGaugeChart() {
		//TODO complete test
		assertNotNull(this._dashboardGaugeChartManager);
	}

	public void testDeleteDashboardGaugeChart() {
		//TODO complete test
		assertNotNull(this._dashboardGaugeChartManager);
	}
	
	private void init() {
		//TODO add the spring bean id as constant
		this._dashboardGaugeChartManager = (IDashboardGaugeChartManager) this.getService("dashboardDashboardGaugeChartManager");
	}
	
	private IDashboardGaugeChartManager _dashboardGaugeChartManager;
}

