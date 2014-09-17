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
package com.agiletec.plugins.jpnewsletter.aps.internalservlet.subscriber;

import javax.sql.DataSource;

import com.agiletec.plugins.jpnewsletter.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpnewsletter.util.JpnewsletterTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

import com.opensymphony.xwork2.Action;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

public class TestRegSubscriberAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this.activeMailManager(false);
    }
	
	public void testAddSubscription() throws Throwable {
		String mail = "inesistente@inesistente.in";
		try {
			String result = this.executeAdd(mail);
			assertEquals(Action.SUCCESS, result);
			Subscriber subscriber = this._newsletterManager.loadSubscriber(mail);
			assertEquals(mail, subscriber.getMailAddress());
			assertFalse(subscriber.isActive());
			assertNotNull(this._helper.getToken(mail));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddresses();
		}
	}
	
	public void testActivateSubscription() throws Throwable{
		String mail = "inesistente@inesistente.in";
		try {
			String result = this.executeAdd(mail);
			assertEquals(Action.SUCCESS, result);
			
			String token = this._helper.getToken(mail);
			this.initAction("/do/jpnewsletter/Front/RegSubscriber", "subscription");
			this.addParameter("mailAddress", mail);
			this.addParameter("token", token);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddresses();
		}
	}
	
	public void testIsDuplicated() throws Throwable{
		String mail = "inesistente@inesistente.in";
		try {
			String result = this.executeAdd(mail);
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeAdd(mail);
			assertEquals(Action.SUCCESS, result);
			
			String token = this._helper.getToken(mail);
			this.initAction("/do/jpnewsletter/Front/RegSubscriber", "subscription");
			this.addParameter("mailAddress", mail);
			this.addParameter("token", token);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeAdd(mail);
			assertEquals(Action.INPUT, result);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.cleanAddresses();
		}
	}
	
	public String executeAdd(String mailAddress) throws Throwable {
		this.initAction("/do/jpnewsletter/Front/RegSubscriber", "addSubscription");
		this.addParameter("mailAddress", mailAddress);
		String result = this.executeAction();
		return result;
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
	
	private INewsletterManager _newsletterManager;
	private JpnewsletterTestHelper _helper;
	
}