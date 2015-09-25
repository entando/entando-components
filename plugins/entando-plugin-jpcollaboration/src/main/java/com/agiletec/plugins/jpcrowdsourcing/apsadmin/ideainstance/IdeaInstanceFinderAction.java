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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.ideainstance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;


public class IdeaInstanceFinderAction extends BaseAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaInstanceFinderAction.class);

	public List<String> getIdeaInstancesId() {
		try {
			List<String> groups = new ArrayList<String>();

			if (StringUtils.isNotBlank(this.getGroupName())) {
				groups.add(this.getGroupName());
			}
			List<String> ideaInstances = this.getIdeaInstanceManager().getIdeaInstances(groups, this.getCode());
			return ideaInstances;
		} catch (Throwable t) {
			_logger.error("Error getting ideaInstances list", t);
			throw new RuntimeException("Error getting ideaInstances list", t);
		}
	}

	public IdeaInstance getIdeaInstance(String code) {
		IdeaInstance ideaInstance = null;
		try {
			ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(code);
		} catch (Throwable t) {
			_logger.error("Error getting ideaInstance with code {}", code, t);
			throw new RuntimeException("Error getting ideaInstance with code " + code, t);
		}
		return ideaInstance;
	}

	public List<Group> getSystemGroups() {
		List<Group> groups = null;
		try {
			groups = this.getGroupManager().getGroups();
		} catch (Throwable t) {
			_logger.error("Error loading system groups", t);
			throw new RuntimeException("Error loading system groups");
		}
		return groups;
	}

	public String getGroupName() {
		return _groupName;
	}
	public void setGroupName(String groupName) {
		this._groupName = groupName;
	}

	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}


	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}


	private String _groupName;
	private String _code;
	private IGroupManager _groupManager;
	private IIdeaInstanceManager _ideaInstanceManager;

}