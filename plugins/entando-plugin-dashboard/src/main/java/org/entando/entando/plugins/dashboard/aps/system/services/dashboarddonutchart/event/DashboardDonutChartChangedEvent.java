/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.DashboardDonutChart;


public class DashboardDonutChartChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardDonutChartChangedObserver) srv).updateFromDashboardDonutChartChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardDonutChartChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardDonutChart getDashboardDonutChart() {
		return _dashboardDonutChart;
	}
	public void setDashboardDonutChart(DashboardDonutChart dashboardDonutChart) {
		this._dashboardDonutChart = dashboardDonutChart;
	}

	private DashboardDonutChart _dashboardDonutChart;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
