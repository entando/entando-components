/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.plugins.jacms.aps.system.services.content.command.common;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.context.BaseBulkCommandContext;
import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

import com.agiletec.aps.system.services.user.UserDetails;

public class ContentBulkCommandContext extends BaseBulkCommandContext<String> {

	public ContentBulkCommandContext(Collection<String> items, UserDetails currentUser, BulkCommandTracer<String> tracer) {
		super(items, tracer);
		this.setCurrentUser(currentUser);
	}

	public UserDetails getCurrentUser() {
		return _currentUser;
	}
	public void setCurrentUser(UserDetails currentUser) {
		this._currentUser = currentUser;
	}

	private UserDetails _currentUser;

}
