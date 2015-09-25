/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpmyportalplus.apsadmin.portal;

import java.util.List;
import java.util.Set;

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;

/**
 * @author E.Santoboni
 */
public class WidgetTypeAction extends com.agiletec.apsadmin.portal.WidgetTypeAction {
	
	@Override
	public String edit() {
		String result = super.edit();
		if (null == result || !result.equals(SUCCESS)) return result;
		try {
			this.setSwappable(this.isSwappableType(this.getWidgetTypeCode()));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		String result = super.save();
		if (null == result || result.equals(FAILURE)) return FAILURE;
		this.updateWidgetTypeConfig();
		return result;
	}
	
	private String updateWidgetTypeConfig() {
		try {
			this.getWidgetTypeCode();
			boolean swappable = (null != this.getSwappable()) ? this.getSwappable().booleanValue() : false;
			if (this.isCustomizable()) {
				MyPortalConfig config = this.getMyPortalConfigManager().getConfig();
				if (swappable) {
					config.getAllowedWidgets().add(this.getWidgetTypeCode());
				} else {
					config.getAllowedWidgets().remove(this.getWidgetTypeCode());
				}
				this.getMyPortalConfigManager().saveConfig(config);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateWidgetTypeConfig");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private boolean isCustomizable() {
		WidgetType type = this.getWidgetTypeManager().getWidgetType(this.getWidgetTypeCode());
		if (null == type) return false;
		List<WidgetTypeParameter> typeParameters = type.getTypeParameters();
		if (!type.isUserType() && !type.isLogic() && (null != typeParameters && typeParameters.size() > 0)) return false;
		if (type.getCode().equals(this.getMyPortalConfigManager().getVoidWidgetCode())) return false;
		return true;
	}
	
	public boolean isSwappableType(String showletTypeCode) {
		boolean swappable = false;
		try {
			Set<String> swappables = this.getMyPortalConfigManager().getConfig().getAllowedWidgets();
			swappable = (swappables != null && swappables.contains(showletTypeCode));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "isSwappableType");
		}
		return swappable;
	}
	
	public Boolean getSwappable() {
		return _swappable;
	}
	public void setSwappable(Boolean swappable) {
		this._swappable = swappable;
	}
	
	protected IMyPortalConfigManager getMyPortalConfigManager() {
		return _myPortalConfigManager;
	}
	public void setMyPortalConfigManager(IMyPortalConfigManager myPortalConfigManager) {
		this._myPortalConfigManager = myPortalConfigManager;
	}
	
	protected Boolean _swappable;
	
	private IMyPortalConfigManager _myPortalConfigManager;
	
}