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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg;

import java.util.Calendar;

import javax.sql.DataSource;

import com.agiletec.plugins.jpuserreg.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpuserreg.aps.JpUserRegSystemConstants;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegDAO;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.UserRegDAO;

/**
 * 
 * @author G.Cocco
 * */
public class TestUserRegDAO extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testAddReactivationToken_ClearOldAccountRequests_GetUsernameFromToken() throws ApsSystemException {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 01, 01);
		String token = "test_token";
		_userRegDAO.addActivationToken("test_username", token, calendar.getTime(), IUserRegDAO.REACTIVATION_RECOVER_TOKEN_TYPE);
		String username = _userRegDAO.getUsernameFromToken(token);
		assertNotNull(username);
		_userRegManager.clearOldAccountRequests();
		username = _userRegDAO.getUsernameFromToken(token);
		assertNull(username);
	}
	
	public void testAddActivationToken_ClearOldAccountRequests_GetUsernameFromToken() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 01, 01);
		String token = "test_token";
		String username = "test_username";
		User user = new User();
		user.setDisabled(true);
		user.setUsername(username);
		user.setPassword("password");
		_userManager.addUser(user);
		_userRegDAO.addActivationToken(username, token, calendar.getTime(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
		username = _userRegDAO.getUsernameFromToken(token);
		assertNotNull(username);
		_userRegManager.clearOldAccountRequests();
		username = _userRegDAO.getUsernameFromToken(token);
		assertNull(username);
	}
	
	public void testClearTokenByUsername() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 01, 01);
		String token = "test_token";
		String username = "test_username";
		_userRegDAO.addActivationToken(username, token, calendar.getTime(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
		String usernameCtrl = _userRegDAO.getUsernameFromToken(token);
		assertNotNull(usernameCtrl);
		_userRegDAO.clearTokenByUsername(username);
		usernameCtrl = _userRegDAO.getUsernameFromToken(token);
		assertNull(usernameCtrl);
	}
	
	public void testRemoveConsumedToken() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(2000, 01, 01);
		String token = "test_token";
		String username = "test_username";
		_userRegDAO.addActivationToken(username, token, calendar.getTime(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
		String usernameCtrl = _userRegDAO.getUsernameFromToken(token);
		assertNotNull(usernameCtrl);
		_userRegDAO.removeConsumedToken(token);
		usernameCtrl = _userRegDAO.getUsernameFromToken(token);
		assertNull(usernameCtrl);
	}
	
	private void init() {
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		UserRegDAO userRegDAO = new UserRegDAO();
		userRegDAO.setDataSource(dataSource);
		this._userRegDAO = userRegDAO;
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		this._userRegManager = (IUserRegManager) this.getService(JpUserRegSystemConstants.USER_REG_MANAGER);
	}
	
	private IUserRegDAO _userRegDAO;
	private IUserManager _userManager;
	private IUserRegManager _userRegManager;
	
}