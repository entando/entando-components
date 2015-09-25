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
package com.agiletec.plugins.jpcrowdsourcing.apsadmin.idea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.ApsAdminSystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.IIdeaManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea.Idea;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IIdeaInstanceManager;
import com.agiletec.plugins.jpcrowdsourcing.aps.system.services.ideainstance.IdeaInstance;

public class IdeaFinderAction extends BaseAction implements IIdeaFinderAction {

	private static final Logger _logger =  LoggerFactory.getLogger(IdeaFinderAction.class);

	@Override
	public List<String> getIdeas() {
		List<String> list = new ArrayList<String>();
		try {
			list = this.getIdeaManager().searchIdeas(this.getIdeaInstance(),this.getSearchStatus(), this.getText(), null, null);
		} catch (Throwable t) {
			_logger.error("error in getIdeas", t);
			throw new RuntimeException("Errore in caricamento lista idee");
		}
		return list;
	}

	@Override
	public IIdea getIdea(String code) {
		IIdea idea = null;
		try {
			idea = this.getIdeaManager().getIdea(code);
		} catch (Throwable t) {
			_logger.error("error in getIdea", t);
			throw new RuntimeException("Errore in caricamento idea " + code);
		}
		return idea;
	}

	@Override
	public String changeStatus() {
		try {
			IIdea idea = this.getIdeaManager().getIdea(this.getIdeaId());
			if (null == idea) {
				this.addActionError(this.getText("jpcrowdsourcing.idea.null"));
				return INPUT;
			}
			((Idea)idea).setStatus(this.getStatus());
			this.getIdeaManager().updateIdea(idea);
		} catch (Throwable t) {
			_logger.error("error in changeStatus", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String trash() {
		try {
			IIdea idea = this.getIdeaManager().getIdea(this.getIdeaId());
			if (null == idea) {
				this.addActionError(this.getText("jpcrowdsourcing.idea.null"));
				return INPUT;
			}
			this.setStrutsAction(ApsAdminSystemConstants.DELETE);
		} catch (Throwable t) {
			_logger.error("error in trash", t);
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String delete() {
		try {
			if (this.getStrutsAction() != ApsAdminSystemConstants.DELETE) {
				return INPUT;
			}
			if (null != this.getIdeaId() && this.getIdeaId().trim().length() > 0) {
				this.getIdeaManager().deleteIdea(this.getIdeaId());
			}
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		this.addActionMessage(this.getText("jpcrowdsourcing.idea.trash"));
		return SUCCESS;
	}

	public List<IdeaInstance> getIdeaInstances() {
		List<IdeaInstance> ideaInstances = new ArrayList<IdeaInstance>();
		try {
			List<String> codes = this.getIdeaInstanceManager().getIdeaInstances(null, null);
			if (null != codes && !codes.isEmpty()) {
				Iterator<String> it = codes.iterator();
				while (it.hasNext()) {
					String code = it.next();
					IdeaInstance ideaInstance = this.getIdeaInstanceManager().getIdeaInstance(code);
					if (null != ideaInstance) {
						ideaInstances.add(ideaInstance);
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("error in getIdeaInstances", t);
			throw new RuntimeException("error in getIdeaInstances");
		}
		return ideaInstances;
	}


	public void setText(String text) {
		this._text = text;
	}
	public String getText() {
		return _text;
	}

	public void setStatus(Integer status) {
		this._status = status;
	}
	public Integer getStatus() {
		return _status;
	}

	public void setIdeaId(String ideaId) {
		this._ideaId = ideaId;
	}
	public String getIdeaId() {
		return _ideaId;
	}

	public void setStrutsAction(int strutsAction) {
		this._strutsAction = strutsAction;
	}
	public int getStrutsAction() {
		return _strutsAction;
	}

	public void setSearchStatus(Integer searchStatus) {
		this._searchStatus = searchStatus;
	}
	public Integer getSearchStatus() {
		return _searchStatus;
	}

	public String getIdeaInstance() {
		return _ideaInstance;
	}

	public void setIdeaInstance(String ideaInstance) {
		this._ideaInstance = ideaInstance;
	}

	public void setIdeaManager(IIdeaManager ideaManager) {
		this._ideaManager = ideaManager;
	}
	protected IIdeaManager getIdeaManager() {
		return _ideaManager;
	}

	protected IIdeaInstanceManager getIdeaInstanceManager() {
		return _ideaInstanceManager;
	}
	public void setIdeaInstanceManager(IIdeaInstanceManager ideaInstanceManager) {
		this._ideaInstanceManager = ideaInstanceManager;
	}

	private IIdeaManager _ideaManager;
	private IIdeaInstanceManager _ideaInstanceManager;
	private int _strutsAction;
	private String _text;
	private Integer _searchStatus;
	private Integer _status;
	private String _ideaId;
	private String _ideaInstance;

}
