/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardLineChartChangedObserver extends ObserverService {
	
	public void updateFromDashboardLineChartChanged(DashboardLineChartChangedEvent event);
	
}
