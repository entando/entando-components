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
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;

public abstract class AbstractTicketAction extends BaseAction {
	
	protected abstract boolean isAccessAllowed(Ticket ticket);
	
	public String view() {
		try {
			String code = this.getCode();
			ITicketManager ticketManager = this.getTicketManager();
			Ticket ticket = ticketManager.getTicket(code);
			if (ticket!=null && this.isAccessAllowed(ticket)) {
				this.setTicket(ticket);
				List<TicketOperation> ticketOperations = ticketManager.getTicketOperations(code);
				this.setTicketOperations(ticketOperations);
			} else {
				this.addActionError(this.getText("Errors.ticketOperation.ticketNotFound"));
				return "ticketNotFound";
			}
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "view");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public Map<Integer, InterventionType> getInterventionTypes() {
		return this.getTicketManager().getInterventionTypes();
	}
	public InterventionType getInterventionType(Integer typeCode) {
		return this.getTicketManager().getInterventionType(typeCode);
	}
	public Map<Integer, String> getPriorities() {
		return this.getTicketManager().getPriorities();
	}
	public String getPriority(int priority) {
		return this.getTicketManager().getPriority(priority);
	}
	
	public Ticket getTicket() {
		return this._ticket;
	}
	protected void setTicket(Ticket ticket) {
		this._ticket = ticket;
	}
	
	public List<TicketOperation> getTicketOperations() {
		return this._ticketOperations;
	}
	protected void setTicketOperations(List<TicketOperation> ticketOperations) {
		this._ticketOperations = ticketOperations;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	protected ITicketManager getTicketManager() {
		return _ticketManager;
	}
	public void setTicketManager(ITicketManager ticketManager) {
		this._ticketManager = ticketManager;
	}
	
	private String _code;
	
	private Ticket _ticket;
	private List<TicketOperation> _ticketOperations;
	
	private ITicketManager _ticketManager;
	
}