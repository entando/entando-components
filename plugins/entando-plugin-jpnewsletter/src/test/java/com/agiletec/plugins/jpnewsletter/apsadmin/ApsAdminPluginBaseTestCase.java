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
package com.agiletec.plugins.jpnewsletter.apsadmin;

import com.agiletec.ConfigTestUtils;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpnewsletter.PluginConfigTestUtils;
import com.agiletec.plugins.jpnewsletter.aps.system.JpnewsletterSystemConstants;

/**
 * BaseTestCase Class for jpnewsletter apsadmin tests.
 */
public class ApsAdminPluginBaseTestCase extends ApsAdminBaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.activeMailManager(false);
    }
	
	@Override
	protected void tearDown() throws Exception {
		this.waitForSentNewsletter();
		this.activeMailManager(true);
		super.tearDown();
	}
	
	protected void waitForSentNewsletter() throws Exception {
		Thread[] threads = new Thread[40];
	    Thread.enumerate(threads);
	    for (int i=0; i<threads.length; i++) {
	    	Thread currentThread = threads[i];
	    	if (currentThread != null && 
	    			(currentThread.getName().startsWith(JpnewsletterSystemConstants.NEWSLETTER_SENDER_THREAD_NAME)
	    					|| currentThread.getName().startsWith(JpnewsletterSystemConstants.EMAIL_SENDER_NAME_THREAD_PREFIX))
	    			) {
	    		currentThread.join();
	    	}
	    }
	}
	
    private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
	
}
