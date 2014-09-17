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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.parse;

import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpwtt.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.WttConfig;

public class TestWttConfigDOM extends ApsPluginBaseTestCase {

    protected void setUp() throws Exception {
    	super.setUp();
        this.init();
    }

    public void testReadConfig() throws Throwable {
    	String xml = this._configManager.getConfigItem("jpwttConfig");
    	WttConfig config = new WttConfigDOM().extractConfig(xml);

    	assertEquals(2, config.getInterventionTypes().size());
    	InterventionType hwType = config.getInterventionType(new Integer(1));
    	assertEquals("Hardware", hwType.getDescr());
    	assertEquals(new Integer(1), hwType.getId());
    	InterventionType swType = config.getInterventionType(new Integer(2));
    	assertEquals("Software", swType.getDescr());
    	assertEquals(new Integer(2), swType.getId());

    	Map<Integer, String> priorities = config.getPriorities();
    	assertEquals(3, priorities.size());
    	assertEquals("High", priorities.get(new Integer(1)));
    	assertEquals("Medium", priorities.get(new Integer(2)));
    	assertEquals("Low", priorities.get(new Integer(3)));
    }

    private void init() throws Exception {
    	try {
    		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		} catch (Exception e) {
			throw e;
		}
    }

	private ConfigInterface _configManager;

}