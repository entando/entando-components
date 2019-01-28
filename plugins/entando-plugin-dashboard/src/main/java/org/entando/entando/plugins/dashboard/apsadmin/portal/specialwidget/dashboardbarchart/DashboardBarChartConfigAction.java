/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.apsadmin.portal.specialwidget.dashboardbarchart;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.IDashboardBarChartManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardBarChartConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardBarChartConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getDashboardBarChartsId() {
		try {
			List<Integer> dashboardBarCharts = this.getDashboardBarChartManager().searchDashboardBarCharts(null);
			return dashboardBarCharts;
		} catch (Throwable t) {
			_logger.error("error in getDashboardBarChartsId", t);
			throw new RuntimeException("Error getting dashboardBarCharts list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IDashboardBarChartManager getDashboardBarChartManager() {
		return _dashboardBarChartManager;
	}
	public void setDashboardBarChartManager(IDashboardBarChartManager dashboardBarChartManager) {
		this._dashboardBarChartManager = dashboardBarChartManager;
	}

	private int _id;
	private IDashboardBarChartManager _dashboardBarChartManager;
}

