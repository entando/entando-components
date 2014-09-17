/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportalplus.apsadmin.tags;

import java.util.List;
import java.util.Set;

import org.entando.entando.aps.system.services.widgettype.WidgetType;
import org.entando.entando.aps.system.services.widgettype.WidgetTypeParameter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;

/**
 * Tag equals to {@link com.agiletec.apsadmin.tags.WidgetTypeInfoTag} tag.
 * Return also a "swappable" property of the type.
 * If the widget type is uncompatible with MyPortal Engine, return -1.
 * If the widget type is unswappable return 0, else return 1.
 * @author E.Santoboni
 */
public class WidgetTypeInfoTag extends com.agiletec.apsadmin.tags.WidgetTypeInfoTag {
	
	@Override
	protected Object getPropertyValue(Object masterObject, String propertyValue) {
		if (null == propertyValue || !propertyValue.equals("swappable")) {
			return super.getPropertyValue(masterObject, propertyValue);
		}
		Object value = null;
		try {
			WidgetType type = (WidgetType) masterObject;
			IMyPortalConfigManager myPortalConfigManager = (IMyPortalConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_CONFIG_MANAGER, this.pageContext);
			if (this.isCustomizable(myPortalConfigManager, type)) {
				Set<String> swappables = myPortalConfigManager.getConfig().getAllowedShowlets();
				boolean swappable = (null != swappables && swappables.contains(type.getCode()));
				value = (swappable) ? new Integer(1) : new Integer(0);
			} else {
				value = new Integer(-1);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getPropertyValue");
		}
		return value;
	}
	
	private boolean isCustomizable(IMyPortalConfigManager myPortalConfigManager, WidgetType type) {
		if (null == type) return false;
		List<WidgetTypeParameter> typeParameters = type.getTypeParameters();
		if (!type.isUserType() && !type.isLogic() && (null != typeParameters && typeParameters.size() > 0)) return false;
		if (type.getCode().equals(myPortalConfigManager.getVoidWidgetCode())) return false;
		return true;
	}
	
}