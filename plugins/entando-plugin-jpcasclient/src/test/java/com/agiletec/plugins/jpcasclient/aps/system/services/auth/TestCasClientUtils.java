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
package com.agiletec.plugins.jpcasclient.aps.system.services.auth;

import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.url.PageURL;

public class TestCasClientUtils extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		_urlManager = (IURLManager) this.getApplicationContext().getBean(SystemConstants.URL_MANAGER);
		_casClientUtils = new CasClientUtils();
	}
	
	public void test () {
		RequestContext reqCtx = this.getRequestContext();
		PageURL pageUrl = new PageURL(_urlManager, reqCtx);

		pageUrl.addParam("param1", "value1");
		pageUrl.addParam("param2", "value2");
		pageUrl.addParam("ticket", "ticket_value");
		pageUrl.addParam("param3", "value3");
		
		String url = pageUrl.getURL();
		assertTrue(url.contains("ticket"));
		
		String urlWithoutParam = _casClientUtils.getURLStringWithoutTicketParam(pageUrl, reqCtx);
		assertNotNull(urlWithoutParam);
		assertFalse(urlWithoutParam.contains("ticket"));
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	private CasClientUtils _casClientUtils;
	private IURLManager _urlManager;
}
