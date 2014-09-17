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
package com.agiletec.plugins.jpcasclient;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.agiletec.plugins.jpcasclient.aps.system.services.auth.TestCasClientUtils;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.parse.TestConfigDOM;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jpcasclient");
		System.out.println("Test for jpcasclient");
		
//		suite.addTestSuite(TestCasClientUtils.class);
		
		suite.addTestSuite(TestConfigDOM.class);

		
		return suite;
	}
	
}