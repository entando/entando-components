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
package com.agiletec.plugins.jpstats.apsadmin;

import com.agiletec.ConfigTestUtils;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jpstats.PluginConfigTestUtils;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsManager;
import com.agiletec.plugins.jpstats.util.TestStatsUtils;

public class ApsAdminPluginBaseTestCase extends ApsAdminBaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
        TestStatsUtils.cleanTestEnvironment(this._statsManager);
	}
	
	protected void init() throws Exception {
    	try {
    		this._statsManager = (StatsManager) this.getService(JpStatsSystemConstants.STATS_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
	}
	
	protected StatsManager _statsManager = null;
}
