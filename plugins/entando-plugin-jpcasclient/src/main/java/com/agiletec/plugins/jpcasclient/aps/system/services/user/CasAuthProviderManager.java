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
