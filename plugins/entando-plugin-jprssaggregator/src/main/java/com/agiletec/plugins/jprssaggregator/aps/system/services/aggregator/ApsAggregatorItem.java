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
package com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator;

import java.util.Date;

import com.agiletec.aps.util.ApsProperties;

/**
 * Base Object of the aggregator service
 */
public class ApsAggregatorItem {


	public int getCode() {
		return _code;
	}
	public void setCode(int code) {
		this._code = code;
	}
	
	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}
	/**
	 * 
	 * @return the value in seconds
	 */
	public long getDelay() {
		return _delay;
	}
	/**
	 * Sets the delay in seconds.
	 * @param delay in seconds
	 */
	public void setDelay(long delay) {
		this._delay = delay;
	}
	
	public String getLink() {
		return _link;
	}
	public void setLink(String link) {
		this._link = link;
	}
	
	public void setLastUpdate(Date lastUpdate) {
		this._lastUpdate = lastUpdate;
	}
	public Date getLastUpdate() {
		return _lastUpdate;
	}

	public void setCategories(ApsProperties categories) {
		this._categories = categories;
	}
	public ApsProperties getCategories() {
		return _categories;
	}

	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	public String getContentType() {
		return _contentType;
	}

	private String _contentType;
	private int _code;
	private String _descr;
	private long _delay;
	private String _link;
	private Date _lastUpdate;
	private ApsProperties _categories;
	
	public static final String DATE_FORMAT = "MM/dd/yyyy";
}
