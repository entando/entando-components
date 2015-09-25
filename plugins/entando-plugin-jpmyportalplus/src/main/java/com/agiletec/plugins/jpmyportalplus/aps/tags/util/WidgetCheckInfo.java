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
