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
package com.agiletec.plugins.jpldap.apsadmin.user;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpldap.aps.system.services.user.ILdapUserManager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;

/**
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
				Boolean entandoUser = (userType == 1);
				List<String> ldapUsernames = this.getLdapUsernames();
				List<String> newList = null;
				if (entandoUser) {
					newList = (List<String>) ListUtils.removeAll(mainSearchResult, ldapUsernames);
				} else {
					newList = (List<String>) ListUtils.intersection(mainSearchResult, ldapUsernames);
				}
				return newList;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSearchResult");
			throw new RuntimeException("Error while searching users", t);
		}
	}
	
	protected List<String> getLdapUsernames() throws ApsSystemException {
		List<UserDetails> users = ((ILdapUserManager) this.getUserManager()).searchUsers(this.getUsername(), false);
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
