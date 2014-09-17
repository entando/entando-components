/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform.apsadmin.portal.specialwidget.webdynamicform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.portal.specialwidget.SimpleWidgetConfigAction;

import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;

public class WebdynamicformWidgetAction extends SimpleWidgetConfigAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			String typeCode = this.getTypeCode();
			if (typeCode == null || this.getMessageManager().getSmallMessageTypesMap().get(typeCode) == null) {
				this.addFieldError("typeCode", this.getText("Errors.typeCode.required"));
			} else {
				List<String> protectionTypes = Arrays.asList(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPES);
				if (null != this.getFormProtectionType() && this.getFormProtectionType().trim().length() > 0) {
					if (!protectionTypes.contains(this.getFormProtectionType())) {
						this.addFieldError("formProtectionType", this.getText("Errors.formProtectionType.invalid"));
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate");
		}
	}
	
	@Override
	public String init() {
		String result = super.init();
		try {
			if (result.equals(SUCCESS)) {
				ApsProperties config = this.getWidget().getConfig();
				String typeCode = config.getProperty(JpwebdynamicformSystemConstants.TYPECODE_WIDGET_PARAM);
				this.setTypeCode(typeCode);
				String protectionType = config.getProperty(JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPE_WIDGET_PARAM);
				this.setFormProtectionType(protectionType);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "init");
			return FAILURE;
		}
		return result;
	}
	
	public List<SmallMessageType> getMessageTypes() {
		try {
			return this.getMessageManager().getSmallMessageTypes();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getMessageTypes");
			throw new RuntimeException("Error searching message types", t);
		}
	}
	
	public List<SelectItem> getFormProtectionTypeSelectItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		for (int i = 0; i < JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPES.length; i++) {
			String key = JpwebdynamicformSystemConstants.FORM_PROTECTION_TYPES[i];
			items.add(new SelectItem(key, this.getText("label.formProtectionType." + key)));
		}
		return items;
	}
	
	public String getTypeCode() {
		return _typeCode;
	}
	public void setTypeCode(String typeCode) {
		this._typeCode = typeCode;
	}
	
	public String getFormProtectionType() {
		return _formProtectionType;
	}
	public void setFormProtectionType(String formProtectionType) {
		this._formProtectionType = formProtectionType;
	}
	
	protected IMessageManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	
	private String _typeCode;
	private String _formProtectionType;
	
	private IMessageManager _messageManager;
	
}