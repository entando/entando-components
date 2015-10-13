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
package org.entando.entando.plugins.jprss.apsadmin.config;

import java.util.List;
import java.util.Properties;

import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.BaseFilterAction;

/**
 * This class manages allows to build the content filters for the channel configuration.
 * Extends BaseFilterAction for filters configuration
 * @author S.Puddu
 */
public class RssFilterAction extends BaseFilterAction {
	
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

	public void setFilterIndex(int filterIndex) {
		this._filterIndex = filterIndex;
	}
	public int getFilterIndex() {
		return _filterIndex;
	}

	public void setMovement(String movement) {
		this._movement = movement;
	}
	public String getMovement() {
		return _movement;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setFiltersProperties(List<Properties> filtersProperties) {
		this._filtersProperties = filtersProperties;
	}
	public List<Properties> getFiltersProperties() {
		return _filtersProperties;
	}
	
	public int getMaxContentsSize() {
		return _maxContentsSize;
	}
	public void setMaxContentsSize(int maxContentsSize) {
		this._maxContentsSize = maxContentsSize;
	}
	
	private int _id;
	private String _title;
	private String _description;
	private boolean _active;
	private String _feedType;
	private int _filterIndex;
	private String _movement;
	private int _strutsAction;
	private List<Properties> _filtersProperties;
	private int _maxContentsSize;
	
}
