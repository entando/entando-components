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
package com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message;

import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;

public class MessageTypesAction extends BaseAction {
	
	public List<SmallMessageType> getMessageTypes() {
		List<SmallMessageType> types = null;
		try {
			types = this.getMessageManager().getSmallMessageTypes();
			this.checkTypeLables(types);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getMessageTypes");
			throw new RuntimeException("Error searching message types", t);
		}
		return types;
	}
	
	protected void checkTypeLables(List<SmallMessageType> types) {
		if (null == types) {
			return;
		}
		try {
			for (int i = 0; i < types.size(); i++) {
				SmallMessageType smallMessageType = types.get(i);
				String labelKey = "jpwebdynamicform_TITLE_" + smallMessageType.getCode();
				ApsProperties labelGroup = this.getI18nManager().getLabelGroup(labelKey);
				if (null == labelGroup) {
					this.addLabelGroups(labelKey, smallMessageType.getDescr());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "checkTypeLables");
			throw new RuntimeException("Error checking label types", t);
		}
	}
	
	protected void addLabelGroups(String key, String defaultValue) throws ApsSystemException {
		try {
			ApsProperties properties = new ApsProperties();
			Lang defaultLang = super.getLangManager().getDefaultLang();
			properties.put(defaultLang.getCode(), defaultValue);
			this.getI18nManager().addLabelGroup(key, properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addLabelGroups");
			throw new RuntimeException("Error adding label groups - key '" + key + "'", t);
		}
	}
	
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}
	
	protected IMessageManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	
	private II18nManager _i18nManager;
	private IMessageManager _messageManager;
	
}