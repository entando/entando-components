/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.agiletec.plugins.jpwtt.aps.externalframework.ticket.TestUserTicketAction;
import com.agiletec.plugins.jpwtt.aps.externalframework.ticket.TestUserTicketFinderAction;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.TestTicketDAO;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.TestTicketManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter.mail.parse.TestWttMailConfigDOM;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.parse.TestWttConfigDOM;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.TestTicketAction;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.TestTicketFinderAction;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS Plugin jpwtt");

		suite.addTestSuite(TestWttConfigDOM.class);
		suite.addTestSuite(TestWttMailConfigDOM.class);
		suite.addTestSuite(TestTicketDAO.class);
		suite.addTestSuite(TestTicketManager.class);

		suite.addTestSuite(TestUserTicketFinderAction.class);
		suite.addTestSuite(TestUserTicketAction.class);

		suite.addTestSuite(TestTicketFinderAction.class);
		suite.addTestSuite(TestTicketAction.class);

		return suite;
	}

}
