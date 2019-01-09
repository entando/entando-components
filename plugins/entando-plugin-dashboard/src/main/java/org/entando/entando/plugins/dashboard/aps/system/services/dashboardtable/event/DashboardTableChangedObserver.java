/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardTableChangedObserver extends ObserverService {
	
	public void updateFromDashboardTableChanged(DashboardTableChangedEvent event);
	
}
