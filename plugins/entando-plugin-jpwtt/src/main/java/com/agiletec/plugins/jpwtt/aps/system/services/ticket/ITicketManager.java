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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.ITicketSearchBean;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;

public interface ITicketManager {
	
	public List<Ticket> getTickets() throws ApsSystemException;
	
	public List<String> searchTicketIds(ITicketSearchBean searchBean) throws ApsSystemException;
	
	public Ticket getTicket(String code) throws ApsSystemException;
	
	public List<TicketOperation> getTicketOperations(String code) throws ApsSystemException;
	
	public void addTicket(Ticket ticket) throws ApsSystemException;
	
	public void updateTicketWithOperation(Ticket ticket, TicketOperation operation) throws ApsSystemException;
	
	public Map<Integer, String> getPriorities();
	
	public String getPriority(int priority);
	
	public Map<Integer, InterventionType> getInterventionTypes();
	
	public InterventionType getInterventionType(Integer typeCode);
	
}