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
package com.agiletec.plugins.jpblog.aps.system.common.entity.model.attribute;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.attribute.AbstractTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;

public class AuthorAttribute extends MonoTextAttribute {

	private static final Logger _logger =  LoggerFactory.getLogger(AuthorAttribute.class);
	
	public String getAuthorname() {
		String username = super.getText();
		String authorname = username;
		try {
			IUserProfileManager userProfileManager = (IUserProfileManager) this.getBeanFactory().getBean(SystemConstants.USER_PROFILE_MANAGER);
			IUserProfile userProfile = userProfileManager.getProfile(username);
			if (null != userProfile) {
				String fullname = this.getProfileAttribute(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FULL_NAME, userProfile);
				if (null != fullname) {
					authorname = fullname;
				} else {
					String name = this.getProfileAttribute(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_FIRST_NAME, userProfile);
					String surname = this.getProfileAttribute(SystemConstants.USER_PROFILE_ATTRIBUTE_ROLE_SURNAME, userProfile);
					if (null != name || null != surname) {
						StringBuilder sbBuffer = new StringBuilder();
						if (StringUtils.isNotBlank(name)) {
							sbBuffer.append(name);
						}
						if (StringUtils.isNotBlank(surname)) {
							sbBuffer.append(" ").append(surname);
						}
						authorname = sbBuffer.toString();
					}
				}
			}
			if(StringUtils.isEmpty(authorname)){
				authorname = username;
			}
		} catch (Throwable t) {
			_logger.error("Error extracting profile attributes for author attribute", t);
		}
		return authorname;
	}
	
	private String getProfileAttribute(String role, IUserProfile userProfile) {
		String value = null;
		AttributeInterface attr = userProfile.getAttributeByRole(role);
		if (null != attr && attr instanceof AbstractTextAttribute) {
			value = ((AbstractTextAttribute)attr).getText();
		}
		return value;
	}
	
}
