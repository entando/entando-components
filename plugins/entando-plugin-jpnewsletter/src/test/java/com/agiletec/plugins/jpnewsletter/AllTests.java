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
package com.agiletec.plugins.jpnewsletter;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpnewsletter.aps.internalservlet.subscriber.TestRegSubscriberAction;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.TestNewsletterDAO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.TestNewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.parse.TestNewsletterConfigDOM;
import com.agiletec.plugins.jpnewsletter.apsadmin.newsletter.TestNewsletterAction;
import com.agiletec.plugins.jpnewsletter.apsadmin.newsletter.TestNewsletterFinderAction;
import com.agiletec.plugins.jpnewsletter.apsadmin.newsletter.queue.TestNewsletterQueueAction;
import com.agiletec.plugins.jpnewsletter.apsadmin.subscriber.TestSubscribersAction;
import com.agiletec.plugins.jpnewsletter.apsadmin.subscriber.TestSubscribersFinderAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Entando Plugin Newsletter");
		
		suite.addTestSuite(TestNewsletterConfigDOM.class);
		suite.addTestSuite(TestNewsletterDAO.class);
		suite.addTestSuite(TestNewsletterManager.class);
		
		suite.addTestSuite(TestNewsletterFinderAction.class);
		suite.addTestSuite(TestNewsletterAction.class);
		suite.addTestSuite(TestNewsletterQueueAction.class);
		
		suite.addTestSuite(TestRegSubscriberAction.class);
		
		suite.addTestSuite(TestSubscribersAction.class);
		suite.addTestSuite(TestSubscribersFinderAction.class);
		
		return suite;
	}
	
}