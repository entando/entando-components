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

import com.agiletec.plugins.jpcontentnotifier.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpcontentnotifier.aps.system.JpcontentnotifierSystemConstants;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.IContentNotifierManager;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.NotifierConfig;
import com.agiletec.plugins.jpcontentnotifier.apsadmin.contentnotifier.NotifierConfigAction;
import com.opensymphony.xwork2.Action;

public class TestNotifierConfigAction extends ApsAdminPluginBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testPerformConfig() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpcontentnotifier/NotifierConfig", "config");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		NotifierConfigAction action  = (NotifierConfigAction) this.getAction();
		assertTrue(action.isActive());
		assertTrue(action.isOnlyOwner());
		
		assertEquals(24, action.getHoursDelay());
		assertEquals(3, action.getDay());
		assertEquals(3, action.getMonth()); // Mesi da 0 a 11
		assertEquals(2009, action.getYear());
		assertEquals(18, action.getHour());
		assertEquals(25, action.getMinute());
		
		assertEquals("CODE1", action.getSenderCode());
		assertEquals("email", action.getMailAttrName());
		assertTrue(action.isHtml());
		assertEquals("Oggetto della mail di notifica", action.getSubject());
		assertEquals("Inizio Mail (testata)<br/>", action.getHeader());
		assertEquals("<br />Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br /> {link} <br />", action.getTemplateInsert());
		assertEquals("<br />Aggiornamento Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br /> {link} <br />", action.getTemplateUpdate());
		assertEquals("<br />Rimozione Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br />", action.getTemplateRemove());
		assertTrue(action.isNotifyRemove());
		assertEquals("<br />Fine Mail (footer)", action.getFooter());
	}
	
	public void testPerformSave() throws Throwable {
		String notifierConfig = this._configManager.getConfigItem(JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_CONFIG_ITEM);
		try {
			String result = this.executeSave("admin", false, 10l, false, 10, 3, 2009, 21, 45, "CODE2", "eMail", true, "subject", "header", 
					"templateInsert", "templateUpdate", "", true, "footer");
			assertEquals(Action.SUCCESS, result);
			
			NotifierConfig config = this._notifierManager.getConfig();
			
			assertEquals(false, config.isActive());
			assertEquals("10/04/2009 21:45", DateConverter.getFormattedDate(config.getStartScheduler(), "dd/MM/yyyy HH:mm"));
			assertEquals(10, config.getHoursDelay());
			assertEquals(false, config.isOnlyOwner());
			
			assertEquals("CODE2", config.getSenderCode());
			assertEquals("eMail", config.getMailAttrName());
			assertEquals(true, config.isHtml());
			assertEquals("subject", config.getSubject());
			assertEquals("header", config.getHeader());
			assertEquals("templateInsert", config.getTemplateInsert());
			assertEquals("templateUpdate", config.getTemplateUpdate());
			assertEquals("", config.getTemplateRemove());
			assertEquals(false, config.isNotifyRemove());
			assertEquals("footer", config.getFooter());
		} catch(Throwable t) {
			throw t;
		} finally {
			this._configManager.updateConfigItem(JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_CONFIG_ITEM, notifierConfig);
		}
	}
	
	public String executeSave(String username, boolean active, long hoursDelay, boolean onlyOwner, int day, 
			int month, int year, int hour, int minute, String senderCode, String mailAttrName, boolean html, 
			String subject, String header, String templateInsert, String templateUpdate, String templateRemove, 
			boolean notifyRemove, String footer) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpcontentnotifier/NotifierConfig", "save");
		this.addParameter("active", String.valueOf(active));
		this.addParameter("hoursDelay", String.valueOf(hoursDelay));
		this.addParameter("onlyOwner", String.valueOf(onlyOwner));
		this.addParameter("day", String.valueOf(day));
		this.addParameter("month", String.valueOf(month));
		this.addParameter("year", String.valueOf(year));
		this.addParameter("hour", String.valueOf(hour));
		this.addParameter("minute", String.valueOf(minute));
		this.addParameter("senderCode", senderCode);
		this.addParameter("mailAttrName", mailAttrName);
		this.addParameter("html", String.valueOf(html));
		this.addParameter("subject", subject);
		this.addParameter("header", header);
		this.addParameter("templateInsert", templateInsert);
		this.addParameter("templateUpdate", templateUpdate);
		this.addParameter("templateRemove", templateRemove);
		this.addParameter("notifyRemove", String.valueOf(notifyRemove));
		this.addParameter("footer", footer);
		String result = this.executeAction();
		return result;
	}
	
	private void init() throws Exception {
		try {
			this._notifierManager = (IContentNotifierManager) this.getService(JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_MANAGER);
			this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	private IContentNotifierManager _notifierManager = null;
	private ConfigInterface _configManager = null;
	
}