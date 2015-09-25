/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
