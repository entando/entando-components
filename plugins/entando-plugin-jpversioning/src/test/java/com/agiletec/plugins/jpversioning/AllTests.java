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
package com.agiletec.plugins.jpversioning;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TestTrashedResourceDAO;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TestTrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.TestVersioningDAO;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.TestVersioningManager;
import com.agiletec.plugins.jpversioning.apsadmin.resource.TestTrashedResourceAction;
import com.agiletec.plugins.jpversioning.apsadmin.versioning.TestVersionAction;
import com.agiletec.plugins.jpversioning.apsadmin.versioning.TestVersionFinderAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jpversioning");
		System.out.println("Test for jpversioning");
		
		suite.addTestSuite(TestVersioningDAO.class);
		suite.addTestSuite(TestVersioningManager.class);
		
		suite.addTestSuite(TestTrashedResourceDAO.class);
		suite.addTestSuite(TestTrashedResourceManager.class);
		
		suite.addTestSuite(TestTrashedResourceAction.class);
		
		suite.addTestSuite(TestVersionFinderAction.class);
		suite.addTestSuite(TestVersionAction.class);
		
		return suite;
	}
	
}