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