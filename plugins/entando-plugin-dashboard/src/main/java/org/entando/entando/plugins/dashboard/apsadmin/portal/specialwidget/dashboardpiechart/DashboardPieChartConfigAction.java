/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.apsadmin.portal.specialwidget.dashboardpiechart;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.IDashboardPieChartManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardPieChartConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardPieChartConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getDashboardPieChartsId() {
		try {
			List<Integer> dashboardPieCharts = this.getDashboardPieChartManager().searchDashboardPieCharts(null);
			return dashboardPieCharts;
		} catch (Throwable t) {
			_logger.error("error in getDashboardPieChartsId", t);
			throw new RuntimeException("Error getting dashboardPieCharts list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IDashboardPieChartManager getDashboardPieChartManager() {
		return _dashboardPieChartManager;
	}
	public void setDashboardPieChartManager(IDashboardPieChartManager dashboardPieChartManager) {
		this._dashboardPieChartManager = dashboardPieChartManager;
	}

	private int _id;
	private IDashboardPieChartManager _dashboardPieChartManager;
}

