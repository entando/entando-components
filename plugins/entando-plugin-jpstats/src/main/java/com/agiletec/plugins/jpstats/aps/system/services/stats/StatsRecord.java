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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * A single stats record
 * @author E.Santoboni
 */
public class StatsRecord {
	
	public StatsRecord(Calendar recordDate) {
		this.setRecordDate(recordDate);
	}
	
	public String getTimestamp() {
		String timestamp = "";
		if (null != this.getRecordDate()) {
			Timestamp ts = new Timestamp(this.getRecordDate().getTimeInMillis());
			timestamp = ts.toString();
		}
		return timestamp;
	}
	
	public String getYear() {
		return String.valueOf(this.getRecordDate().get(Calendar.YEAR));
	}
	
	public String getMonth() {
		String month = String.valueOf(this.getRecordDate().get(Calendar.MONTH)+1);
		if (month.length()==1) {
			month = "0"+month;
		}
		return month;
	}
	
	public String getDay() {
		String day = String.valueOf(this.getRecordDate().get(Calendar.DAY_OF_MONTH));
		if (day.length()==1) {
			day = "0"+day;
		}
		return day;
	}
	
	public String getHour() {
		String hourOfDay = String.valueOf(this.getRecordDate().get(Calendar.HOUR_OF_DAY));
		if (hourOfDay.length()==1) hourOfDay = "0"+hourOfDay;
		String minute = String.valueOf(this.getRecordDate().get(GregorianCalendar.MINUTE));
		if (minute.length()==1) minute = "0"+minute;
		String second = String.valueOf(this.getRecordDate().get(GregorianCalendar.SECOND));
		if (second.length()==1) second = "0"+second;
		String hour = hourOfDay + ":" + minute + ":" + second;
		return hour;
	}
	
	public String getIp() {
		return _ip;
	}
	public void setIp(String ip) {
		this._ip = ip;
	}
	
	public String getReferer() {
		return _referer;
	}
	public void setReferer(String referer) {
		this._referer = referer;
	}
	
	public String getSessionId() {
		return _sessionId;
	}
	public void setSessionId(String sessionId) {
		this._sessionId = sessionId;
	}
	
	public String getRole() {
		return _role;
	}
	public void setRole(String role) {
		this._role = role;
	}
	
	public Calendar getRecordDate() {
		return _recordDate;
	}
	private void setRecordDate(Calendar recordDate) {
		this._recordDate = recordDate;
	}
	
	public String getPageCode() {
		return _pageCode;
	}
	public void setPageCode(String pageCode) {
		this._pageCode = pageCode;
	}
	
	public String getLangcode() {
		return _langcode;
	}
	public void setLangcode(String langcode) {
		this._langcode = langcode;
	}
	
	public String getUseragent() {
		return _useragent;
	}
	public void setUseragent(String useragent) {
		this._useragent = useragent;
	}
	
	public String getBrowserLang() {
		return _browserLang;
	}
	public void setBrowserLang(String browserLang) {
		this._browserLang = browserLang;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	private String _ip;
	private String _referer;
	private String _sessionId;
	private String _role;
	
	private Calendar _recordDate;
	
	private String _pageCode;
	private String _langcode;
	private String _useragent;
	private String _browserLang;
	private String _contentId;
	
}