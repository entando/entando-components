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
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;

public class IdeaInstanceManager extends AbstractService implements IIdeaInstanceManager, GroupUtilizer {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaInstanceManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public IdeaInstance getIdeaInstance(String code) throws ApsSystemException {
		IdeaInstance ideainstance = null;
		try {
			ideainstance = this.getIdeaInstanceDAO().loadIdeaInstance(code, null);
		} catch (Throwable t) {
			_logger.error("Error loading ideainstance with code {}", code, t);
			throw new ApsSystemException("Error loading ideainstance with code: " + code, t);
		}
		return ideainstance;
	}

	public IdeaInstance getIdeaInstance(String code, Collection<Integer> status) throws ApsSystemException {
		IdeaInstance ideainstance = null;
		try {
			ideainstance = this.getIdeaInstanceDAO().loadIdeaInstance(code, status);
		} catch (Throwable t) {
			_logger.error("Error loading ideainstance with code {}", code, t);
			throw new ApsSystemException("Error loading ideainstance with code: " + code, t);
		}
		return ideainstance;
	}

	@Override
	public List<String> getIdeaInstances() throws ApsSystemException {
		List<String> ideainstances = new ArrayList<String>();
		try {
			ideainstances = this.getIdeaInstanceDAO().loadIdeaInstances();
		} catch (Throwable t) {
			_logger.error("Error loading IdeaInstance ", t);
			throw new ApsSystemException("Error loading IdeaInstance ", t);
		}
		return ideainstances;
	}

	@Override
	public List<String> getIdeaInstances(Collection<String> groupNames, String code) throws ApsSystemException {
		List<String> ideainstances = new ArrayList<String>();
		try {
			ideainstances = this.getIdeaInstanceDAO().searchIdeaInstances(groupNames, code);
		} catch (Throwable t) {
			_logger.error("Error searching IdeaInstances", t);
			throw new ApsSystemException("Error searching IdeaInstances ", t);
		}
		return ideainstances;
	}

	@Override
	public void addIdeaInstance(IdeaInstance ideainstance) throws ApsSystemException {
		try {
			ideainstance.setCreatedat(new Date());
			this.getIdeaInstanceDAO().insertIdeaInstance(ideainstance);
		} catch (Throwable t) {
			_logger.error("Error adding IdeaInstance", t);
			throw new ApsSystemException("Error adding IdeaInstance", t);
		}
	}

	@Override
	public void updateIdeaInstance(IdeaInstance ideainstance) throws ApsSystemException {
		try {
			this.getIdeaInstanceDAO().updateIdeaInstance(ideainstance);
		} catch (Throwable t) {
			_logger.error("Error updating IdeaInstance", t);
			throw new ApsSystemException("Error updating IdeaInstance " + ideainstance, t);
		}
	}

	@Override
	public void deleteIdeaInstance(String code) throws ApsSystemException {
		try {
			this.getIdeaInstanceDAO().removeIdeaInstance(code);
		} catch (Throwable t) {
			_logger.error("Error deleting IdeaInstance wiwh code {}", code, t);
			throw new ApsSystemException("Error deleting IdeaInstance wiwh code:" + code, t);
		}
	}

	@Override
	public List<IdeaInstance> getGroupUtilizers(String groupName) throws ApsSystemException {
		List<String> instances = null;
		List<IdeaInstance> utilizers = new ArrayList<IdeaInstance>();
		try {
			List<String> groups = new ArrayList<String>();
			groups.add(groupName);
			instances = this.getIdeaInstanceDAO().searchIdeaInstances(groups, null);
			if (instances != null) {
				for (int i=0; i<instances.size(); i++) {
					String code = instances.get(i);
					IdeaInstance instance = this.getIdeaInstance(code);
					if (null != instance) {
						utilizers.add(instance);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error while loading the members of the group {}", groupName, t);
			throw new ApsSystemException("Error while loading the members of the group "+ groupName, t);
		}
		return utilizers;
	}


	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	protected IIdeaInstanceDAO getIdeaInstanceDAO() {
		return _ideaInstanceDAO;
	}
	public void setIdeaInstanceDAO(IIdeaInstanceDAO ideaInstanceDAO) {
		this._ideaInstanceDAO = ideaInstanceDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IIdeaInstanceDAO _ideaInstanceDAO;

}
