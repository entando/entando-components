/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.apsadmin.portal.specialwidget.iotlistdevices;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.IIotListDevicesManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IotListDevicesConfigAction extends SimpleWidgetConfigAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IotListDevicesConfigAction.class);
	
	protected String extractInitConfig() {
		String result = super.extractInitConfig();
		String id = this.getWidget().getConfig().getProperty("id");
		if (StringUtils.isNotBlank(id)) {
			this.setId(new Integer(id));
		}
		return result;
	}

	public List<Integer> getIotListDevicessId() {
		try {
			List<Integer> iotListDevicess = this.getIotListDevicesManager().searchIotListDevicess(null);
			return iotListDevicess;
		} catch (Throwable t) {
			_logger.error("error in getIotListDevicessId", t);
			throw new RuntimeException("Error getting iotListDevicess list", t);
		}
	}
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	protected IIotListDevicesManager getIotListDevicesManager() {
		return _iotListDevicesManager;
	}
	public void setIotListDevicesManager(IIotListDevicesManager iotListDevicesManager) {
		this._iotListDevicesManager = iotListDevicesManager;
	}

	private int _id;
	private IIotListDevicesManager _iotListDevicesManager;
}

