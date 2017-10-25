/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface KieFormOverrideChangedObserver extends ObserverService {

	public void updateFromKieFormOverrideChanged(KieFormOverrideChangedEvent event);

}
