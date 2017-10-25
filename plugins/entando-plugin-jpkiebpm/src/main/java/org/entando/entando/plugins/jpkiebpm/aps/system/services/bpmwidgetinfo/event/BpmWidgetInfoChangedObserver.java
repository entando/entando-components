/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.bpmwidgetinfo.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface BpmWidgetInfoChangedObserver extends ObserverService {
	
	public void updateFromBpmWidgetInfoChanged(BpmWidgetInfoChangedEvent event);
	
}
