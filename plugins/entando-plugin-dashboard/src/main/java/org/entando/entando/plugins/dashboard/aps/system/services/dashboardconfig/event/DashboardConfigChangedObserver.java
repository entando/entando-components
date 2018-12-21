/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardConfigChangedObserver extends ObserverService {
	
	public void updateFromDashboardConfigChanged(DashboardConfigChangedEvent event);
	
}
