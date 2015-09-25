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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.suspension;

import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpuserreg.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.opensymphony.xwork2.Action;

/**
 * @author G.Cocco
 * */
public class TestUserSuspensionAction extends ApsAdminPluginBaseTestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testInitSuspend_RedirectWithoutUserOnSession() throws Throwable {
		this.initAction("/do/jpuserreg/UserReg", "initSuspension");
		String result = this.executeAction();
		assertNull(result);
	}
	
	public void testInitSuspend_RedirectToErrorPageForAdministrators() throws Throwable {
		this.initAction("/do/jpuserreg/UserReg", "initSuspension");
		this.setUserOnSession("admin");
		String result = this.executeAction();
		assertNotNull(result);
		assertEquals("userreg_error", result);
	}
	
	public void testSuspend_NoRequiredPassword() throws Throwable {
		String username = "username_test";
		try {
			User user = new User();
			user.setDisabled(false);
			user.setUsername(username);
			user.setPassword(username);
			_userManager.addUser(user);
			this.setUserOnSession(username);
			
			this.initAction("/do/jpuserreg/UserReg", "suspend");
			String result = this.executeAction();
			assertNotNull(result);
			assertEquals("input", result);
			
			Map fieldsErrors = this.getAction().getFieldErrors();
			assertNotNull(fieldsErrors);
			assertEquals(fieldsErrors.size(), 1);
			
			List<String> errors = (List<String>) fieldsErrors.get("password");
			assertNotNull(errors);
			assertEquals(errors.get(0), this.getAction().getText("jpuserreg.suspension.password.required"));
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
	
	public void testSuspend_WrongPassword() throws Throwable {
		String username = "username_test";
		try {
			User user = new User();
			user.setDisabled(false);
			user.setUsername(username);
			user.setPassword(username);
			_userManager.addUser(user);
			this.setUserOnSession(username);
			
			this.initAction("/do/jpuserreg/UserReg", "suspend");
			this.addParameter("password", "wrongpassword");
			String result = this.executeAction();
			assertNotNull(result);
			assertEquals("input", result);
			
			Map fieldsErrors = this.getAction().getFieldErrors();
			assertNotNull(fieldsErrors);
			assertEquals(fieldsErrors.size(), 1);
			
			List<String> errors = (List<String>) fieldsErrors.get("password");
			assertNotNull(errors);
			assertEquals(errors.get(0), this.getAction().getText("jpuserreg.suspension.password.wrong"));
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
	
	public void testSuspend() throws Throwable {
		String username = "username_test";
		try {
			this.setUserOnSession("editorCoach");
			User user = new User();
			user.setDisabled(false);
			user.setUsername(username);
			user.setPassword(username);
			_userManager.addUser(user);
			this.setUserOnSession(username);
			this.initAction("/do/jpuserreg/UserReg", "suspend");
			this.addParameter("password", username);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			user = (User) _userManager.getUser(username);
			assertNotNull(user);
			assertTrue(user.isDisabled());
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
	
	private void init() {
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
	}
	
	private IUserManager _userManager;
	
}