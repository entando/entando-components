/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardDonutChartChangedObserver extends ObserverService {
	
	public void updateFromDashboardDonutChartChanged(DashboardDonutChartChangedEvent event);
	
}
