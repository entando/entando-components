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
