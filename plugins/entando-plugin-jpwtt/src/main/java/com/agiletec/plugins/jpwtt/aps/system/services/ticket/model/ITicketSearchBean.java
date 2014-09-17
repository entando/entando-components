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

public interface ITicketSearchBean {
	
	public String getMessage();
	
	public String getAuthor();
	
	public String getOperator();
	
	public String[] getWttRoles();
	
	public int[] getStates();
	
	public Integer getPriority();
	
	public Integer getUserInterventionType();
	
	public Integer getAssignedInterventionType();
	
	public Boolean getResolved();
	
}