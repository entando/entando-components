/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.model;

public class TicketSearchBean implements ITicketSearchBean {
	
	@Override
	public String getMessage() {
		return _message;
	}
	public void setMessage(String message) {
		this._message = message;
	}
	
	@Override
	public String getAuthor() {
		return _author;
	}
	public void setAuthor(String author) {
		this._author = author;
	}
	
	@Override
	public String getOperator() {
		return _operator;
	}
	public void setOperator(String operator) {
		this._operator = operator;
	}
	
	@Override
	public String[] getWttRoles() {
		return _wttRoles;
	}
	public void setWttRoles(String[] wttRoles) {
		this._wttRoles = wttRoles;
	}
	
	@Override
	public int[] getStates() {
		return _states;
	}
	public void setStates(int[] states) {
		this._states = states;
	}
	
	@Override
	public Integer getPriority() {
		return _priority;
	}
	public void setPriority(Integer priority) {
		this._priority = priority;
	}
	
	@Override
	public Integer getUserInterventionType() {
		return _userInterventionType;
	}
	public void setUserInterventionType(Integer userInterventionType) {
		this._userInterventionType = userInterventionType;
	}
	
	@Override
	public Integer getAssignedInterventionType() {
		return _assignedInterventionType;
	}
	public void setAssignedInterventionType(Integer assignedInterventionType) {
		this._assignedInterventionType = assignedInterventionType;
	}
	
	@Override
	public Boolean getResolved() {
		return _resolved;
	}
	public void setResolved(Boolean resolved) {
		this._resolved = resolved;
	}
	
	public String _message;
	public String _author;
	public String _operator;
	public String[] _wttRoles;
	public int[] _states;
	public Integer _status;
	public Integer _priority;
	public Integer _userInterventionType;
	public Integer _assignedInterventionType;
	public Boolean _resolved;
	
}