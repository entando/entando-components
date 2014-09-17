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
package com.agiletec.plugins.jpnewsletter.apsadmin.newsletter;

import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpnewsletter.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpnewsletter.util.JpnewsletterTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jacms.apsadmin.content.IContentFinderAction;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;

import com.opensymphony.xwork2.Action;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

public class TestNewsletterFinderAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testList() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpnewsletter/Newsletter", "list");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		IContentFinderAction action = (IContentFinderAction) this.getAction();
		List<String> contentIds = action.getContents();
		assertEquals(1, contentIds.size());
		assertTrue(contentIds.contains("ART180"));
	}
	
	public void testAddRemoveFromQueue() throws Throwable {
		try {
			String result = this.executeAddToQueue("admin", new String[] { "ART180" });
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { "ART180" });
			
			result = this.executeAddToQueue("admin", new String[] { "ART102" });
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { "ART180" });
			
			result = this.executeRemoveFromQueue("admin", new String[] { "ART102" });
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { "ART180" });
			
			result = this.executeAddToQueue("admin", new String[] { "ART180", "ART102" });
			assertEquals(Action.SUCCESS, result);
			this.checkQueue(this._newsletterManager.getContentQueue(), new String[] { "ART180" });
			
			result = this.executeRemoveFromQueue("admin", new String[] { "ART180" });
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
	
	private String executeAddToQueue(String currentUserName, String[] contentIds) throws Throwable {
		this.initAction("/do/jpnewsletter/Newsletter", "addToQueue");
		this.setUserOnSession(currentUserName);
		this.addParameter("contentIds", contentIds);
		return this.executeAction();
	}
	
	private String executeRemoveFromQueue(String currentUserName, String[] contentIds) throws Throwable {
		this.initAction("/do/jpnewsletter/Newsletter", "removeFromQueue");
		this.setUserOnSession(currentUserName);
		this.addParameter("contentIds", contentIds);
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