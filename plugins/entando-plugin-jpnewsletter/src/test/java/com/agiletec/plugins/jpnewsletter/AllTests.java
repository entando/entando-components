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
		TestSuite suite = new TestSuite("Test for jAPS Plugin Newsletter");
		
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