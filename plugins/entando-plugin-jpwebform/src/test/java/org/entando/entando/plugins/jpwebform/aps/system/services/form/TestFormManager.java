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
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.util.ApsProperties;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;
import org.entando.entando.plugins.jpwebform.aps.ApsPluginBaseTestCase;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.SmallMessageType;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;

/**
 * @author E.Santoboni
 */
public class TestFormManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testGetMessageTypes() throws Throwable {
		List<SmallMessageType> messageTypes = this._formManager.getSmallMessageTypes();
		assertNotNull(messageTypes);
		assertEquals(2, messageTypes.size());
		SmallMessageType smt = messageTypes.get(0);
		assertEquals("COM", smt.getCode());
	}
	
	public void testGetMessageType() throws Throwable {
		
		Message prototype = (Message) this._formManager.getEntityPrototype("COM");
		assertNotNull(prototype);
		assertEquals("COM", prototype.getTypeCode());
		StepsConfig stepsConfig = this._formManager.getStepsConfig("COM");
		assertNotNull(stepsConfig);
		assertEquals(2, stepsConfig.getSteps().size());
	}
	
	
	public void testExpression() throws Throwable {
		Message prototype = (Message) this._formManager.getEntityPrototype("COM");
		assertNotNull(prototype);
		assertEquals("COM", prototype.getTypeCode());
		MonoTextAttribute attribute = (MonoTextAttribute) prototype.getAttribute("Company");
		attribute.setText("test");
		String expression = "#entity.getAttribute(\"Company\").text == \"test\"";
		Boolean value = this._formManager.validateOgnl(expression, prototype,"admin");
		assertTrue(value);
		String expressionFalse = "getAttribute(\"Company\").text != \"test\"";
		Boolean valueFalse = this._formManager.validateOgnl(expressionFalse, prototype, "testUser");
		assertFalse(valueFalse);
	}
	
	public void testVerifyOgnlExpression() throws Throwable {
		Message prototype = (Message) this._formManager.getEntityPrototype("COM");
		assertNotNull(prototype);
		assertEquals("COM", prototype.getTypeCode());
		MonoTextAttribute attribute = (MonoTextAttribute) prototype.getAttribute("Company");
		attribute.setText("test");
		String expression = "#entity.getAttribute(\"Company\").text == \"test\"";
		IFormManager.OGNL_MESSAGES value = this._formManager.verifyOgnlExpression(expression, prototype,"admin");
		assertEquals(IFormManager.OGNL_MESSAGES.SUCCESS, value);
		expression = "#entity.getAttribute(\"Company\").test == \"test\"";
		value = this._formManager.verifyOgnlExpression(expression, prototype,"admin");
		assertEquals(IFormManager.OGNL_MESSAGES.PROPERTY_ERROR, value);
		expression = "#entity.getAttribute(\"Casdompany\").text == \"test\"";
		value = this._formManager.verifyOgnlExpression(expression, prototype,"admin");
		assertEquals(IFormManager.OGNL_MESSAGES.GENERIC_ERROR, value);
		expression = "#entity.getAttasdribute(\"Casdompany\").text == \"test\"";
		value = this._formManager.verifyOgnlExpression(expression, prototype,"admin");
		assertEquals(IFormManager.OGNL_MESSAGES.METHOD_ERROR, value);
	}
	
	public void testCreateMessageLabel() throws Throwable {
		Message prototype = (Message) this._formManager.getEntityPrototype("COM");
		Integer version = prototype.getVersionType();
		String time = String.valueOf(System.currentTimeMillis());
		StepsConfig stepsConfig = this._formManager.getStepsConfig("COM");
		List<Step> steps = stepsConfig.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			String stepTitleCode = "jpwebform_TITLE_" + prototype.getTypeCode() + "_" + version + "_" + step.getCode();
			ApsProperties labelGroup = this._i18nManager.getLabelGroup(stepTitleCode);
			assertNotNull(labelGroup);
			assertEquals(2, labelGroup.size());
			Iterator<Object> iter = labelGroup.keySet().iterator();
			while (iter.hasNext()) {
				Object key = iter.next();
				String value = labelGroup.getProperty(key.toString());
				String newValue = value + time;
				labelGroup.put(key, newValue);
			}
			this._i18nManager.updateLabelGroup(stepTitleCode, labelGroup);
			labelGroup = this._i18nManager.getLabelGroup(stepTitleCode);
			iter = labelGroup.values().iterator();
			while (iter.hasNext()) {
				String value = iter.next().toString();
				assertTrue(value.endsWith(time));
			}
		}
		
		this._formManager.generateNewVersionType("COM");
		prototype = (Message) this._formManager.getEntityPrototype("COM");
		Integer newVersion = prototype.getVersionType();
		assertTrue(newVersion > version);
		stepsConfig = this._formManager.getStepsConfig("COM");
		steps = stepsConfig.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			String stepTitleCode = "jpwebform_TITLE_" + prototype.getTypeCode() + "_" + newVersion + "_" + step.getCode();
			ApsProperties labelGroup = this._i18nManager.getLabelGroup(stepTitleCode);
			assertNotNull(labelGroup);
			Iterator<Object> iter = labelGroup.values().iterator();
			while (iter.hasNext()) {
				String value = iter.next().toString();
				assertTrue(value.endsWith(time));
			}
		}
	}
	
	private void init() throws Exception {
		try {
			this._userProfileManager = (IUserProfileManager) super.getService(SystemConstants.USER_PROFILE_MANAGER);
			Date birthdate = this.getBirthdate(1970, 1, 1);
			IUserProfile profile = this.createProfile("test", "testtest", "test@test.com", birthdate, "it");
			this._userProfileManager.addProfile("admin", profile);
			this._formManager = (IFormManager) super.getService(JpwebformSystemConstants.FORM_MANAGER);
			this._i18nManager = (II18nManager) super.getService(SystemConstants.I18N_MANAGER);
        } catch (Throwable e) {
            throw new Exception(e);
        }
	}
	
	private Date getBirthdate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date birthdate = new Date(calendar.getTimeInMillis());
		return birthdate;
	}
	
	private IUserProfile createProfile(String name,	String surname, String email, Date birthdate, String language) {
		IUserProfile profile = this._userProfileManager.getDefaultProfileType();
		MonoTextAttribute nameAttr = (MonoTextAttribute) profile.getAttribute("fullname");
		nameAttr.setText(name + " " + surname);
		MonoTextAttribute emailAttr = (MonoTextAttribute) profile.getAttribute("email");
		emailAttr.setText(email);
		DateAttribute birthdateAttr = (DateAttribute) profile.getAttribute("birthdate");
		birthdateAttr.setDate(birthdate);
		MonoTextAttribute languageAttr = (MonoTextAttribute) profile.getAttribute("language");
		languageAttr.setText(language);
		((UserProfile) profile).setPublicProfile(true);
		return profile;
	}
	
	@Override
    protected void tearDown() throws Exception {
        this._userProfileManager.deleteProfile("admin");
        super.tearDown();
    }
	
	private IUserProfileManager _userProfileManager;
	private IFormManager _formManager;
	private II18nManager _i18nManager;
	private static final String USERNAME_FOR_TEST = "testUser";
    private IApsAuthority _role = null;
	
}