/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.apsadmin.portal.specialwidget.dashboardmap;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.IDashboardMapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DashboardMapConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(DashboardMapConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getDashboardMapsId() {
		try {
			List<Integer> dashboardMaps = this.getDashboardMapManager().searchDashboardMaps(null);
			return dashboardMaps;
		} catch (Throwable t) {
			_logger.error("error in getDashboardMapsId", t);
			throw new RuntimeException("Error getting dashboardMaps list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IDashboardMapManager getDashboardMapManager() {
		return _dashboardMapManager;
	}
	public void setDashboardMapManager(IDashboardMapManager dashboardMapManager) {
		this._dashboardMapManager = dashboardMapManager;
	}

	private int _id;
	private IDashboardMapManager _dashboardMapManager;
}

