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
package com.agiletec.plugins.jpcasclient.aps.system.services.controller.control;

import java.util.HashMap;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.authentication.AttributePrincipalImpl;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;
import org.springframework.mock.web.MockHttpServletRequest;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.ApsPluginBaseTestCase;

public class TestCasClientAuthenticatorControlService extends ApsPluginBaseTestCase {

	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }


	public void test_no_auth() {
		RequestContext reqCtx = this.getRequestContext();
		int status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		UserDetails currentUser = (UserDetails) reqCtx.getRequest().getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals(SystemConstants.GUEST_USER_NAME, currentUser.getUsername());
	}

	public void test_no_cas_auth() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setParameter("username", "admin");
		request.setParameter("password", "admin");
		int status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals("admin", currentUser.getUsername());
	}

	public void test_no_cas_auth_authFailure() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();
		request.setParameter("user", "notauthorized");
		request.setParameter("password", "notauthorized");
		int status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals(SystemConstants.GUEST_USER_NAME, currentUser.getUsername());
	}


	public void test_cas_auth_authFailure() throws ApsSystemException {
		RequestContext reqCtx = this.getRequestContext();
		MockHttpServletRequest request = (MockHttpServletRequest) reqCtx.getRequest();

		AttributePrincipal attributePrincipal = new AttributePrincipalImpl("admin");
		Assertion assertion = new AssertionImpl(attributePrincipal, new HashMap());
		request.setAttribute(CasClientPluginSystemCostants.JPCASCLIENT_CONST_CAS_ASSERTION, assertion);

		int status = _authenticator.service(reqCtx, ControllerManager.CONTINUE);
		assertEquals(status, ControllerManager.CONTINUE);
		UserDetails currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		assertEquals(SystemConstants.GUEST_USER_NAME, currentUser.getUsername());
	}

	private void init() throws Exception {
        try {
        	this._authenticator = (CasClientAuthenticatorControlService) this.getApplicationContext().getBean("AuthenticatorControlService");
        } catch (Throwable e) {
            throw new Exception(e);
        }
    }

	private CasClientAuthenticatorControlService _authenticator;





}
