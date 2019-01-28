/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.DashboardGaugeChart;


public class DashboardGaugeChartChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardGaugeChartChangedObserver) srv).updateFromDashboardGaugeChartChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardGaugeChartChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardGaugeChart getDashboardGaugeChart() {
		return _dashboardGaugeChart;
	}
	public void setDashboardGaugeChart(DashboardGaugeChart dashboardGaugeChart) {
		this._dashboardGaugeChart = dashboardGaugeChart;
	}

	private DashboardGaugeChart _dashboardGaugeChart;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
