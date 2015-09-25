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
package com.agiletec.plugins.jpcasclient.aps.system.services.user;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.AuthenticationProviderManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Extends the default AuthenticationProviderManager to provide a method
 * for loading user and user authorizations with username and without password.
 * 
 * */
public class CasAuthProviderManager extends AuthenticationProviderManager {
	
	public UserDetails getUser(String username) throws ApsSystemException {
		UserDetails user = null;
		try {
			user = this.getUserManager().getUser(username);
			if (null == user || (null != user && user.isDisabled())) return null;
			if (!user.getUsername().equals(SystemConstants.ADMIN_USER_NAME)) {
				if (!user.isAccountNotExpired()) {
					ApsSystemUtils.getLogger().info("User '" + user.getUsername() + "' expired");
					return user;
				}
			}
			this.getUserManager().updateLastAccess(user);
			if (!user.isCredentialsNotExpired()) {
				ApsSystemUtils.getLogger().info("User '" + user.getUsername() + "' credential expired");
				return user;
			}
			this.addUserAuthorizations(user);
		} catch (Throwable t) {
			throw new ApsSystemException("Error in user authentication " + username, t);
		}
		return user;
	}

}
