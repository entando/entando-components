/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.apsadmin.portal.specialwidget.iotconfig;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IIotConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotConfigConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IotConfigConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getIotConfigsId() {
		try {
			List<Integer> iotConfigs = this.getIotConfigManager().searchIotConfigs(null);
			return iotConfigs;
		} catch (Throwable t) {
			_logger.error("error in getIotConfigsId", t);
			throw new RuntimeException("Error getting iotConfigs list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IIotConfigManager getIotConfigManager() {
		return _iotConfigManager;
	}
	public void setIotConfigManager(IIotConfigManager iotConfigManager) {
		this._iotConfigManager = iotConfigManager;
	}

	private int _id;
	private IIotConfigManager _iotConfigManager;
}

