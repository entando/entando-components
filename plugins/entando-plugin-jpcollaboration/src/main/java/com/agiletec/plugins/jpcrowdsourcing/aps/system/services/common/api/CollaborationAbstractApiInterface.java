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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.common.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;

public abstract class CollaborationAbstractApiInterface {

	
	/**
	 * Extract from the api properties the group codes of the user
	 * @param properties the api properties
	 * @return the user's groups list
	 */
	protected Set<String> extractGroups(Properties properties) {
		UserDetails user = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
		return this.extractGroups(user);
	}
	
	/**
	 * Extract from the api properties the group codes of the user
	 * @param user the api user
	 * @return the user's groups list
	 */
	protected Set<String> extractGroups(UserDetails user) {
		Set<String> groupCodes = new HashSet<String>();
		if (null != user) {
			List<Group> groups = this.getAuthorizationManager().getUserGroups(user);
			if (null != groups) {
				Iterator<Group> it = groups.iterator();
				while (it.hasNext()) {
					Group currentGroup = it.next();
					groupCodes.add(currentGroup.getName());
				}
			}
		}
		groupCodes.add(Group.FREE_GROUP_NAME);
		return groupCodes;
	}

	protected Collection<Integer> extractIdeaStatusListForUser(UserDetails user) {
		List<Integer> ideaStateFilter = new ArrayList<Integer>();
		ideaStateFilter.add(IIdea.STATUS_APPROVED);
		//TODO CREATE ROLE
		if (null != user && !this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERUSER)) {
			ideaStateFilter.add(IIdea.STATUS_NOT_APPROVED);
			ideaStateFilter.add(IIdea.STATUS_TO_APPROVE);
		}
		return ideaStateFilter;
	}
	
	public boolean isAuthOnInstance(UserDetails user, IdeaInstance instance) {
		boolean auth = false;
		Collection<String> groupCodes = this.extractGroups(user);
		if (CollectionUtils.containsAny(groupCodes, instance.getGroups())) {
			auth = true;
		}
		return auth;
	}
	

	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}


	private IAuthorizationManager _authorizationManager;
}
