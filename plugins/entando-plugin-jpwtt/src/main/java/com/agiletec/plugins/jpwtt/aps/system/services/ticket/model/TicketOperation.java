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

import java.util.Date;

public class TicketOperation {
	
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
	public String getTicketCode() {
		return _ticketCode;
	}
	public void setTicketCode(String ticketCode) {
		this._ticketCode = ticketCode;
	}
	
	public String getOperator() {
		return _operator;
	}
	public void setOperator(String operator) {
		this._operator = operator;
	}
	
	public int getOperationCode() {
		return _operationCode;
	}
	public void setOperationCode(int operationCode) {
		this._operationCode = operationCode;
	}
	
	public String getNote() {
		return _note;
	}
	public void setNote(String note) {
		this._note = note;
	}
	
	public int getInterventionType() {
		return _interventionType;
	}
	public void setInterventionType(int interventionType) {
		this._interventionType = interventionType;
	}
	
	public int getPriority() {
		return _priority;
	}
	public void setPriority(int priority) {
		this._priority = priority;
	}
	
	public String getWttRole() {
		return _wttRole;
	}
	public void setWttRole(String wttRole) {
		this._wttRole = wttRole;
	}
	
	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}
	
	private int _id;
	private String _ticketCode;
	private String _operator;
	private int _operationCode;
	private String _note;
	private int _interventionType;
	private int _priority;
	private String _wttRole;
	private Date _date;
	
	public static final int OPERATIONS_UPDATE = 1; // Admin, Operator
	public static final int OPERATIONS_ANSWER = 5; // Admin, Operator
	public static final int OPERATIONS_SETASSIGNABLE = 10; // Admin
	public static final int OPERATIONS_TAKEINCHARGE = 15; // Admin, Operator
//	public static final int OPERATIONS_DISPATCH = 20; // Operator???
	public static final int OPERATIONS_RELEASE = 25; // Admin, Operator
	public static final int OPERATIONS_CLOSE = 30; // Admin, Operator
	
}