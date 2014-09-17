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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.authorization.IApsAuthority;
import com.agiletec.aps.system.services.role.IRoleManager;
import com.agiletec.aps.system.services.role.Role;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpwtt.aps.system.services.JpWttSystemConstants;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.ITicketManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.common.AbstractTicketAction;

public class TicketAction extends AbstractTicketAction implements ITicketAction {
	
	@Override
	public String takeInCharge() {
		return this.saveOperation(TicketOperation.OPERATIONS_TAKEINCHARGE);
	}
	
	@Override
	public String update() {
		return this.saveOperation(TicketOperation.OPERATIONS_UPDATE);
	}
	
	@Override
	public String setAssignable() {
		return this.saveOperation(TicketOperation.OPERATIONS_SETASSIGNABLE);
	}
	
	@Override
	public String answer() {
		return this.saveOperation(TicketOperation.OPERATIONS_ANSWER);
	}
	
	@Override
	public String release() {
		return this.saveOperation(TicketOperation.OPERATIONS_RELEASE);
	}
	
	@Override
	public String close() {
		return this.saveOperation(TicketOperation.OPERATIONS_CLOSE);
	}
	
	@Override
	protected boolean isAccessAllowed(Ticket ticket) {
		UserDetails currentUser = this.getCurrentUser();
		boolean allowed = false;
		int status = ticket.getStatus();
		if (this.getAuthorizationManager().isAuthOnPermission(currentUser, JpWttSystemConstants.WTT_ADMIN_PERMISSION) 
				|| (Ticket.STATES_ASSIGNABLE==status && this.userHasRole(ticket.getWttRole()))
				|| Ticket.STATES_ASSIGNED==status && currentUser.getUsername().equals(ticket.getWttOperator())) {
			allowed = true;
		}
		return allowed;
	}
	
	protected String entryOperation(int operationCode) {
		try {
			String code = this.getCode();
			ITicketManager ticketManager = this.getTicketManager();
			Ticket ticket = ticketManager.getTicket(code);
			if (ticket!=null) {
				this.setTicket(ticket);
				if (this.isOperationAllowed(ticket, operationCode)) {
					List<TicketOperation> ticketOperations = ticketManager.getTicketOperations(code);
					this.setTicketOperations(ticketOperations);
				} else {
					this.addActionError(this.getText("Errors.ticketOperation.notAllowed"));
					return "opNotAllowed";
				}
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
	
	protected String saveOperation(int operationCode) {
		try {
			String code = this.getCode();
			ITicketManager ticketManager = this.getTicketManager();
			Ticket ticket = ticketManager.getTicket(code);
			if (ticket!=null) {
				this.setTicket(ticket);
				boolean allowed = this.isOperationAllowed(ticket, operationCode);
				boolean validated = this.validateParameters(ticket, operationCode, allowed);
				if (!allowed) {
					return "opNotAllowed";
				} else if (!validated) {
					return INPUT;
				} else {
					Ticket newTicket = this.createTicketForUpdate(ticket, operationCode);
					TicketOperation operation = this.createTicketOperation(operationCode);
					ticketManager.updateTicketWithOperation(newTicket, operation);
					this.addActionMessage(this.getText("Message.ticketOperation.completed"));
				}
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
	
	public boolean isOperationAllowed(Ticket ticket, int operationCode) {
		boolean allowed = false;
		int status = ticket.getStatus();
		String operator = ticket.getWttOperator();
		UserDetails currentUser = this.getCurrentUser();
		boolean isAdmin = this.getAuthorizationManager().isAuthOnPermission(currentUser, JpWttSystemConstants.WTT_ADMIN_PERMISSION);
		switch (operationCode) {
			case TicketOperation.OPERATIONS_SETASSIGNABLE:
				if (isAdmin && (Ticket.STATES_OPENED==status || Ticket.STATES_WORKING==status)) {
					allowed = true;
				}
				break;
			case TicketOperation.OPERATIONS_TAKEINCHARGE:
				if ((isAdmin && (Ticket.STATES_OPENED==status || Ticket.STATES_WORKING==status)) 
						|| (Ticket.STATES_ASSIGNABLE==status && this.userHasRole(ticket.getWttRole()))) {
					allowed = true;
				}
				break;
			case TicketOperation.OPERATIONS_ANSWER:
			case TicketOperation.OPERATIONS_UPDATE:
			case TicketOperation.OPERATIONS_CLOSE:
				if (Ticket.STATES_ASSIGNED==status && currentUser.getUsername().equals(operator)) {
					allowed = true;
				}
				break;
			case TicketOperation.OPERATIONS_RELEASE:
				if ((isAdmin && (Ticket.STATES_ASSIGNABLE==status || Ticket.STATES_ASSIGNED==status))
						|| (Ticket.STATES_ASSIGNED==status && currentUser.getUsername().equals(operator))) {
					allowed = true;
				}
				break;
		}
		return allowed;
	}
	
	protected boolean userHasRole(String roleName) {
		boolean hasPermission = false;
		if(roleName!=null) {
			IApsAuthority authority = this.getRoleManager().getRole(roleName);
			if (authority!=null) {
				hasPermission = this.getAuthorizationManager().isAuth(this.getCurrentUser(), authority);
			}
		}
		return hasPermission;
	}
	
	public boolean validateParameters(Ticket ticket, int operationCode, boolean allowed) {
		boolean validated = allowed;
		if (!allowed) {
			this.addActionError(this.getText("Errors.ticketOperation.notAllowed"));
		} else {
			switch (operationCode) {
				case TicketOperation.OPERATIONS_ANSWER:
					if (!this.validateAnswerParams(ticket)) {
						validated = false;
					}
					break;
				case TicketOperation.OPERATIONS_UPDATE:
					if (!this.validateUpdateParams()) {
						validated = false;
					}
					break;
				case TicketOperation.OPERATIONS_SETASSIGNABLE:
					if (!this.validateSetAssignableParams(ticket)) {
						validated = false;
					}
					break;
			}
		}
		return validated;
	}
	
	protected Ticket createTicketForUpdate(Ticket ticket, int operationCode) throws Exception {
		Ticket newTicket = (Ticket) ticket.clone();
		switch (operationCode) {
			case TicketOperation.OPERATIONS_TAKEINCHARGE:
				newTicket.setStatus(Ticket.STATES_ASSIGNED);
				newTicket.setWttOperator(this.getCurrentUser().getUsername());
				break;
			case TicketOperation.OPERATIONS_SETASSIGNABLE:
				newTicket.setStatus(Ticket.STATES_ASSIGNABLE);
				newTicket.setWttOperator(null);
				newTicket.setWttRole(this.getRoleName());
				break;
			case TicketOperation.OPERATIONS_UPDATE:
				if (newTicket.getStatus()==Ticket.STATES_OPENED) {
					newTicket.setStatus(Ticket.STATES_WORKING);
				}
				int priority = this.getPriority()!=null ? this.getPriority().intValue() : 0;
				newTicket.setPriority(priority);
				int interventionType = this.getInterventionType()!=null ? this.getInterventionType().intValue() : 0;
				newTicket.setOpInterventionType(interventionType);
				break;
			case TicketOperation.OPERATIONS_CLOSE:
				newTicket.setStatus(Ticket.STATES_CLOSED);
				newTicket.setClosingDate(new Date());
				if (this.getResolved()!=null) {
					newTicket.setResolved(this.getResolved().booleanValue());
				}
				newTicket.setWttOperator(null);
				break;
			case TicketOperation.OPERATIONS_ANSWER:
				if (newTicket.getStatus()==Ticket.STATES_OPENED) {
					newTicket.setStatus(Ticket.STATES_WORKING);
				}
				break;
			case TicketOperation.OPERATIONS_RELEASE:
				newTicket.setStatus(Ticket.STATES_WORKING);
				newTicket.setClosingDate(null);
				newTicket.setResolved(false);
				newTicket.setWttOperator(null);
				break;
		}
		if (this.getPriority()!=null) {
			newTicket.setPriority(this.getPriority().intValue());
		}
		return newTicket;
	}
	
	protected boolean validateUpdateParams() {
		boolean allowed = true;
		ITicketManager ticketManager = this.getTicketManager();
		Integer interventionType = this.getInterventionType();
		if (null!=interventionType && 0!=interventionType.intValue() && null==ticketManager.getInterventionType(interventionType)) {
			this.addFieldError("interventionType", this.getText("Errors.interventionType.notValid"));
			allowed = false;
		}
		Integer priority = this.getPriority();
		if (null!=priority && 0!=priority.intValue() && null==ticketManager.getPriorities().get(priority)) {
			this.addFieldError("priority", this.getText("Errors.priority.notValid"));
			allowed = false;
		}
		return allowed;
	}
	
	protected boolean validateAnswerParams(Ticket ticket) {
		boolean allowed = false;
		String email = ticket.getEmail();
		String message = this.getMessage();
		if (null==email || email.length()==0) {
			this.addActionError(this.getText("Errors.ticketAnswer.mailNotFound"));
		} else if (message==null || message.length()==0) {
			this.addFieldError("message", this.getText("required", new String[] { "message" }));// TODO verificare parametro
		} else {
			allowed = true;
		}
		return allowed;
	}
	
	protected boolean validateSetAssignableParams(Ticket ticket) {
		boolean allowed = false;
		String roleName = this.getRoleName();
		if (null!=roleName) {
			Role role = this.getRoleManager().getRole(roleName);
			if (null!=role && role.hasPermission(JpWttSystemConstants.WTT_OPERATOR_PERMISSION)) {
				allowed = true;
			}
		}
		if (!allowed) {
			this.addFieldError("roleName", this.getText("Errors.ticketSetAssiglable.roleNotFound"));
		}
		return allowed;
	}
	
	protected TicketOperation createTicketOperation(int operationCode) {
		TicketOperation operation = new TicketOperation();
		operation.setTicketCode(this.getCode());
		operation.setOperator(this.getCurrentUser().getUsername());
		operation.setOperationCode(operationCode);
		operation.setDate(new Date());
		if (TicketOperation.OPERATIONS_ANSWER==operationCode) {
			operation.setNote(this.getMessage());
		} else if (TicketOperation.OPERATIONS_UPDATE==operationCode) {
			int priority = this.getPriority()!=null ? this.getPriority().intValue() : 0;
			operation.setPriority(priority);
			int interventionType = this.getInterventionType()!=null ? this.getInterventionType().intValue() : 0;
			operation.setInterventionType(interventionType);
			operation.setNote(this.getNote());
		} else if (TicketOperation.OPERATIONS_SETASSIGNABLE==operationCode) {
			operation.setWttRole(this.getRoleName());
		}
		return operation;
	}
	
	public List<Role> getRoles() {
		List<Role> roles = this.getRoleManager().getRolesWithPermission(JpWttSystemConstants.WTT_OPERATOR_PERMISSION);
		return roles;
	}
	
	public Role getRole(String roleName) {
		return this.getRoleManager().getRole(roleName);
	}
	
	public Map<Integer, String> getOperations() {
		if (null==this._operations) {
			this._operations = new HashMap<Integer, String>();
			this._operations.put(TicketOperation.OPERATIONS_SETASSIGNABLE, "entryAssign");
			this._operations.put(TicketOperation.OPERATIONS_TAKEINCHARGE, "takeInCharge");
			this._operations.put(TicketOperation.OPERATIONS_UPDATE, "update");
			this._operations.put(TicketOperation.OPERATIONS_ANSWER, "entryAnswer");
			this._operations.put(TicketOperation.OPERATIONS_RELEASE, "reopen");
		}
		return this._operations;
	}
	
	public Integer getInterventionType() {
		return _interventionType;
	}
	public void setInterventionType(Integer interventionType) {
		this._interventionType = interventionType;
	}
	
	public Integer getPriority() {
		return _priority;
	}
	public void setPriority(Integer priority) {
		this._priority = priority;
	}
	
	public String getNote() {
		return _note;
	}
	public void setNote(String note) {
		this._note = note;
	}
	
	public String getMessage() {
		return _message;
	}
	public void setMessage(String message) {
		this._message = message;
	}
	
	public Boolean getResolved() {
		return _resolved;
	}
	public void setResolved(Boolean resolved) {
		this._resolved = resolved;
	}
	
	public String getRoleName() {
		return _roleName;
	}
	public void setRoleName(String roleName) {
		this._roleName = roleName;
	}
	
	public IRoleManager getRoleManager() {
		return _roleManager;
	}
	public void setRoleManager(IRoleManager authorityManager) {
		this._roleManager = authorityManager;
	}
	
	private Integer _interventionType;
	private Integer _priority;
	private String _note;
	private String _message;
	private Boolean _resolved;
	private String _roleName;
	
	private Map<Integer, String> _operations;
	
	private IRoleManager _roleManager;
	
}