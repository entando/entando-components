/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.event;

import com.agiletec.aps.system.common.notify.ObserverService;

public interface DashboardGaugeChartChangedObserver extends ObserverService {
	
	public void updateFromDashboardGaugeChartChanged(DashboardGaugeChartChangedEvent event);
	
}
