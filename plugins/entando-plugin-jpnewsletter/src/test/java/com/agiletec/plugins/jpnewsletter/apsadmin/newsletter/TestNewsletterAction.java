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