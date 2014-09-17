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
package com.agiletec.plugins.jpcontentnotifier.apsadmin.contentnotifier;

import java.util.Calendar;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.IContentNotifierManager;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.NotifierConfig;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.opensymphony.xwork2.Action;

public class NotifierConfigAction extends BaseAction implements INotifierConfigAction {
	
	@Override
	public String config() {
		try {
			this.initConfig();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "config");
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			NotifierConfig config = this.prepareConfig();
			this.getContentNotifierManager().updateNotifierConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	
	protected NotifierConfig prepareConfig() {
		NotifierConfig config = new NotifierConfig();
		config.setActive(this.isActive());
		config.setHoursDelay(this.getHoursDelay());
		config.setOnlyOwner(this.isOnlyOwner());
		
		Calendar start = Calendar.getInstance();
		start.set(Calendar.MONTH, this.getMonth());
		start.set(Calendar.YEAR, this.getYear());
		if (this.getDay() <= start.getActualMaximum(Calendar.DAY_OF_MONTH)) {
			start.set(Calendar.DAY_OF_MONTH, this.getDay());
		} else {
			start.set(Calendar.DAY_OF_MONTH, start.getActualMaximum(Calendar.DAY_OF_MONTH));
		}
		start.set(Calendar.HOUR_OF_DAY, this.getHour());
		start.set(Calendar.MINUTE, this.getMinute());
		config.setStartScheduler(start.getTime());
		
		config.setSenderCode(this.getSenderCode());
		config.setMailAttrName(this.getMailAttrName());
		config.setHtml(this.isHtml());
		config.setSubject(this.getSubject());
		config.setHeader(this.getHeader());
		config.setTemplateInsert(this.getTemplateInsert().trim());
		config.setTemplateUpdate(this.getTemplateUpdate().trim());
		String templateRemove = this.getTemplateRemove();
		config.setTemplateRemove(templateRemove);
		config.setFooter(this.getFooter().trim());
		
		return config;
	}
	
	protected void initConfig() {
		IContentNotifierManager notifierManager = this.getContentNotifierManager();
		
		NotifierConfig config = notifierManager.getConfig();
		
		this.setActive(config.isActive());
		this.setHoursDelay(config.getHoursDelay());
		this.setOnlyOwner(config.isOnlyOwner());
		
		Calendar start = Calendar.getInstance();
		start.setTime(config.getStartScheduler());
		this.setDay(start.get(Calendar.DAY_OF_MONTH));
		this.setMonth(start.get(Calendar.MONTH));
		this.setYear(start.get(Calendar.YEAR));
		this.setHour(start.get(Calendar.HOUR_OF_DAY));
		this.setMinute(start.get(Calendar.MINUTE));
		
		this.setSenderCode(config.getSenderCode());
		this.setMailAttrName(config.getMailAttrName());
		this.setHtml(config.isHtml());
		this.setSubject(config.getSubject());
		this.setHeader(config.getHeader());
		this.setTemplateInsert(config.getTemplateInsert());
		this.setTemplateUpdate(config.getTemplateUpdate());
		String templateRemove = config.getTemplateRemove();
		this.setTemplateRemove(templateRemove);
		this.setNotifyRemove(config.isNotifyRemove());
		this.setFooter(config.getFooter());
	}
	
	public Map<String, String> getSenderCodes() {
		try {
			return this.getMailManager().getMailConfig().getSenders();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSenderCodes");
			throw new RuntimeException("Error loading mail sender codes", t);
		}
	}
	
	public boolean isActive() {
		return _active;
	}
	public void setActive(boolean active) {
		this._active = active;
	}
	
	public long getHoursDelay() {
		return _hoursDelay;
	}
	public void setHoursDelay(long hoursDelay) {
		this._hoursDelay = hoursDelay;
	}
	
	public boolean isOnlyOwner() {
		return _onlyOwner;
	}
	public void setOnlyOwner(boolean onlyOwner) {
		this._onlyOwner = onlyOwner;
	}
	
	public int getDay() {
		return _day;
	}
	public void setDay(int day) {
		this._day = day;
	}
	
	public int getMonth() {
		return _month;
	}
	public void setMonth(int month) {
		this._month = month;
	}
	
	public int getYear() {
		return _year;
	}
	public void setYear(int year) {
		this._year = year;
	}
	
	public int getHour() {
		return _hour;
	}
	public void setHour(int hour) {
		this._hour = hour;
	}
	
	public int getMinute() {
		return _minute;
	}
	public void setMinute(int minute) {
		this._minute = minute;
	}
	
	public String getSenderCode() {
		return _senderCode;
	}
	public void setSenderCode(String senderCode) {
		this._senderCode = senderCode;
	}
	
	public String getMailAttrName() {
		return _mailAttrName;
	}
	public void setMailAttrName(String mailAttrName) {
		this._mailAttrName = mailAttrName;
	}
	
	public boolean isHtml() {
		return _html;
	}
	public void setHtml(boolean html) {
		this._html = html;
	}
	
	public String getSubject() {
		return _subject;
	}
	public void setSubject(String subject) {
		this._subject = subject;
	}
	
	public String getHeader() {
		return _header;
	}
	public void setHeader(String header) {
		this._header = header;
	}
	
	public String getTemplateInsert() {
		return _templateInsert;
	}
	public void setTemplateInsert(String templateInsert) {
		this._templateInsert = templateInsert;
	}
	
	public String getTemplateUpdate() {
		return _templateUpdate;
	}
	public void setTemplateUpdate(String templateUpdate) {
		this._templateUpdate = templateUpdate;
	}
	
	public boolean isNotifyRemove() {
		return _notifyRemove;
	}
	public void setNotifyRemove(boolean notifyRemove) {
		this._notifyRemove = notifyRemove;
	}
	
	public String getTemplateRemove() {
		return _templateRemove;
	}
	public void setTemplateRemove(String templateRemove) {
		this._templateRemove = templateRemove;
	}
	
	public String getFooter() {
		return _footer;
	}
	public void setFooter(String footer) {
		this._footer = footer;
	}
	
	public void setContentNotifierManager(IContentNotifierManager contentNotifierManager) {
		this._contentNotifierManager = contentNotifierManager;
	}
	public IContentNotifierManager getContentNotifierManager() {
		return _contentNotifierManager;
	}
	
	public IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	private boolean _active;
	
	private long _hoursDelay;
	private boolean _onlyOwner;
	
	private int _day;
	private int _month;
	private int _year;
	private int _hour;
	private int _minute;
	
	private String _senderCode;
	private String _mailAttrName;
	private boolean _html;
	private String _subject;
	private String _header;
	private String _templateInsert;
	private String _templateUpdate;
	private boolean _notifyRemove = false;
	private String _templateRemove;
	private String _footer;
	
	private IContentNotifierManager _contentNotifierManager;
	private IMailManager _mailManager;
	
}