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
package org.entando.entando.plugins.jprssaggregator.aps.system.services.aggregator;

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
