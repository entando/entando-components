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
package com.agiletec.plugins.jpstats;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.agiletec.plugins.jpstats.aps.system.services.controller.control.TestStatsMonitor;
import com.agiletec.plugins.jpstats.aps.system.services.stats.TestStatsDao;
import com.agiletec.plugins.jpstats.aps.system.services.stats.TestStatsManager;
import com.agiletec.plugins.jpstats.aps.util.TestCalendarConverter;
import com.agiletec.plugins.jpstats.apsadmin.TestStatisticsAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS Stats Plugin");
		suite.addTestSuite(TestStatsDao.class);
		suite.addTestSuite(TestStatsManager.class);
		suite.addTestSuite(TestStatsMonitor.class);
		suite.addTestSuite(TestStatisticsAction.class);
		suite.addTestSuite(TestCalendarConverter.class);
		return suite;
	}
	
}