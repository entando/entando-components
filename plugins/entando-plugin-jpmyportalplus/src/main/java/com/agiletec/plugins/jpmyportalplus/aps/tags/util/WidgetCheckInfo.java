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
package com.agiletec.plugins.jpmyportalplus.aps.tags.util;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.services.lang.Lang;

/**
 * @author E.Santoboni
 */
public class WidgetCheckInfo {
	
	public WidgetCheckInfo(WidgetType widgetType, Boolean checked, Lang lang) {
		this._widgetType = widgetType;
		this._checked = checked;
		this._lang = lang;
	}
	
	public WidgetType getWidgetType() {
		return _widgetType;
	}
	
	public Boolean getChecked() {
		return _checked;
	}
	
	public Lang getLang() {
		return _lang;
	}
	
	public String getCode() {
		return this._widgetType.getCode();
	}
	
	public String getTitle() {
		String title = this._widgetType.getTitles().getProperty(this._lang.getCode());
		if (null != title) {
			return title;
		}
		return this.getCode();
	}
	
	private WidgetType _widgetType;
	private Boolean _checked;
	private Lang _lang;
	
}
