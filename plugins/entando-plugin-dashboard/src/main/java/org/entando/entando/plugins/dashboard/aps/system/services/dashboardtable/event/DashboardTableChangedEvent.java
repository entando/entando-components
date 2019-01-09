/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.DashboardTable;


public class DashboardTableChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((DashboardTableChangedObserver) srv).updateFromDashboardTableChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return DashboardTableChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public DashboardTable getDashboardTable() {
		return _dashboardTable;
	}
	public void setDashboardTable(DashboardTable dashboardTable) {
		this._dashboardTable = dashboardTable;
	}

	private DashboardTable _dashboardTable;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
