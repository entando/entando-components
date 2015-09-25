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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.activation;

import java.util.Date;

import javax.sql.DataSource;

import com.agiletec.plugins.jpuserreg.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegDAO;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.UserRegDAO;

/**
 * @author G.Cocco
 * */
public class TestUserActivationAction extends ApsAdminPluginBaseTestCase {
	
	public void testInitActivation_ErrorWithoutToken() throws Throwable {
		this.initAction("/do/jpuserreg/UserReg", "initActivation");
		String result = this.executeAction();
		assertEquals("activationError", result);
	}
	
	public void testActivate() throws Throwable {
		String username = "username_test";
		String token = "token_test";
		try {
			//		prepare
			User user = new User();
			user.setDisabled(true);
			user.setUsername(username);
			user.setPassword("password");
			_userManager.addUser(user);
			_userRegDAO.addActivationToken(username, token, new Date(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
						
			this.initAction("/do/jpuserreg/UserReg", "activate");
			this.addParameter("token", token);
			this.addParameter("password", "012345678");
			this.addParameter("passwordConfirm", "012345678");
			String result = this.executeAction();
			assertEquals("success", result);
			user = (User) _userManager.getUser(username);
			assertNotNull(user);
			assertFalse(user.isDisabled());
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
	
	public void testActivateWithFakeToken() throws Throwable {
		String username = "username_test";
		String token = "token_test";
		String tokenFake = "12345678";
		try {
			//		prepare
			User user = new User();
			user.setDisabled(true);
			user.setUsername(username);
			user.setPassword("password");
			_userManager.addUser(user);
			_userRegDAO.addActivationToken(username, token, new Date(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
						
			this.initAction("/do/jpuserreg/UserReg", "initActivation");
			this.addParameter("token",  tokenFake);
			String result = this.executeAction();
			assertEquals("activationError", result);
			user = (User) _userManager.getUser(username);
			assertNotNull(user);
			assertTrue(user.isDisabled());
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
		
	public void testInitReactivation_ErrorWithoutOrFakeToken() throws Throwable {
		this.initAction("/do/jpuserreg/UserReg", "initReactivation");
		String result = this.executeAction();
		assertEquals("activationError", result);
		
		this.initAction("/do/jpuserreg/UserReg", "initReactivation");
		this.addParameter("token", "fakeToken");
		result = this.executeAction();
		assertEquals("activationError", result);
	}

	public void testReactivate() throws Throwable {
		String username = "username_test";
		String token = "token_test";
		try {
			//		prepare
			User user = new User();
			user.setDisabled(true);
			user.setUsername(username);
			user.setPassword("password");
			_userManager.addUser(user);
			_userRegDAO.addActivationToken(username, token, new Date(), IUserRegDAO.REACTIVATION_RECOVER_TOKEN_TYPE);
						
			this.initAction("/do/jpuserreg/UserReg", "reactivate");
			this.addParameter("token", token);
			this.addParameter("password", "password");
			this.addParameter("passwordConfirm", "wrongPasswordConfirm");
			String result = this.executeAction();
			assertEquals("input", result);
					
			this.initAction("/do/jpuserreg/UserReg", "reactivate");
			this.addParameter("token", token);
			this.addParameter("password", "new_password");
			this.addParameter("passwordConfirm", "new_password");
			result = this.executeAction();
			assertEquals("success", result);
			user = (User) _userManager.getUser(username,"new_password");
			assertNotNull(user);
			assertFalse(user.isDisabled());
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	private void init() throws Exception {
    	try {
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		UserRegDAO userRegDAO = new UserRegDAO();
    		userRegDAO.setDataSource(dataSource);
    		this._userRegDAO = userRegDAO;
		} catch (Exception e) {
			throw e;
		}
    }
	
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	public IUserManager getUserManager() {
		return _userManager;
	}
	
	private IUserManager _userManager;
	private IUserRegDAO _userRegDAO;
	
}