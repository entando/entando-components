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
