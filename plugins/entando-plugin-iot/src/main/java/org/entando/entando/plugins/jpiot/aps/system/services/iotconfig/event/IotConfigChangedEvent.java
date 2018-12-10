/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IotConfig;


public class IotConfigChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((IotConfigChangedObserver) srv).updateFromIotConfigChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return IotConfigChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public IotConfig getIotConfig() {
		return _iotConfig;
	}
	public void setIotConfig(IotConfig iotConfig) {
		this._iotConfig = iotConfig;
	}

	private IotConfig _iotConfig;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
