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
package com.agiletec.plugins.jpcasclient.aps.system.services.config.parse;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcasclient.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.CasClientConfig;

public class TestConfigDOM extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_configDOM = new ConfigDOM();
	}

	public void test () throws ApsSystemException {
		CasClientConfig config = _configDOM.extractConfig(xmlConfig);
		assertNotNull(config);
		
		assertEquals(false, config.isActive());
		assertEquals("http://japs.intranet:8080/cas/login", config.getCasLoginURL());
		assertEquals("http://japs.intranet:8080/cas/logout", config.getCasLogoutURL());
		assertEquals("http://japs.intranet:8080/cas/validate", config.getCasValidateURL());
		assertEquals("notauth", config.getNotAuthPage());
		assertEquals("demo.entando.com", config.getRealm());
		assertEquals("http://japs.intranet:8080", config.getServerBaseURL());
		
		String configStr = _configDOM.createConfigXml(config);
		assertNotNull(configStr);
		
		assertTrue(configStr.contains(xmlConfig));
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		_configDOM = null;
	}
	
	private String xmlConfig = "<casclientConfig>" +
				"<active>false</active>" +
				"<casLoginURL>http://japs.intranet:8080/cas/login</casLoginURL>" +
				"<casLogoutURL>http://japs.intranet:8080/cas/logout</casLogoutURL>" +
				"<casValidateURL>http://japs.intranet:8080/cas/validate</casValidateURL>" +
				"<serverBaseURL>http://japs.intranet:8080</serverBaseURL>" +
				"<notAuthPage>notauth</notAuthPage>" +
				"<realm>demo.entando.com</realm>" +
			"</casclientConfig>";
	private ConfigDOM _configDOM;

}
