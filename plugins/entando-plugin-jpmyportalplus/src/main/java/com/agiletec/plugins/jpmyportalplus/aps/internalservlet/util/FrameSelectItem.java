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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet.util;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;

/**
 * @author E.Santoboni
 */
public class FrameSelectItem {

	public FrameSelectItem(Integer columnId, Integer columnIdDest,
			Widget showlet, Integer frameId, Lang lang) {
		super();
		this._currentColumnId = columnId;
		this._columnIdDest = columnIdDest;
		this._showlet = showlet;
		this._frameId = frameId;
		this._lang = lang;
	}

	public Boolean getSameColumn() {
		return (null != this.getCurrentColumnId()
				&& null != this.getColumnIdDest()
				&& this.getCurrentColumnId().equals(this.getColumnIdDest()));
	}

	public Integer getCurrentColumnId() {
		return _currentColumnId;
	}
	public Integer getColumnIdDest() {
		return _columnIdDest;
	}
	public Widget getShowlet() {
		return _showlet;
	}
	public Integer getFrameId() {
		return _frameId;
	}

	public String getCode() {
		if (this._showlet == null) return null;
		return this._showlet.getType().getCode();
	}

	public String getTitle() {
		if (this._showlet == null) return null;
		String title = this._showlet.getType().getTitles().getProperty(this._lang.getCode());
		if (null != title) {
			return title;
		}
		return this.getCode();
	}

	private Integer _currentColumnId;
	private Widget _showlet;
	private Integer _frameId;
	private Lang _lang;

	private Integer _columnIdDest;

}
