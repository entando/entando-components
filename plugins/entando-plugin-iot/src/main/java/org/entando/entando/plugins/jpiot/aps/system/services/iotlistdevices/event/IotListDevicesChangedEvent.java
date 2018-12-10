/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.event;

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IotListDevices;


public class IotListDevicesChangedEvent extends ApsEvent {
	
	@Override
	public void notify(IManager srv) {
		((IotListDevicesChangedObserver) srv).updateFromIotListDevicesChanged(this);
	}
	
	@Override
	public Class getObserverInterface() {
		return IotListDevicesChangedObserver.class;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public IotListDevices getIotListDevices() {
		return _iotListDevices;
	}
	public void setIotListDevices(IotListDevices iotListDevices) {
		this._iotListDevices = iotListDevices;
	}

	private IotListDevices _iotListDevices;
	private int _operationCode;
	
	public static final int INSERT_OPERATION_CODE = 1;
	public static final int REMOVE_OPERATION_CODE = 2;
	public static final int UPDATE_OPERATION_CODE = 3;

}
