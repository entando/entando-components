/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfig;


public class DashboardConfigChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardConfigChangedObserver) srv).updateFromDashboardConfigChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardConfigChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardConfig getDashboardConfig() {
		return _dashboardConfig;
	}
	public void setDashboardConfig(DashboardConfig dashboardConfig) {
		this._dashboardConfig = dashboardConfig;
	}

	private DashboardConfig _dashboardConfig;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
