/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpseo;


import junit.framework.Test;
import junit.framework.TestSuite;

import org.entando.entando.plugins.jpseo.apsadmin.content.TestContentAction;

import com.agiletec.apsadmin.portal.TestPageAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Change me with a suitable description");
		suite.addTestSuite(TestContentAction.class);
		suite.addTestSuite(TestPageAction.class);
		return suite;
	}

}
