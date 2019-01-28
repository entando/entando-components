/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.DashboardLineChart;


public class DashboardLineChartChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardLineChartChangedObserver) srv).updateFromDashboardLineChartChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardLineChartChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardLineChart getDashboardLineChart() {
		return _dashboardLineChart;
	}
	public void setDashboardLineChart(DashboardLineChart dashboardLineChart) {
		this._dashboardLineChart = dashboardLineChart;
	}

	private DashboardLineChart _dashboardLineChart;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
