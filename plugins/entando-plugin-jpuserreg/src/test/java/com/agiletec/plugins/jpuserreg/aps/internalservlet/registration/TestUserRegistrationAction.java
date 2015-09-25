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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.registration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import com.agiletec.plugins.jpuserreg.JpUserRegTestHelper;
import com.agiletec.plugins.jpuserreg.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpuserreg.aps.internalservlet.registration.UserRegistrationAction;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegDAO;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.UserRegDAO;

import com.opensymphony.xwork2.Action;
import static junit.framework.Assert.assertNotNull;

/**
 * @author zuanni G.Cocco
 */
public class TestUserRegistrationAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.activeMailManager(false);
	}
	
	public void testInitRegistration() throws Throwable {
		String result = this.executeInitRegistration();
		assertEquals(Action.SUCCESS, result);
		UserRegistrationAction action = (UserRegistrationAction) this.getAction();
		assertNotNull(action);
	}
	
	public void testRegister() throws Throwable {
		String username = "test_user.name";
		try {
			String result = this.executeInitRegistration();// Apertura sessione
			assertEquals(Action.SUCCESS, result);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", username);
			params.put("Monotext:fullname", "admin admin");
			params.put("Monotext:email", JpUserRegTestHelper.EMAIL);
			params.put("emailConfirm", JpUserRegTestHelper.EMAIL);
			params.put("privacyPolicyAgreement", "true");
			params.put("Monotext:language", "it");
			params.put("Date:birthdate", "01/01/1981");
			result = this.executeRegister(params);
			assertEquals(Action.SUCCESS, result);
			
			UserRegistrationAction action = (UserRegistrationAction) this.getAction();
			Map<String, List<String>> fieldErrors = action.getFieldErrors();
			assertNotNull(fieldErrors);
			assertEquals(0, fieldErrors.size());
			Set<String> keys = fieldErrors.keySet();
			assertEquals(0, keys.size());
		} finally {
			this.getUserManager().removeUser(username);
			_userRegDAO.clearTokenByUsername(username);
		}
	}
	
	public void testRegister_Requiredfields() throws Throwable {
		String result = this.executeInitRegistration();// Apertura sessione
		assertEquals(Action.SUCCESS, result);
		
		Map<String, String> params = new HashMap<String, String>();
		result = this.executeRegister(params);
		assertEquals(Action.INPUT, result);
		UserRegistrationAction reqAccountAction = (UserRegistrationAction) this.getAction();
		Collection<String> errors = reqAccountAction.getActionErrors();
		assertNotNull(errors);
		assertEquals(0, errors.size());
		Collection<String> messages = reqAccountAction.getActionMessages();
		assertNotNull(messages);
		assertEquals(0, messages.size());
		Map<String, List<String>> fieldErrors = reqAccountAction.getFieldErrors();
		assertNotNull(fieldErrors);
		assertEquals(7, fieldErrors.size());
		List<String> error = fieldErrors.get("privacyPolicyAgreement");
		assertNotNull(error);
		error = fieldErrors.get("username");
		assertNotNull(error);
		error = fieldErrors.get("Monotext:fullname");
		assertNotNull(error);
		error = fieldErrors.get("Monotext:language");
		assertNotNull(error);
		error = fieldErrors.get("Monotext:email");
		assertNotNull(error);
		error = fieldErrors.get("Monotext:email");
		assertNotNull(error);
		error = fieldErrors.get("emailConfirm");
		assertNotNull(error);
		
		params.put("username", "user#name");
		result = this.executeRegister(params);
		assertEquals(Action.INPUT, result);
		reqAccountAction = (UserRegistrationAction) this.getAction();
		fieldErrors = reqAccountAction.getFieldErrors();
		assertEquals(7, fieldErrors.size());
		assertNotNull(fieldErrors.get("username"));
	}
	
	public void testSaveRegistration_UserAlreadyPresent() throws Throwable {
		String result = this.executeInitRegistration();// Apertura sessione
		assertEquals(Action.SUCCESS, result);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "admin");
		params.put("Date:birthdate", "01/01/1981");
		result = this.executeRegister(params);
		assertEquals(Action.INPUT, result);
		UserRegistrationAction reqAccountAction = (UserRegistrationAction) this.getAction();
		Map<String, List<String>> fieldErrors = reqAccountAction.getFieldErrors();
		assertNotNull(fieldErrors);
		assertEquals(6, fieldErrors.size());
		List<String> errors = fieldErrors.get("username");
		assertNotNull(errors);
		assertEquals(1, errors.size());
		assertEquals(errors.get(0), this.getAction().getText("jpuserreg.error.duplicateUser"));
	}
	
	public void testSaveRegistration_EmailAlreadyPresent() throws Throwable {
		String username_1 = "test_user_1";
		String username_2 = "test_user_2";
		try {
			String result = this.executeInitRegistration();// Apertura sessione
			assertEquals(Action.SUCCESS, result);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("username", username_1);
			params.put("Monotext:fullname", "admin admin");
			params.put("Monotext:email", JpUserRegTestHelper.EMAIL);
			params.put("emailConfirm", JpUserRegTestHelper.EMAIL);
			params.put("privacyPolicyAgreement", "true");
			params.put("Monotext:language", "it");
			params.put("Date:birthdate", "01/01/1981");
			result = this.executeRegister(params);
			assertEquals(Action.SUCCESS, result);
			UserRegistrationAction action = (UserRegistrationAction) this.getAction();
			Map<String, List<String>> fieldErrors = action.getFieldErrors();
			assertNotNull(fieldErrors);
			assertEquals(0, fieldErrors.size());
			Set<String> keys = fieldErrors.keySet();
			assertEquals(0, keys.size());
			
			params.put("username", username_2);
			result = this.executeRegister(params);
			assertEquals("expired", result);// Sessione scaduta
			
			result = this.executeInitRegistration();// Ripristino sessione
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeRegister(params);
			assertEquals(Action.INPUT, result);
			action = (UserRegistrationAction) this.getAction();
			fieldErrors = action.getFieldErrors();
			assertNotNull(fieldErrors);
			assertEquals(1, fieldErrors.size());
			assertEquals("email", (String) fieldErrors.keySet().iterator().next());
			assertEquals((fieldErrors.get("email")).get(0), action.getText("jpuserreg.error.email.alreadyInUse"));
		} finally {
			this.getUserManager().removeUser(username_1);
			_userRegDAO.clearTokenByUsername(username_1);
		}
	}
	
	private String executeInitRegistration() throws Throwable {
		this.initAction("/do/jpuserreg/UserReg", "initRegistration");
		String result = this.executeAction();
		return result;
	}
	
	private String executeRegister(Map<String, String> params) throws Throwable {
		this.initAction("/do/jpuserreg/UserReg", "register");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}
	
	private void init() {
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		UserRegDAO userRegDAO = new UserRegDAO();
		userRegDAO.setDataSource(dataSource);
		this._userRegDAO = userRegDAO;
	}
	
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	public IUserManager getUserManager() {
		return _userManager;
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
	
	private IUserManager _userManager;
	private IUserRegDAO _userRegDAO;
	
}