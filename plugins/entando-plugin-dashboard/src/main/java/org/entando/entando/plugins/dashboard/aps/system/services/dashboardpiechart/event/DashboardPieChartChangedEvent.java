/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.DashboardPieChart;


public class DashboardPieChartChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardPieChartChangedObserver) srv).updateFromDashboardPieChartChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardPieChartChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardPieChart getDashboardPieChart() {
		return _dashboardPieChart;
	}
	public void setDashboardPieChart(DashboardPieChart dashboardPieChart) {
		this._dashboardPieChart = dashboardPieChart;
	}

	private DashboardPieChart _dashboardPieChart;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
