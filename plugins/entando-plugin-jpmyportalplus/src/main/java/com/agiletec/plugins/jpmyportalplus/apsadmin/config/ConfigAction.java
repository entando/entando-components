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
package com.agiletec.plugins.jpmyportalplus.apsadmin.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.AbstractPortalAction;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.beanutils.BeanComparator;

/**
 * @author E.Santoboni
 */
public class ConfigAction extends AbstractPortalAction {
	
	@Override
	public void validate() {
		super.validate();
		Set<String> widgetTypeCodes = this.getWidgetTypeCodes();
		IWidgetTypeManager widgetTypeManager = this.getWidgetTypeManager();
		for (String widgetCode : widgetTypeCodes) {
			if (!this.isWidgetAllowed(widgetTypeManager.getWidgetType(widgetCode))) {
				this.addFieldError("showlets", this.getText("errors.jpmyportalConfig.widgets.notValid", new String[] { widgetCode }));
			}
		}
	}
	
	public String edit() {
		try {
			MyPortalConfig config = this.getMyPortalConfigManager().getConfig();
			this.populateForm(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Deprecated
	public String addShowlet() {
		return addWidget();
	}
	
	public String addWidget() {
		try {
			String widgetCode = this.getWidgetCode();
			WidgetType type = this.getWidgetTypeManager().getWidgetType(widgetCode);
			if (this.isWidgetAllowed(type)) {
				this.getWidgetTypeCodes().add(widgetCode);
			} else {
				this.addFieldError("showletCode", this.getText("Errors.jpmyportalConfig.WidgetType.notValid", new String[] { widgetCode }));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addWidget");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Deprecated
	public String removeShowlet() {
		return removeWidget();
	}
	
	public String removeWidget() {
		try {
			String widgetCode = this.getWidgetCode();
			if (widgetCode != null) {
				this.getWidgetTypeCodes().remove(widgetCode);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeWidget");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String save() {
		try {
			MyPortalConfig config = this.prepareConfig();
			this.getMyPortalConfigManager().saveConfig(config);
			this.addActionMessage(this.getText("jpmyportalplus.message.configSavedOk"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private void populateForm(MyPortalConfig config) {
		Set<String> allowedWidgets = config.getAllowedWidgets();
		IWidgetTypeManager widgetTypeManager = this.getWidgetTypeManager();
		Set<String> widgets = new TreeSet<String>();
		for (String widgetCode : allowedWidgets) {
			if (this.isWidgetAllowed(widgetTypeManager.getWidgetType(widgetCode))) {
				widgets.add(widgetCode);
			}
		}
		this.setWidgetTypeCodes(widgets);
	}
	
	@Override
	protected void addFlavourWidgetType(String mapCode, WidgetType type, Map<String, List<SelectItem>> mapping) {
		if (null == type) return;
		List<WidgetTypeParameter> typeParameters = type.getTypeParameters();
		if (!type.isUserType() && !type.isLogic() && (null != typeParameters && typeParameters.size() > 0)) return;
		if (type.getCode().equals(this.getMyPortalConfigManager().getVoidWidgetCode())) return;
		super.addFlavourWidgetType(mapCode, type, mapping);
	}
	
	private MyPortalConfig prepareConfig() throws ApsSystemException {
		MyPortalConfig config = new MyPortalConfig();
		config.setAllowedWidgets(this.getWidgetTypeCodes());
		return config;
	}
	
	public WidgetType getWidgetType(String widgetCode) {
		return this.getWidgetTypeManager().getWidgetType(widgetCode);
	}
	
	@Deprecated
	public WidgetType getShowletType(String showletCode) {
		return this.getWidgetType(showletCode);
	}
	
	private boolean isWidgetAllowed(WidgetType widgetType) {
		return widgetType != null && 
				(widgetType.isLogic() || widgetType.isUserType() || widgetType.getTypeParameters() == null || widgetType.getTypeParameters().isEmpty());
	}
	
	@Deprecated
	public Set<String> getShowletTypeCodes() {
		return this.getWidgetTypeCodes();
	}
	@Deprecated
	public void setShowletTypeCodes(Set<String> showletTypeCodes) {
		this.setWidgetTypeCodes(showletTypeCodes);
	}
	
	public List<SelectItem> getWidgetTypeItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		Set<String> widgetCodes = this.getWidgetTypeCodes();
		if (null != widgetCodes) {
			for (String widgetCode : widgetCodes) {
				WidgetType widgetType = this.getWidgetType(widgetCode);
				if (null != widgetType) {
					items.add(new SelectItem(widgetCode, super.getTitle(widgetCode, widgetType.getTitles()), widgetType.getPluginCode()));
				}
			}
			BeanComparator c = new BeanComparator("value");
			Collections.sort(items, c);
		}
		return items;
	}
	
	public Set<String> getWidgetTypeCodes() {
		return _widgetTypeCodes;
	}
	public void setWidgetTypeCodes(Set<String> widgetTypeCodes) {
		this._widgetTypeCodes = widgetTypeCodes;
	}
	
	@Deprecated
	public String getShowletCode() {
		return this.getWidgetCode();
	}
	@Deprecated
	public void setShowletCode(String showletCode) {
		this.setWidgetCode(showletCode);
	}
	
	public String getWidgetCode() {
		return _widgetCode;
	}
	public void setWidgetCode(String widgetCode) {
		this._widgetCode = widgetCode;
	}
	
	protected IMyPortalConfigManager getMyPortalConfigManager() {
		return _myPortalConfigManager;
	}
	public void setMyPortalConfigManager(IMyPortalConfigManager myPortalConfigManager) {
		this._myPortalConfigManager = myPortalConfigManager;
	}
	
	private Set<String> _widgetTypeCodes = new TreeSet<String>();
	
	private String _widgetCode;
	
	private IMyPortalConfigManager _myPortalConfigManager;
	
}