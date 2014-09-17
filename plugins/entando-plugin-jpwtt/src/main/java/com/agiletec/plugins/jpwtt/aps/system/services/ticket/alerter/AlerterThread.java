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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.alerter;

import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;

/**
 * 
 * @version 1.0
 * @author E.Mezzano
 */
public class AlerterThread extends Thread {
	
	public void startAddedTicketThread(AlerterThreadListener threadListener, 
			Ticket ticket, InterventionType interventionType, String threadName) {
		this.startThread(ALERT_ON_ADDTICKET, threadListener, ticket, null, interventionType, threadName);
	}
	
	public void startTicketOperationThread(AlerterThreadListener threadListener, Ticket ticket, 
			TicketOperation operation, InterventionType interventionType, String threadName) {
		this.startThread(ALERT_ON_TICKETOPERATION, threadListener, ticket, operation, interventionType, threadName);
	}
	
	private void startThread(int actionType, AlerterThreadListener threadListener, Ticket ticket, 
			TicketOperation operation, InterventionType interventionType, String threadName) {
		this._actionType = actionType;
		this._threadListener = threadListener;
		this._ticket = ticket;
		this._operation = operation;
		this._interventionType = interventionType;
		this.setName(threadName);
		this.start();
	}
	
	public void run() {
		try {
			if (this._actionType == ALERT_ON_ADDTICKET) {
				this._threadListener.sendAlertOnAddedTicket(this._ticket, this._interventionType);
			} else if (this._actionType == ALERT_ON_TICKETOPERATION) {
				this._threadListener.sendAlertOnTicketOperation(this._ticket, this._operation, this._interventionType);
			}
		} catch (Throwable e) {
			throw new RuntimeException("Errore in ticket alert", e);
		}
	}
	
	private AlerterThreadListener _threadListener;
	private Ticket _ticket;
	private TicketOperation _operation;
	private InterventionType _interventionType;
	private int _actionType;
	
	private static final int ALERT_ON_ADDTICKET = 1;
	private static final int ALERT_ON_TICKETOPERATION = 2;
	
}