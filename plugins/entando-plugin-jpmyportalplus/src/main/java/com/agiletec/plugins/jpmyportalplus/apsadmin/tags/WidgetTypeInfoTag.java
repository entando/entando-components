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