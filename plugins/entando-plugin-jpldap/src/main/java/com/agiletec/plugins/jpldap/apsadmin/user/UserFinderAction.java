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
package com.agiletec.plugins.jpldap.apsadmin.user;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpldap.aps.system.services.user.ILdapUserManager;
import java.util.ArrayList;
import org.apache.commons.collections.CollectionUtils;

/**
 * Classe action delegate alla ricerca utenti.
 * @author E.Santoboni
 */
public class UserFinderAction extends org.entando.entando.apsadmin.user.UserProfileFinderAction {
	
	@Override
	public List<String> getSearchResult() {
		List<String> mainSearchResult = super.getSearchResult();
		try {
			Integer userType = this.getUserType();
			if (null == userType || userType == 0) {
				return mainSearchResult;
			} else {
				Boolean entandoUser = userType.intValue() == 1;
				List<String> ldapUsernames = this.getLdapUsernames();
				if (entandoUser) {
					return (List<String>) CollectionUtils.removeAll(mainSearchResult, ldapUsernames);
				} else {
					return (List<String>) CollectionUtils.intersection(mainSearchResult, ldapUsernames);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSearchResult");
			throw new RuntimeException("Error while searching users", t);
		}
	}
	
	protected List<String> getLdapUsernames() throws ApsSystemException {
		List<UserDetails> users = ((ILdapUserManager) this.getUserManager()).searchUsers(this.getUsername(), true);
		List<String> usernames = new ArrayList<String>();
		if (null != users) {
			for (int i = 0; i < users.size(); i++) {
				UserDetails user = users.get(i);
				usernames.add(user.getUsername());
			}
		}
		return usernames;
	}
	
	public Integer getUserType() {
		return _userType;
	}
	public void setUserType(Integer userType) {
		this._userType = userType;
	}
	
	private Integer _userType;
	
}