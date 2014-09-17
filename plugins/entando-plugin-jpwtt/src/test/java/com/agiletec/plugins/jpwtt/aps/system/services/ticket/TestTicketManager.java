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

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwtt.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwtt.aps.system.services.JpWttSystemConstants;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.InterventionType;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketSearchBean;

public class TestTicketManager extends ApsPluginBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testAddGetTicket() throws Throwable {
		Ticket ticket = this._helper.createTicket(null, new Date(), "nome", "cognome", "codFisc", "comune", "localita", "indT",
				"indirizzo", "numInd", "telefono", "email@email.itte", "message", "mainEditor", 0, 0, 0, null, null, Ticket.STATES_OPENED, null, false);
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			this._ticketManager.addTicket(ticket);

			assertEquals(1, this._ticketManager.searchTicketIds(null).size());
			Ticket addedTicket = this._ticketManager.getTicket(ticket.getCode());
			this.compareTickets(ticket, addedTicket);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testUpdateTicketWithOperation() throws Throwable {
		Ticket ticket = this._helper.createTicket(null, new Date(), "nome", "cognome", "codFisc", "comune", "localita", "indT",
				"indirizzo", "numInd", "telefono", "email@email.itte", "message", "mainEditor", 0, 0, 0, null, null, Ticket.STATES_OPENED, null, false);
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			this._ticketManager.addTicket(ticket);
			String code = ticket.getCode();
			this.compareTickets(this._ticketManager.getTicket(code), ticket);
			assertEquals(1, this._ticketManager.searchTicketIds(null).size());

			TicketOperation operation1 = this._helper.createTicketOperation(1, code, "editorCustomers", TicketOperation.OPERATIONS_ANSWER, "note1", 0, 0, null, new Date());
			this._ticketManager.updateTicketWithOperation(ticket, operation1);
			this.compareTickets(this._ticketManager.getTicket(code), ticket);

			Ticket modifiedTicket = this._helper.createTicket(code, new Date(), "nome2", "cognome2", "codFisc2", "comune2",
					"localita2", "ind2", "indirizzo2", "num2", "telefono2", "email2@email.itte", "message2", "editorCustomers",
					1, 0, 1, "mainEditor", null, Ticket.STATES_WORKING, new Date(), true);
			TicketOperation operation2 = this._helper.createTicketOperation(2, code, "mainEditor", TicketOperation.OPERATIONS_UPDATE, "note2", 0, 0, null, new Date());
			this._ticketManager.updateTicketWithOperation(modifiedTicket, operation2);
			this.compareTickets(this._ticketManager.getTicket(code), modifiedTicket);

			List<TicketOperation> operations = this._ticketManager.getTicketOperations(code);
			assertEquals(2, operations.size());
			for (TicketOperation operation : operations) {
				if (operation.getId()==operation1.getId()) {
					this.compareTicketOperations(operation1, operation);
				} else if (operation.getId()==operation2.getId()) {
					this.compareTicketOperations(operation2, operation);
				} else {
					fail();
				}
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testGetTickets() throws Throwable {
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), "nome1", "cognome1", "codFisc1", "comune1",
					"localita1", "ind1", "indirizzo1", "num1", "telefono1", "email1@email.itte", "message1",
					"mainEditor", 0, 0, 0, "pageManagerCustomers", null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), null, "message2", "editorCustomers", 1, 1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), null, "message3", "mainEditor", 2, 2, "editorCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t3);
			Ticket t4 = this._helper.createTicket(null, new Date(), null, "message4", "pageManagerCustomers", 1, 0, "editorCustomers", null, Ticket.STATES_WORKING, new Date(), true);
			this._ticketManager.addTicket(t4);

			List<Ticket> tickets = this._ticketManager.getTickets();
			assertEquals(4, tickets.size());
			for (Ticket ticket : tickets) {
				String code = ticket.getCode();
				if (t1.getCode().equals(code)) {
					this.compareTickets(t1, ticket);
				} else if (t2.getCode().equals(code)) {
					this.compareTickets(t2, ticket);
				} else if (t3.getCode().equals(code)) {
					this.compareTickets(t3, ticket);
				} else if (t4.getCode().equals(code)) {
					this.compareTickets(t4, ticket);
				} else {
					fail();
				}
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testSearchTicketIds() throws Throwable {
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket("code1", new Date(), null, "message1", "mainEditor",
					0, 0, "pageManagerCustomers", null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket("code2", new Date(), null, "message2", "editorCustomers",
					1, 1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket("code3", new Date(), null, "message3", "mainEditor",
					2, 2, "editorCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t3);
			Ticket t4 = this._helper.createTicket("code4", new Date(), null, "message4", "pageManagerCustomers",
					1, 0, "editorCustomers", null, Ticket.STATES_WORKING, new Date(), true);
			this._ticketManager.addTicket(t4);

			// subject = 'subject2'
			TicketSearchBean searchBean = this._helper.createSearchBean("message2", null, null, null, null, null, null, null, null);
			List<String> ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t2.getCode() }, ticketIds);

			// Tutti i subject
			searchBean.setMessage("ess");
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t1.getCode(), t2.getCode(), t3.getCode(), t4.getCode() }, ticketIds);

			// author = 'mainEditor'
			searchBean.setAuthor("mainEditor");
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t1.getCode(), t3.getCode() }, ticketIds);

			// author = 'mainEditor', operator = 'editorCustomers'
			searchBean.setOperator("editorCustomers");
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t3.getCode() }, ticketIds);

			// operator = 'editorCustomers', priority = 0, status = RESOLVED
			searchBean.setAuthor(null);
			searchBean.setPriority(0);
			searchBean.setStates(new int[] { Ticket.STATES_WORKING });
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t4.getCode() }, ticketIds);

			// operator = 'editorCustomers', priority = 0
			searchBean.setStates(null);
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t4.getCode() }, ticketIds);

			// priority = 0
			searchBean.setOperator(null);
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t1.getCode(), t4.getCode() }, ticketIds);

			// priority = 0, interventionType = 1
			searchBean.setUserInterventionType(1);
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t4.getCode() }, ticketIds);

			// interventionType = 1
			searchBean.setPriority(null);
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t2.getCode(), t4.getCode() }, ticketIds);

			// interventionType = 1, resolved = true
			searchBean.setResolved(new Boolean(true));
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t4.getCode() }, ticketIds);

			// interventionType = 1, resolved = false
			searchBean.setResolved(new Boolean(false));
			ticketIds = this._ticketManager.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { t2.getCode() }, ticketIds);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

    public void testGetInterventionTypes() throws Throwable {
    	Map<Integer, InterventionType> interventionTypes = this._ticketManager.getInterventionTypes();
    	assertEquals(2, interventionTypes.size());
    	InterventionType hwType = interventionTypes.get(new Integer(1));
    	assertEquals("Hardware", hwType.getDescr());
    	assertEquals(new Integer(1), hwType.getId());
    	InterventionType swType = interventionTypes.get(new Integer(2));
    	assertEquals("Software", swType.getDescr());
    	assertEquals(new Integer(2), swType.getId());
    	swType = this._ticketManager.getInterventionType(new Integer(2));
    	assertEquals("Software", swType.getDescr());
    	assertEquals(new Integer(2), swType.getId());
    }

    public void testGetPriorities() throws Throwable {
    	Map<Integer, String> priorities = this._ticketManager.getPriorities();
    	assertEquals(3, priorities.size());
    	assertEquals("High", priorities.get(new Integer(1)));
    	assertEquals("Medium", priorities.get(new Integer(2)));
    	assertEquals("Low", priorities.get(new Integer(3)));
    }

    private void init() throws Exception {
    	try {
			this._ticketManager = (TicketManager) this.getService(JpWttSystemConstants.TICKET_MANAGER);
		} catch (Exception e) {
			throw e;
		}
	}

	private ITicketManager _ticketManager = null;

}