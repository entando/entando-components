/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.apsadmin.portal.specialwidget.dashboarddonutchart;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.IDashboardDonutChartManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardDonutChartConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardDonutChartConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getDashboardDonutChartsId() {
		try {
			List<Integer> dashboardDonutCharts = this.getDashboardDonutChartManager().searchDashboardDonutCharts(null);
			return dashboardDonutCharts;
		} catch (Throwable t) {
			_logger.error("error in getDashboardDonutChartsId", t);
			throw new RuntimeException("Error getting dashboardDonutCharts list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IDashboardDonutChartManager getDashboardDonutChartManager() {
		return _dashboardDonutChartManager;
	}
	public void setDashboardDonutChartManager(IDashboardDonutChartManager dashboardDonutChartManager) {
		this._dashboardDonutChartManager = dashboardDonutChartManager;
	}

	private int _id;
	private IDashboardDonutChartManager _dashboardDonutChartManager;
}

