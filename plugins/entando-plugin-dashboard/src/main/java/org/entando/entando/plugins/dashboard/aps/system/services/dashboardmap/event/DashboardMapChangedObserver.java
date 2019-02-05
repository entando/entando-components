/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardMapChangedObserver extends ObserverService {
	
	public void updateFromDashboardMapChanged(DashboardMapChangedEvent event);
	
}
