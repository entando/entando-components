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
package com.agiletec.plugins.jpnewsletter.apsadmin.newsletter.queue;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpnewsletter.apsadmin.newsletter.INewsletterQueueAction;
import com.agiletec.plugins.jpnewsletter.util.JpnewsletterTestHelper;

import com.opensymphony.xwork2.Action;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

public class TestNewsletterQueueAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testList() throws Throwable {
		try {
			String result = this.executeList("admin");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(((INewsletterQueueAction) this.getAction()).getContentIds(), new String[] { });
			this._newsletterManager.addContentToQueue("ART180");
			result = this.executeList("admin");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(((INewsletterQueueAction) this.getAction()).getContentIds(), new String[] { "ART180" });
			this._newsletterManager.addContentToQueue("ART102");
			result = this.executeList("admin");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(((INewsletterQueueAction) this.getAction()).getContentIds(), new String[] { "ART180", "ART102" });
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
		}
	}
	
	public void testSend() throws Throwable {
		try {
			NewsletterConfig config = this._newsletterManager.getNewsletterConfig();
			config.setActive(true);
			config.setHoursDelay(1);
			config.setStartScheduler(new Date(new Date().getTime()+2));
			_newsletterManager.updateNewsletterConfig(config);
			
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { });
			String result = this.executeSend("admin");
			assertEquals(Action.SUCCESS, result);
			
			this._newsletterManager.addContentToQueue("ART180");
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { "ART180" });
			result = this.executeSend("admin");
			assertEquals(Action.SUCCESS, result);
			
			this._helper.joinWithSenderThread(JpnewsletterSystemConstants.NEWSLETTER_SENDER_THREAD_NAME);
			this._helper.joinWithSenderThread(JpnewsletterSystemConstants.EMAIL_SENDER_NAME_THREAD_PREFIX);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { });
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
			this._helper.resetConfig();
		}
	}
	
	public void testRemoveFromQueue() throws Throwable {
		try {
			String result = this.executeRemoveFromQueue("admin", "ART102");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { });
			
			this._newsletterManager.addContentToQueue("ART180");
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { "ART180" });
			
			result = this.executeRemoveFromQueue("admin", "ART102");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { "ART180" });
			
			result = this.executeRemoveFromQueue("admin", "ART180");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { });
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteNewsletters();
		}
	}
	
	private void checkQueue(List<String> contentQueue, String[] expectedQueue) {
		assertEquals(expectedQueue.length, contentQueue.size());
		for (String id : expectedQueue) {
			assertTrue(contentQueue.contains(id));
		}
	}
	
	private String executeList(String currentUserName) throws Throwable {
		this.initAction("/do/jpnewsletter/Newsletter/Queue", "list");
		this.setUserOnSession(currentUserName);
		return this.executeAction();
	}
	
	private String executeSend(String currentUserName) throws Throwable {
		this.initAction("/do/jpnewsletter/Newsletter/Queue", "send");
		this.setUserOnSession(currentUserName);
		return this.executeAction();
	}
	
	private String executeRemoveFromQueue(String currentUserName, String contentId) throws Throwable {
		this.initAction("/do/jpnewsletter/Newsletter/Queue", "removeFromQueue");
		this.setUserOnSession(currentUserName);
		this.addParameter("contentId", contentId);
		return this.executeAction();
	}
	
    protected void init() throws Exception {
    	try {
    		this._newsletterManager = (INewsletterManager) this.getService(JpnewsletterSystemConstants.NEWSLETTER_MANAGER);
    		IUserManager userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		IUserProfileManager profileManager = (IUserProfileManager) this.getService(SystemConstants.USER_PROFILE_MANAGER);
    		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
    		this._helper = new JpnewsletterTestHelper(userManager, profileManager, configManager);
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		this._helper.setDataSource(dataSource);
		} catch (Exception e) {
			throw e;
		}
    }
    
	private INewsletterManager _newsletterManager;
	private JpnewsletterTestHelper _helper;
	
}