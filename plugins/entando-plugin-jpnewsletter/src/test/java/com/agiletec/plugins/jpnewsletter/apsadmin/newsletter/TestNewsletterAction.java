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
package com.agiletec.plugins.jpnewsletter.apsadmin.newsletter;

import javax.sql.DataSource;

import com.agiletec.plugins.jpnewsletter.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpnewsletter.util.JpnewsletterTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jpnewsletter.apsadmin.newsletter.NewsletterAction;

import com.opensymphony.xwork2.Action;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

public class TestNewsletterAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testEntry() throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpnewsletter/Newsletter", "entry");
		this.addParameter("contentId", "ART180");
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
		NewsletterAction action = (NewsletterAction) this.getAction();
		assertNull(action.getContentReport());
		assertNotNull(action.getContentVo());
		assertNotNull(action.getContent());
	}
	
    protected void init() throws Exception {
    	try {
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
    
	private JpnewsletterTestHelper _helper;
	
}