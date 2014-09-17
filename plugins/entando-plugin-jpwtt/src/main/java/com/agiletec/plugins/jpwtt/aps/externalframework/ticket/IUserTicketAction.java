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
package com.agiletec.plugins.jpwtt.aps.externalframework.ticket;

/**
 * Interface for a class providing, for the current user, functions of creation ad visualization of a Ticket.
 * @version 1.0
 * @author E.Mezzano
 *
 */
public interface IUserTicketAction {
	
	/**
	 * Execute the action of visualization of a Ticket created by the current user.
	 * @return The action result code.
	 */
	public String view();
	
	/**
	 * Execute the action of creation of a Ticket for the current user.
	 * @return The action result code.
	 */
	public String save();
	
}