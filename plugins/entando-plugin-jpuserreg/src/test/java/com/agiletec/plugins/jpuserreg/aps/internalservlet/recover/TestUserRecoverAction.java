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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.recover;

import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import com.agiletec.plugins.jpuserreg.JpUserRegTestHelper;
import com.agiletec.plugins.jpuserreg.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;
import com.agiletec.plugins.jpuserreg.aps.JpUserRegSystemConstants;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;

import com.opensymphony.xwork2.Action;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;

/**
 * @author G.Cocco
 * */
public class TestUserRecoverAction  extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.activeMailManager(false);
	}
	
	public void testRecoverFromUsername_RecoverFromEmail() throws Throwable{
		String username = "username_test";
		try {
			this.insertTestProfile(username);
			User user = (User) _userManager.getUser(username);
			assertNotNull(user);
			IUserProfile profile = (IUserProfile) user.getProfile();
			assertNotNull(profile);
			String token_0 = this._testHelper.getTokenFromUsername(username);
			assertNotNull(token_0);
			
			this.initAction("/do/jpuserreg/UserReg", "recoverFromUsername");
			this.addParameter("username", username);
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			String token_1 = this._testHelper.getTokenFromUsername(username);
			assertNotNull(token_1);
			assertFalse(token_0.equals(token_1));
			
			this.initAction("/do/jpuserreg/UserReg", "recoverFromEmail");
			this.addParameter("email", JpUserRegTestHelper.EMAIL);
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			
			String token_2 = this._testHelper.getTokenFromUsername(username);
			assertNotNull(token_2);
			assertFalse(token_1.equals(token_2));
		} finally {
			//	clean
			_testHelper.clearTokenByUsername(username);
			_userManager.removeUser(username);
		}
	}
	
	private void insertTestProfile(String username) throws Exception {
		IUserProfile profile = _userProfileManager.getDefaultProfileType();
		profile.setId(username);
		MonoTextAttribute fullnameAttr = (MonoTextAttribute) profile.getAttribute("fullname");
		fullnameAttr.setText("name surname");
		MonoTextAttribute emailAttr = (MonoTextAttribute) profile.getAttribute("email");
		emailAttr.setText(JpUserRegTestHelper.EMAIL);
		DateAttribute dateAttr = (DateAttribute) profile.getAttribute("birthdate");
		dateAttr.setDate(this.getBirthdate(1985, 11, 21));
		MonoTextAttribute languageAttr = (MonoTextAttribute) profile.getAttribute("language");
		languageAttr.setText("en");
		this._regAccountManager.regAccount(profile);
	}
	
	private Date getBirthdate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date birthdate = new Date(calendar.getTimeInMillis());
		return birthdate;
	}
	
	private void init() {
		this._regAccountManager = (IUserRegManager) this.getService(JpUserRegSystemConstants.USER_REG_MANAGER);
		this._userProfileManager = (IUserProfileManager) this.getService(SystemConstants.USER_PROFILE_MANAGER);
		this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		JpUserRegTestHelper testHelper = new JpUserRegTestHelper();
		testHelper.setDataSource(dataSource);
		this._testHelper = testHelper;
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
	private IUserRegManager _regAccountManager;
	private IUserManager _userManager;
	private JpUserRegTestHelper _testHelper;
	
}