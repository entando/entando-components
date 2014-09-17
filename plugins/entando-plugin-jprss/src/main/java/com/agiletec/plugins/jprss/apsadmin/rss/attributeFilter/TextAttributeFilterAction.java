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
package com.agiletec.plugins.jprss.apsadmin.rss.attributeFilter;

public class TextAttributeFilterAction extends com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.TextAttributeFilterAction {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public String getTitle() {
		return _title;
	}
	public void setTitle(String title) {
		this._title = title;
	}
	
	public String getDescription() {
		return _description;
	}
	public void setDescription(String description) {
		this._description = description;
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public String getFeedType() {
		return _feedType;
	}
	public void setFeedType(String feedType) {
		this._feedType = feedType;
	}
	
	public int getFilterIndex() {
		return _filterIndex;
	}
	public void setFilterIndex(int filterIndex) {
		this._filterIndex = filterIndex;
	}

	private int _id;
	private String _title;
	private String _description;
	private boolean _active;
	private String _feedType;
	private int _filterIndex;
}
