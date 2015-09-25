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
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpuserreg.JpUserRegTestHelper;
import com.agiletec.plugins.jpuserreg.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.Authorization;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpuserreg.aps.JpUserRegSystemConstants;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author G.Cocco
 * */
public class TestUserRegManager extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.activeMailManager(false);
	}
	
	//	to verify config a valid email addess in email field
	public void testRegAccount() throws Throwable {
		String username = "username_test";
		try {
			insertTestUserProfile(username);
			User user = (User) _userManager.getUser(username);
			assertNotNull(user);
			assertNotNull(user.getProfile());
			
			String token = this._testHelper.getTokenFromUsername(username);
			assertNotNull(token);
			
			this._userManager.removeUser(username);
			token = this._testHelper.getTokenFromUsername(username);
			assertNull(token);
		} catch(Throwable t) {
			//	clean
			_userManager.removeUser(username);
			_userRegDAO.clearTokenByUsername(username);
			throw t;
		}
	}
	
	public void testActivateUser() throws Exception {
		String username = "username_test";
		String token = "token_test";
		try {
			User user = new User();
			user.setDisabled(true);
			user.setUsername(username);
			user.setPassword("password");
			_userManager.addUser(user);
			_userRegDAO.addActivationToken(username, token, new Date(), IUserRegDAO.ACTIVATION_TOKEN_TYPE);
			_userRegManager.activateUser(username, "password", token);
			user = (User) _userManager.getUser(username);
			assertNotNull(user);
			List<Authorization> authorizations = this._authorizationManager.getUserAuthorizations(username);
			assertNotNull(authorizations);
			assertEquals(2, authorizations.size());
			assertFalse(user.isDisabled());
		} finally {
			_userManager.removeUser(username);
		}
	}
	
	public void testReactivateUser() throws Exception {
		String username = "username_test";
		String token = "token_test";
		try {
			User user = new User();
			user.setDisabled(true);
			user.setUsername(username);
			user.setPassword("password");
			_userManager.addUser(user);
			_userRegDAO.addActivationToken(username, token, new Date(), IUserRegDAO.REACTIVATION_RECOVER_TOKEN_TYPE);
			_userRegManager.reactivateUser(username, "password", token);
			user = (User) _userManager.getUser(username);
			assertNotNull(user);
			assertNotNull(user.getAuthorizations());
			assertEquals(0, user.getAuthorizations().size());
			assertFalse(user.isDisabled());
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
	
	public void testDeactivateUser() throws ApsSystemException {
		String username = "username_test";
		try {
			User user = new User();
			user.setDisabled(false);
			user.setUsername(username);
			user.setPassword("password");
			_userManager.addUser(user);
			user = (User) _userManager.getUser(username);
			assertNotNull(user);
			assertFalse(user.isDisabled());
			_userRegManager.deactivateUser(user);
			user = (User) _userManager.getUser(username);
			assertNotNull(user);
			assertTrue(user.isDisabled());
		} finally {
			//		clean
			_userManager.removeUser(username);
		}
	}
	
	public void testGetUsernameFromToken() throws Exception {
		String username = "username_test";
		String token = "token_test";
		try {
			User user = new User();
			user.setDisabled(true);
			user.setUsername(username);
			user.setPassword("password");
			_userManager.addUser(user);
			_userRegDAO.addActivationToken(username, token, new Date(), IUserRegDAO.REACTIVATION_RECOVER_TOKEN_TYPE);
			String checkUsername = _userRegManager.getUsernameFromToken(token);
			assertNotNull(checkUsername);
			assertEquals(user.getUsername(), checkUsername);
		} finally {
			//		clean
			_userManager.removeUser(username);
			_userRegDAO.clearTokenByUsername(username);
		}
	}
	
//	to verify config a valid email addess in email field
	public void testReactivationByEmail() throws Exception {
		String username = "username_test";
		try {
			insertTestUserProfile(username);
			_userRegManager.reactivationByEmail(JpUserRegTestHelper.EMAIL);
		} finally {
			//	clean
			_userManager.removeUser(username);
			_userRegDAO.clearTokenByUsername(username);
		}
	}
	
//	to verify config a valid email addess in email field
	public void testReactivationByUserName() throws Exception {
		String username = "username_test";
		try {
			insertTestUserProfile(username);
			_userRegManager.reactivationByUserName(username);
		} finally {
			_userManager.removeUser(username);
			_userRegDAO.clearTokenByUsername(username);
		}
	}
	
	private void insertTestUserProfile(String username) throws Exception {
		IUserProfile profile = _userProfileManager.getDefaultProfileType();
		profile.setId(username);
		MonoTextAttribute fullnameAttr = (MonoTextAttribute) profile.getAttribute("fullname");
		fullnameAttr.setText("nametest surnametest");
		MonoTextAttribute emailAttr = (MonoTextAttribute) profile.getAttribute("email");
		emailAttr.setText(JpUserRegTestHelper.EMAIL);
		DateAttribute dateAttr = (DateAttribute) profile.getAttribute("birthdate");
		dateAttr.setDate(this.getBirthdate(1985, 11, 21));
		MonoTextAttribute languageAttr = (MonoTextAttribute) profile.getAttribute("language");
		languageAttr.setText("en");
		_userRegManager.regAccount(profile);
	}
	
	private Date getBirthdate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date birthdate = new Date(calendar.getTimeInMillis());
		return birthdate;
	}
	
	private void init() throws Exception {
    	try {
    		this._userRegManager = (IUserRegManager) this.getService(JpUserRegSystemConstants.USER_REG_MANAGER);
    		this._userProfileManager = (IUserProfileManager) this.getService(SystemConstants.USER_PROFILE_MANAGER);
    		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
    		this._authorizationManager = (IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		UserRegDAO userRegDAO = new UserRegDAO();
    		userRegDAO.setDataSource(dataSource);
    		this._userRegDAO = userRegDAO;
    		JpUserRegTestHelper testHelper = new JpUserRegTestHelper();
    		testHelper.setDataSource(dataSource);
    		this._testHelper = testHelper;
		} catch (Exception e) {
			throw e;
		}
    }
	
	@Override
	protected void tearDown() throws Exception {
		this.activeMailManager(true);
		super.tearDown();
	}
	
	private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
	
	private IUserProfileManager _userProfileManager;
	private IUserManager _userManager;
	private IAuthorizationManager _authorizationManager;
	private IUserRegManager _userRegManager;
	private IUserRegDAO _userRegDAO;
	private JpUserRegTestHelper _testHelper;
	
}