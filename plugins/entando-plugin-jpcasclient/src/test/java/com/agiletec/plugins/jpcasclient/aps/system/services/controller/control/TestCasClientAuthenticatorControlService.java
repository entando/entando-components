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
