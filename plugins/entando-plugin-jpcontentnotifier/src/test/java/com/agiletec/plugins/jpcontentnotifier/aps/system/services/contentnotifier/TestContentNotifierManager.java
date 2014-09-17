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
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpcontentnotifier.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcontentnotifier.util.TestContentNotifierHelper;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IAuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedEvent;
import com.agiletec.plugins.jacms.aps.system.services.content.event.PublicContentChangedObserver;
import com.agiletec.plugins.jpcontentnotifier.aps.system.JpcontentnotifierSystemConstants;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.ContentNotifierManager;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.IContentNotifierManager;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.ContentMailInfo;
import com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.model.NotifierConfig;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;

public class TestContentNotifierManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.activeMailManager(false);
	}
	
	public void testGetConfig() {
		NotifierConfig expectedConfig = new NotifierConfig();
		expectedConfig.setActive(true);
		expectedConfig.setHoursDelay(24);
		expectedConfig.setOnlyOwner(true);
		expectedConfig.setStartScheduler(DateConverter.parseDate("03/04/2009 18:25", "dd/MM/yyyy HH:mm"));
		
		expectedConfig.setSenderCode("CODE1");
		expectedConfig.setMailAttrName("email");
		expectedConfig.setHtml(true);
		expectedConfig.setSubject("Oggetto della mail di notifica");
		expectedConfig.setHeader("Inizio Mail (testata)<br/>");
		expectedConfig.setTemplateInsert("<br />Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br /> {link} <br />");
		expectedConfig.setTemplateUpdate("<br />Aggiornamento Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br /> {link} <br />");
		expectedConfig.setTemplateRemove("<br />Rimozione Contenuto tipo {type} - {descr} <br /> Data Operazione {date} {time} <br />");
		expectedConfig.setFooter("<br />Fine Mail (footer)");
		
		this.compareConfigs(expectedConfig,
				this._notifierManager.getConfig());
	}
	
	public void testUpdateNotifierConfig() throws Throwable {
		NotifierConfig oldConfig = this._notifierManager.getConfig();
		try {
			NotifierConfig newConfig = this.createConfig(true, 12, false, DateConverter.parseDate("120120101002", "ddMMyyHHmm"), 
					"CODE2", "eMail", false, "Oggetto", "header", "templateInsert", "templateUpdate", "templateRemove", "footer");
			this._notifierManager.updateNotifierConfig(newConfig);
			this.compareConfigs(newConfig, this._notifierManager.getConfig());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._notifierManager.updateNotifierConfig(oldConfig);
			this.compareConfigs(oldConfig, this._notifierManager.getConfig());
		}
	}
	
	public void testSendEMails() throws Throwable {
		this._helper.deleteContentChangingEventsRequest();
		try {
			this.addContentEvent("ART180", true, PublicContentChangedEvent.UPDATE_OPERATION_CODE);
			assertEquals(0, this._helper.getNotifiedContents().size());
			assertEquals(1, this._helper.getContentsToNotify().size());
			this._notifierManager.sendEMails();
			assertEquals(1, this._helper.getNotifiedContents().size());
			assertEquals(0, this._helper.getContentsToNotify().size());
			this._helper.deleteContentChangingEventsRequest();
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteContentChangingEventsRequest();
		}
	}
	
	public void testGetContentsToNotifyToUser() throws Throwable {
		this._helper.deleteContentChangingEventsRequest();
		try {
			this.addContentEvent("ART1", true, PublicContentChangedEvent.UPDATE_OPERATION_CODE); // mainGroup = free
			this.addContentEvent("ART111", true, PublicContentChangedEvent.UPDATE_OPERATION_CODE); // mainGroup = coach, extraGroups = customers, helpdesk
			this.addContentEvent("EVN25", true, PublicContentChangedEvent.UPDATE_OPERATION_CODE); // mainGroup = coach, extraGroups = free
			this.addContentEvent("RAH101", true, PublicContentChangedEvent.UPDATE_OPERATION_CODE); // mainGroup = customers
			this.addContentEvent("ART122", true, PublicContentChangedEvent.UPDATE_OPERATION_CODE); // mainGroup = administrators, extraGroups = customers
			List<ContentMailInfo> contentsToNotify = this._notifierManager.getContentsToNotify();
			assertEquals(5, contentsToNotify.size());
			
			UserDetails user = (UserDetails) this._authProvider.getUser("admin", "admin"); // administrators
			List<ContentMailInfo> contentsToNotifyToUser = ((ContentNotifierManager) this._notifierManager).getContentsToNotifyToUser(user, contentsToNotify);
			this.checkContentIds(new String[] { "ART1", "ART111", "EVN25", "RAH101", "ART122" }, contentsToNotifyToUser);
			
			user = (UserDetails) this._authProvider.getUser("supervisorCustomers", "supervisorCustomers"); // customers
			contentsToNotifyToUser = ((ContentNotifierManager) this._notifierManager).getContentsToNotifyToUser(user, contentsToNotify);
			this.checkContentIds(new String[] { "RAH101" }, contentsToNotifyToUser);
			
			user = (UserDetails) this._authProvider.getUser("editorCoach", "editorCoach"); // coach, customers
			contentsToNotifyToUser = ((ContentNotifierManager) this._notifierManager).getContentsToNotifyToUser(user, contentsToNotify);
			this.checkContentIds(new String[] { "ART111", "EVN25", "RAH101" }, contentsToNotifyToUser);
			
			this._notifierManager.getConfig().setOnlyOwner(false); // Also extraGroups
			
			user = (UserDetails) this._authProvider.getUser("supervisorCustomers", "supervisorCustomers"); // customers
			contentsToNotifyToUser = ((ContentNotifierManager) this._notifierManager).getContentsToNotifyToUser(user, contentsToNotify);
			this.checkContentIds(new String[] { "RAH101", "ART111", "ART122" }, contentsToNotifyToUser);
			
			user = (UserDetails) this._authProvider.getUser("editorCoach", "editorCoach"); // coach, customers
			contentsToNotifyToUser = ((ContentNotifierManager) this._notifierManager).getContentsToNotifyToUser(user, contentsToNotify);
			this.checkContentIds(new String[] { "ART111", "EVN25", "RAH101", "ART122" }, contentsToNotifyToUser);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteContentChangingEventsRequest();
		}
	}
	
	private void checkContentIds(String[] expectedIds, List<ContentMailInfo> receivedInfos) {
		assertEquals(expectedIds.length, receivedInfos.size());
		List<String> receivedIds = new ArrayList<String>(receivedInfos.size());
		for (ContentMailInfo info : receivedInfos) {
			receivedIds.add(info.getContentId());
		}
		for (String id : expectedIds) {
			if (!receivedIds.contains(id)) {
				fail("Not found id " + id);
			}
		}
	}
	
	private void addContentEvent(String contentId, boolean onLine,
			int operationCode) throws ApsSystemException {
		PublicContentChangedEvent event = new PublicContentChangedEvent();
		event.setContent(this._contentManager.loadContent(contentId, onLine));
		event.setOperationCode(operationCode);
		((PublicContentChangedObserver) this._notifierManager).updateFromPublicContentChanged(event);
	}
	
	private void compareConfigs(NotifierConfig conf1, NotifierConfig conf2) {
		assertEquals(conf1.isActive(), conf2.isActive());
		assertEquals(conf1.isOnlyOwner(), conf2.isOnlyOwner());
		assertEquals(conf1.getHoursDelay(), conf2.getHoursDelay());
		assertEquals(DateConverter.getFormattedDate(conf1.getStartScheduler(), "ddMMyyyyHHmm"), 
				DateConverter.getFormattedDate(conf2.getStartScheduler(), "ddMMyyyyHHmm"));

		assertEquals(conf1.getSenderCode(), conf2.getSenderCode());
		assertEquals(conf1.getMailAttrName(), conf2.getMailAttrName());
		assertEquals(conf1.isHtml(), conf2.isHtml());
		assertEquals(conf1.getSubject(), conf2.getSubject());
		assertEquals(conf1.getHeader(), conf2.getHeader());
		assertEquals(conf1.getTemplateInsert(), conf2.getTemplateInsert());
		assertEquals(conf1.getTemplateUpdate(), conf2.getTemplateUpdate());
		assertEquals(conf1.getTemplateRemove(), conf2.getTemplateRemove());
		assertEquals(conf1.isNotifyRemove(), conf2.isNotifyRemove());
		assertEquals(conf1.getFooter(), conf2.getFooter());
	}
	
	private NotifierConfig createConfig(boolean active, int hoursDelay, boolean onlyOwner, Date startScheduler, 
			String senderCode,String mailAttrName, boolean html, String subject, String header, 
			String templateInsert, String templateUpdate, String templateRemove, String footer) {
		NotifierConfig config = new NotifierConfig();
		config.setActive(active);
		config.setHoursDelay(hoursDelay);
		config.setOnlyOwner(onlyOwner);
		config.setStartScheduler(startScheduler);
		
		config.setSenderCode(senderCode);
		config.setMailAttrName(mailAttrName);
		config.setHtml(html);
		config.setSubject(subject);
		config.setHeader(header);
		config.setTemplateInsert(templateInsert);
		config.setTemplateUpdate(templateUpdate);
		if (templateRemove != null) {
			config.setTemplateRemove(templateRemove);
		}
		config.setFooter(footer);
		return config;
	}
	
	private void init() throws Exception {
		try {
			this._notifierManager = (IContentNotifierManager) this.getService(JpcontentnotifierSystemConstants.CONTENT_NOTIFIER_MANAGER);
			this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			this._helper = new TestContentNotifierHelper((DataSource) this.getApplicationContext().getBean("servDataSource"));
			this._authProvider = (IAuthenticationProviderManager) this.getService("AuthenticationProviderManager");
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.activeMailManager(true);
		super.tearDown();
	}
	
	private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
	
	private IContentNotifierManager _notifierManager;
	private IContentManager _contentManager;
	private TestContentNotifierHelper _helper;
	private IAuthenticationProviderManager _authProvider;
	
}