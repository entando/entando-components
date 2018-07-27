/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer;

import java.util.Properties;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;

/**
 * Classe action per la configurazione del filtro su attributo tipo "Text".
 * @author E.Santoboni
 */
public class TextAttributeFilterAction extends BaseFilterAction {
	
	@Override
	protected Properties createFilterProperties() {
		Properties properties =  super.createFilterProperties();
		int filterOption = this.getFilterOptionId();
		if (filterOption == VALUE_FILTER_OPTION) {
			this.setStringFilterParam(properties, EntitySearchFilter.VALUE_PARAM, this.getStringValue(), this.isLike());
		}
		if (filterOption == RANGE_FILTER_OPTION) {
			this.setStringFilterParam(properties, EntitySearchFilter.START_PARAM, this.getStringStart(), false);
			this.setStringFilterParam(properties, EntitySearchFilter.END_PARAM, this.getStringEnd(), false);
		}
		return properties;
	}
	
	protected void setStringFilterParam(Properties properties, String paramName, String string, boolean isLike) {
		if (null != string && string.trim().length()>0) {
			properties.put(paramName, string);
			if (isLike) {
				properties.put(EntitySearchFilter.LIKE_OPTION_PARAM, String.valueOf(isLike));
			}
		}
	}
	
	@Override
	public int getFilterTypeId() {
		return TEXT_ATTRIBUTE_FILTER_TYPE;
	}
	
	public String getStringValue() {
		return _stringValue;
	}
	public void setStringValue(String stringValue) {
		this._stringValue = stringValue;
	}
	
	public boolean isLike() {
		return _like;
	}
	public void setLike(boolean like) {
		this._like = like;
	}
	
	public String getStringStart() {
		return _stringStart;
	}
	public void setStringStart(String stringStart) {
		this._stringStart = stringStart;
	}
	
	public String getStringEnd() {
		return _stringEnd;
	}
	public void setStringEnd(String stringEnd) {
		this._stringEnd = stringEnd;
	}
	
	private String _stringValue;
	private boolean _like;
	private String _stringStart;
	private String _stringEnd;
	
}
