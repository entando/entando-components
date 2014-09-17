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

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Widget;

/**
 * Singola item della select di opzioni per lo spostamento widgets.
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
