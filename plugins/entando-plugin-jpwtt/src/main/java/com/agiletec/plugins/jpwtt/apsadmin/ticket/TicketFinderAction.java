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
package com.agiletec.plugins.jpwtt.apsadmin.ticket;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.authorization.authorizator.IApsAuthorityManager;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.plugins.jpwtt.aps.system.services.JpWttSystemConstants;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.ITicketSearchBean;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketSearchBean;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.common.AbstractTicketFinderAction;

public class TicketFinderAction extends AbstractTicketFinderAction {
	
	@Override
	public List<String> getTicketIds() {
		List<String> ticketIds = null;
		try {
			if (this.isAdmin().booleanValue()) {
				return super.getTicketIds();
			} else {
				TicketSearchBean searchBean = (TicketSearchBean) this.prepareSearchBean();
				Integer status = this.getStatus();
				if (status!=null) {
					int statusInt = status.intValue();
					switch (statusInt) {
						case Ticket.STATES_ASSIGNABLE:
							ticketIds = this.searchMyAssignableTicketsId(searchBean);
							break;
						case Ticket.STATES_ASSIGNED:
							ticketIds = this.searchMyAssignedTickets(searchBean);
							break;
					}
				} else {
					ticketIds = this.searchMyAssignableTicketsId(searchBean);
					List<String> assignedTicketIds = this.searchMyAssignedTickets(searchBean);
					if (ticketIds==null || ticketIds.size()==0) {
						ticketIds = assignedTicketIds;
					} else if (assignedTicketIds!=null && assignedTicketIds.size()>0) {
						for (String ticketId : assignedTicketIds) {
							if (!ticketIds.contains(ticketId)) ticketIds.add(ticketId);
						}
					}
				}
				if (null==ticketIds) {
					ticketIds = new ArrayList<String>();
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTicketIds");
			throw new RuntimeException("Error searching tickets", t);
		}
		return ticketIds;
	}
	
	protected List<String> searchMyAssignableTicketsId(TicketSearchBean searchBean) {
		List<String> ticketIds = null;
		try {
			List<IApsAuthority> roles = ((IApsAuthorityManager) this.getRoleManager()).getAuthorizationsByUser(this.getCurrentUser());
			if (null!=roles && roles.size()>0) {
				String[] wttRoles = new String[roles.size()];
				int index = 0;
				for (IApsAuthority role : roles) {
					wttRoles[index++] = role.getAuthority();
				}
				searchBean.setStates(new int[] { Ticket.STATES_ASSIGNABLE });
				searchBean.setOperator(null);
				searchBean.setWttRoles(wttRoles);
				ticketIds = this.getTicketManager().searchTicketIds(searchBean);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchAssignableTicketsId");
		}
		return ticketIds;
	}
	
	protected List<String> searchMyAssignedTickets(TicketSearchBean searchBean) {
		List<String> ticketIds = null;
		try {
			searchBean.setStates(new int[] { Ticket.STATES_ASSIGNED });
			searchBean.setOperator(this.getCurrentUser().getUsername());
			searchBean.setWttRoles(null);
			ticketIds = this.getTicketManager().searchTicketIds(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchAssignableTicketsId");
		}
		return ticketIds;
	}
	
	@Override
	protected ITicketSearchBean prepareSearchBean() {
		TicketSearchBean searchBean = (TicketSearchBean) super.prepareSearchBean();
		searchBean.setOperator(this.getOperator());
		return searchBean;
	}
	
	public Boolean isAdmin() {
		if (null==this._admin) {
			boolean isAdmin = this.getAuthorizationManager().isAuthOnPermission(this.getCurrentUser(), JpWttSystemConstants.WTT_ADMIN_PERMISSION);
			this._admin = new Boolean(isAdmin);
		}
		return _admin;
	}
	
	public String getOperator() {
		return _operator;
	}
	public void setOperator(String operator) {
		this._operator = operator;
	}
	
	protected IRoleManager getRoleManager() {
		return _authorityManager;
	}
	public void setRoleManager(IRoleManager authorityManager) {
		this._authorityManager = authorityManager;
	}
	
	private Boolean _admin;
	
	private String _operator;
	
	private IRoleManager _authorityManager;
	
}