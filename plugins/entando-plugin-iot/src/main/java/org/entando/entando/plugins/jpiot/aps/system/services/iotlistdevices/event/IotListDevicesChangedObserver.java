/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface IotListDevicesChangedObserver extends ObserverService {
	
	public void updateFromIotListDevicesChanged(IotListDevicesChangedEvent event);
	
}
