/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.apsadmin.portal.specialwidget.dashboardlinechart;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.IDashboardLineChartManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardLineChartConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardLineChartConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getDashboardLineChartsId() {
		try {
			List<Integer> dashboardLineCharts = this.getDashboardLineChartManager().searchDashboardLineCharts(null);
			return dashboardLineCharts;
		} catch (Throwable t) {
			_logger.error("error in getDashboardLineChartsId", t);
			throw new RuntimeException("Error getting dashboardLineCharts list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IDashboardLineChartManager getDashboardLineChartManager() {
		return _dashboardLineChartManager;
	}
	public void setDashboardLineChartManager(IDashboardLineChartManager dashboardLineChartManager) {
		this._dashboardLineChartManager = dashboardLineChartManager;
	}

	private int _id;
	private IDashboardLineChartManager _dashboardLineChartManager;
}

