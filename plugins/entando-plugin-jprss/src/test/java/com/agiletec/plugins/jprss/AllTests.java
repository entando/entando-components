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
package com.agiletec.plugins.jprss;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jprss.aps.system.services.rss.TestRssManager;
import com.agiletec.plugins.jprss.apsadmin.rss.TestRssAction;
import com.agiletec.plugins.jprss.apsadmin.rss.portal.TestRssPortalAction;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS RSS Plugin");
		suite.addTestSuite(TestRssManager.class);
		suite.addTestSuite(TestRssAction.class);
		suite.addTestSuite(TestRssPortalAction.class);
		return suite;
	}
	
}
