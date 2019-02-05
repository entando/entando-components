/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.DashboardMap;


public class DashboardMapChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardMapChangedObserver) srv).updateFromDashboardMapChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardMapChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardMap getDashboardMap() {
		return _dashboardMap;
	}
	public void setDashboardMap(DashboardMap dashboardMap) {
		this._dashboardMap = dashboardMap;
	}

	private DashboardMap _dashboardMap;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
