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
package com.agiletec.plugins.jpactionlogger.apsadmin.actionlogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.aps.system.services.actionlog.IActionLogManager;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecord;
import org.entando.entando.aps.system.services.actionlog.model.ActionLogRecordSearchBean;
import org.entando.entando.aps.system.services.actionlog.model.IActionLogRecordSearchBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.Action;

public class ActionLoggerAction extends BaseAction {
	
	private static final Logger _logger = LoggerFactory.getLogger(ActionLoggerAction.class);
	
	public List<Integer> getActionRecords() {
		List<Integer> actionRecords = new ArrayList<Integer>();
		try {
			IActionLogRecordSearchBean searchBean = this.prepareSearchBean();
			actionRecords = this.getActionLogManager().getActionRecords(searchBean);
		} catch (Throwable t) {
			_logger.error("error in getActionRecords", t);
		}
		return actionRecords;
	}
	
	public String delete() {
		try {
			this.getActionLogManager().deleteActionRecord(this.getId());
		} catch (Throwable t) {
			_logger.error("error in delete", t);
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	
	public ActionLogRecord getActionRecord(int id) {
		ActionLogRecord actionRecord = null;
		try {
			actionRecord = this.getActionLogManager().getActionRecord(id);
		} catch (Throwable t) {
			_logger.error("error in getActionRecord", t);
		}
		return actionRecord;
	}
	
	protected IActionLogRecordSearchBean prepareSearchBean() {
		ActionLogRecordSearchBean searchBean = new ActionLogRecordSearchBean();
		searchBean.setUsername(this.getUsername());
		searchBean.setActionName(this.getActionName());
		searchBean.setNamespace(this.getNamespace());
		searchBean.setParams(this.getParams());
		searchBean.setStartCreation(this.getStart());
		Date end = this.getEnd();
		if (end != null) {
			searchBean.setEndCreation(new Date(end.getTime() + 86400000)); // un giorno dopo
		}
		return searchBean;
	}
	
	public void setId(int id) {
		this._id = id;
	}
	public int getId() {
		return _id;
	}
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	public String getNamespace() {
		return _namespace;
	}
	
	public void setActionName(String actionName) {
		this._actionName = actionName;
	}
	public String getActionName() {
		return _actionName;
	}
	
	public void setParams(String params) {
		this._params = params;
	}
	public String getParams() {
		return _params;
	}
	
	public void setStart(Date start) {
		this._start = start;
	}
	public Date getStart() {
		return _start;
	}
	
	public void setEnd(Date end) {
		this._end = end;
	}
	public Date getEnd() {
		return _end;
	}
	
	protected IActionLogManager getActionLogManager() {
		return _actionLogManager;
	}
	public void setActionLogManager(IActionLogManager actionLogManager) {
		this._actionLogManager = actionLogManager;
	}
	
	private int _id;
	private String _username;
	private String _namespace;
	private String _actionName;
	private String _params;
	private Date _start;
	private Date _end;
	
	private IActionLogManager _actionLogManager;
	
}