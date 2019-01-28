/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.apsadmin.portal.specialwidget.dashboardgaugechart;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.IDashboardGaugeChartManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardGaugeChartConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardGaugeChartConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getDashboardGaugeChartsId() {
		try {
			List<Integer> dashboardGaugeCharts = this.getDashboardGaugeChartManager().searchDashboardGaugeCharts(null);
			return dashboardGaugeCharts;
		} catch (Throwable t) {
			_logger.error("error in getDashboardGaugeChartsId", t);
			throw new RuntimeException("Error getting dashboardGaugeCharts list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IDashboardGaugeChartManager getDashboardGaugeChartManager() {
		return _dashboardGaugeChartManager;
	}
	public void setDashboardGaugeChartManager(IDashboardGaugeChartManager dashboardGaugeChartManager) {
		this._dashboardGaugeChartManager = dashboardGaugeChartManager;
	}

	private int _id;
	private IDashboardGaugeChartManager _dashboardGaugeChartManager;
}

