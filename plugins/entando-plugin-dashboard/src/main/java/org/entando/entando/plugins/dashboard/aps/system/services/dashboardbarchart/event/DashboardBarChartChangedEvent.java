/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.DashboardBarChart;


public class DashboardBarChartChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardBarChartChangedObserver) srv).updateFromDashboardBarChartChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardBarChartChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardBarChart getDashboardBarChart() {
		return _dashboardBarChart;
	}
	public void setDashboardBarChart(DashboardBarChart dashboardBarChart) {
		this._dashboardBarChart = dashboardBarChart;
	}

	private DashboardBarChart _dashboardBarChart;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
