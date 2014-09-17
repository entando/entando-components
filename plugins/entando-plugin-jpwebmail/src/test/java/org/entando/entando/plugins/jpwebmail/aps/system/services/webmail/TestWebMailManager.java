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
package org.entando.entando.plugins.jpwebmail.aps.system.services.webmail;

import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;
import org.entando.entando.plugins.jpwebmail.aps.ApsPluginBaseTestCase;

public class TestWebMailManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testGetConfig() throws Throwable {
		WebMailConfig config = this._webMailManager.loadConfig();
		assertEquals("out.virgilio.it", config.getSmtpHost());
		assertEquals("", config.getSmtpUserName());
		assertEquals("", config.getSmtpPassword());
	}
	
    private void init() throws Exception {
    	try {
    		_webMailManager = (IWebMailManager) super.getService(JpwebmailSystemConstants.WEBMAIL_MANAGER);
		} catch (Exception e) {
			throw e;
		}
    }
	
	private IWebMailManager _webMailManager = null;
	
}