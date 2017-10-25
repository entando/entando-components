/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.BpmWidgetInfo;


public class BpmWidgetInfoChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((BpmWidgetInfoChangedObserver) srv).updateFromBpmWidgetInfoChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return BpmWidgetInfoChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public BpmWidgetInfo getBpmWidgetInfo() {
		return _bpmWidgetInfo;
	}
	public void setBpmWidgetInfo(BpmWidgetInfo bpmWidgetInfo) {
		this._bpmWidgetInfo = bpmWidgetInfo;
	}

	private BpmWidgetInfo _bpmWidgetInfo;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
