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
package com.agiletec.plugins.jpuserreg;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.activation.TestUserActivationAction;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.recover.TestUserRecoverAction;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.registration.TestUserRegistrationAction;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.suspension.TestUserSuspensionAction;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.TestUserRegDAO;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.TestUserRegManager;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS User Registration Plugin");
		
		suite.addTestSuite(TestUserRegDAO.class);
		suite.addTestSuite(TestUserRegManager.class);
		
		suite.addTestSuite(TestUserRegistrationAction.class);
		suite.addTestSuite(TestUserActivationAction.class);
		suite.addTestSuite(TestUserRecoverAction.class);
		suite.addTestSuite(TestUserSuspensionAction.class);
		
		return suite;
	}
	
}
