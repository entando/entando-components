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