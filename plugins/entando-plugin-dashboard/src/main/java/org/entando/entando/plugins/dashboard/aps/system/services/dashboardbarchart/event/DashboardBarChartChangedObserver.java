/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardBarChartChangedObserver extends ObserverService {
	
	public void updateFromDashboardBarChartChanged(DashboardBarChartChangedEvent event);
	
}
