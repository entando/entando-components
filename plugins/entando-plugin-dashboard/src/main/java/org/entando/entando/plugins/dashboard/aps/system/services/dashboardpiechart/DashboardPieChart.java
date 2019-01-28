/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart;



public class DashboardPieChart {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getWidgetId() {
		return _widgetId;
	}
	public void setWidgetId(String widgetId) {
		this._widgetId = widgetId;
	}

	
	private int _id;
	private String _widgetId;

}
