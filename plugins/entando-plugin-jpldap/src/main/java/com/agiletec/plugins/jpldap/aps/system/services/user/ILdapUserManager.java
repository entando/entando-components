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
package com.agiletec.plugins.jpldap.aps.system.services.user;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * The interface for the LdapUserManager.
 * @author E.Santoboni
 */
public interface ILdapUserManager extends IUserManager {
	
	/**
	 * Returns the list of users drawn from the research.
	 * @param text The text to search on the username.
	 * @param japsUser true if you want to search on local users, 
	 * false if you want to search on non-local users, otherwise null.
	 * @return The users extracted.
	 * @throws ApsSystemException In case of error.
	 */
	public List<UserDetails> searchUsers(String text, Boolean japsUser) throws ApsSystemException;
	
	public boolean isWriteUserEnable();
	
}