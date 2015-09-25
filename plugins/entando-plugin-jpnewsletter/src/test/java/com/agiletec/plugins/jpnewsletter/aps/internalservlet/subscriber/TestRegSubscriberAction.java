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