/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardPieChartChangedObserver extends ObserverService {
	
	public void updateFromDashboardPieChartChanged(DashboardPieChartChangedEvent event);
	
}
