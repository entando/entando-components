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
package com.agiletec.plugins.jprssaggregator;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.TestAggregatorManager;
import com.agiletec.plugins.jprssaggregator.aps.system.services.converter.TestRssConverterManager;
import com.agiletec.plugins.jprssaggregator.apsadmin.aggregator.TestAggregatorAction;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS RssAggregator Plugin");
		suite.addTestSuite(TestAggregatorManager.class);
		suite.addTestSuite(TestRssConverterManager.class);
		suite.addTestSuite(TestAggregatorAction.class);
		return suite;
	}
}
