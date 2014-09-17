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
package com.agiletec.plugins.jpaddressbook;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.TestAddressBookDAO;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.TestAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.TestAddressBookSearcherDAO;
import com.agiletec.plugins.jpaddressbook.apsadmin.addressbook.TestContactAction;
import com.agiletec.plugins.jpaddressbook.apsadmin.addressbook.TestContactFinderAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jpaddressbook");
		System.out.println("Test for jpaddressbook");
		
		suite.addTestSuite(TestAddressBookDAO.class);
		suite.addTestSuite(TestAddressBookSearcherDAO.class);
		suite.addTestSuite(TestAddressBookManager.class);

		suite.addTestSuite(TestContactFinderAction.class);
		suite.addTestSuite(TestContactAction.class);
		
		return suite;
	}
	
}