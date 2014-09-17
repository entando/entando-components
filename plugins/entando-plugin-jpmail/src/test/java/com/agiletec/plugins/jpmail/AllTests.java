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
package com.agiletec.plugins.jpmail;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpmail.aps.system.services.mail.TestMailManager;
import com.agiletec.plugins.jpmail.aps.system.services.mail.parse.TestMailConfigDOM;
import com.agiletec.plugins.jpmail.aps.system.services.mail.util.TestEmailAddressValidator;
import com.agiletec.plugins.jpmail.apsadmin.mail.TestMailSenderConfigAction;
import com.agiletec.plugins.jpmail.apsadmin.mail.TestSmtpConfigAction;

/**
 * Launch class for all the jpmail tests.
 * @version 1.0
 */
public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Entando Plugin Mail");
		
		suite.addTestSuite(TestEmailAddressValidator.class);
		suite.addTestSuite(TestMailConfigDOM.class);
		suite.addTestSuite(TestMailManager.class);
		
		suite.addTestSuite(TestSmtpConfigAction.class);
		suite.addTestSuite(TestMailSenderConfigAction.class);
		
		return suite;
	}
	
}