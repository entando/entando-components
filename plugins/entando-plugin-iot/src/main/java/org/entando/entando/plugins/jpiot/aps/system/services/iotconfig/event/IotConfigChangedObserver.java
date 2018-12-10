/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface IotConfigChangedObserver extends ObserverService {
	
	public void updateFromIotConfigChanged(IotConfigChangedEvent event);
	
}
