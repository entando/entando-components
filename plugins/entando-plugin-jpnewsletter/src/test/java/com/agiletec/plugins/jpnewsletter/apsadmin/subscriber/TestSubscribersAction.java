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
package com.agiletec.plugins.jpnewsletter.apsadmin.subscriber;

import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.apsadmin.ApsAdminPluginBaseTestCase;
import com.opensymphony.xwork2.Action;

public class TestSubscribersAction extends ApsAdminPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testDeleteSubscriber() throws Throwable{
		String mail = "inesistente@inesistente.in";
		try {
			this.initAction("/do/jpnewsletter/Front/RegSubscriber", "addSubscription");
			this.addParameter("mailAddress", mail);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			assertNotNull(this._newsletterManager.loadSubscriber(mail));
			this.setUserOnSession("admin");
			
			this.initAction("/do/jpnewsletter/Subscriber", "delete");
			this.addParameter("mailAddress", mail);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			assertNull(this._newsletterManager.loadSubscriber(mail));
		} catch (Throwable t) {
			this._newsletterManager.deleteSubscriber(mail);
			throw t;
		}
	}
	
	private void init() throws Exception {
		try {
			this._newsletterManager = (INewsletterManager) this.getService(JpnewsletterSystemConstants.NEWSLETTER_MANAGER);
		} catch (Exception e) {
			throw e;
		}
	}
	
	private INewsletterManager _newsletterManager;
	
}