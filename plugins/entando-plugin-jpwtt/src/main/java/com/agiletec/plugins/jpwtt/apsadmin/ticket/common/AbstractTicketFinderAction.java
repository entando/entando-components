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
package com.agiletec.plugins.jpwtt.apsadmin.ticket.common;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.ITicketManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.ITicketSearchBean;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketSearchBean;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.ITicketFinderAction;

public abstract class AbstractTicketFinderAction extends BaseAction implements ITicketFinderAction {
	
	@Override
	public List<String> getTicketIds() {
		List<String> ticketIds = null;
		try {
			ITicketSearchBean searchBean = this.prepareSearchBean();
			ticketIds = this.getTicketManager().searchTicketIds(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTicketIds");
			throw new RuntimeException("Error searching tickets", t);
		}
		return ticketIds;
	}
	
	public Ticket getTicket(String code) {
		Ticket ticket = null;
		try {
			ticket = this.getTicketManager().getTicket(code);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTicket");
			throw new RuntimeException("Error searching ticket", t);
		}
		return ticket;
	}
	
	public Map<Integer, InterventionType> getInterventionTypes() {
		return this.getTicketManager().getInterventionTypes();
	}
	
	public Map<Integer, String> getPriorities() {
		return this.getTicketManager().getPriorities();
	}
	
	protected ITicketSearchBean prepareSearchBean() {
		TicketSearchBean searchBean = new TicketSearchBean();
		searchBean.setMessage(this.getMessage());
		searchBean.setAuthor(this.getAuthor());
		Integer status = this.getStatus();
		if (status!=null) {
			searchBean.setStates(new int[] { this.getStatus().intValue() });
		}
		searchBean.setPriority(this.getPriority());
		searchBean.setUserInterventionType(this.getUserInterventionType());
		searchBean.setAssignedInterventionType(this.getAssignedInterventionType());
		Boolean resolved = (this.getResolved()==null) ? null : (new Boolean(this.getResolved().intValue()==1));
		searchBean.setResolved(resolved);
		return searchBean;
	}
	
	public String getMessage() {
		return _message;
	}
	public void setMessage(String message) {
		this._message = message;
	}
	
	public String getAuthor() {
		return this._author;
	}
	public void setAuthor(String author) {
		this._author = author;
	}
	
	public Integer getStatus() {
		return _status;
	}
	public void setStatus(Integer status) {
		this._status = status;
	}
	
	public Integer getPriority() {
		return _priority;
	}
	public void setPriority(Integer priority) {
		this._priority = priority;
	}
	
	public Integer getUserInterventionType() {
		return _userInterventionType;
	}
	public void setUserInterventionType(Integer userInterventionType) {
		this._userInterventionType = userInterventionType;
	}
	
	public Integer getAssignedInterventionType() {
		return _assignedInterventionType;
	}
	public void setAssignedInterventionType(Integer assignedInterventionType) {
		this._assignedInterventionType = assignedInterventionType;
	}
	
	public Integer getResolved() {
		return _resolved;
	}
	public void setResolved(Integer resolved) {
		this._resolved = resolved;
	}
	
	protected ITicketManager getTicketManager() {
		return _ticketManager;
	}
	public void setTicketManager(ITicketManager ticketManager) {
		this._ticketManager = ticketManager;
	}
	
	private String _message;
	private String _author;
	private Integer _status;
	private Integer _priority;
	private Integer _userInterventionType;
	private Integer _assignedInterventionType;
	private Integer _resolved;
	
	private ITicketManager _ticketManager;
	
}