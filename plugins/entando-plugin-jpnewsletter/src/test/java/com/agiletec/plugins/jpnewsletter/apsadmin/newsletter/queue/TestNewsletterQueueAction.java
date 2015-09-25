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
import com.agiletec.plugins.jpnewsletter.apsadmin.newsletter.NewsletterQueueAction;
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
			this.checkQueue(((NewsletterQueueAction) this.getAction()).getContentIds(), new String[] { });
			this._newsletterManager.addContentToQueue("ART180");
			result = this.executeList("admin");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(((NewsletterQueueAction) this.getAction()).getContentIds(), new String[] { "ART180" });
			this._newsletterManager.addContentToQueue("ART102");
			result = this.executeList("admin");
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(((NewsletterQueueAction) this.getAction()).getContentIds(), new String[] { "ART180", "ART102" });
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