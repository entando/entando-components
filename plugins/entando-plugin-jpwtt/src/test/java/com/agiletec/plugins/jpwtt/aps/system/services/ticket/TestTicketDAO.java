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

import javax.sql.DataSource;

import com.agiletec.plugins.jpwtt.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketSearchBean;

public class TestTicketDAO extends ApsPluginBaseTestCase {

	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testAddLoadTicket() throws Throwable {
		String code = "code";
		Ticket ticket = this._helper.createTicket(code, new Date(), "nome", "cognome", "codFisc", "comune", "localita", "indT",
				"indirizzo", "numInd", "telefono", "email@email.itte", "message", "mainEditor", 0, 0, 0, null, null, Ticket.STATES_OPENED, null, false);
		try {
			assertEquals(0, this._ticketDAO.searchTicketIds(null).size());
			this._ticketDAO.addTicket(ticket);
			assertEquals(1, this._ticketDAO.searchTicketIds(null).size());
			Ticket addedTicket = this._ticketDAO.loadTicket(code);
			this.compareTickets(ticket, addedTicket);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testAddLoadTicketOperation() throws Throwable {
		String code = "code";
		Ticket ticket = this._helper.createTicket(code, new Date(), "nome", "cognome", "codFisc", "comune",
				"localita", "indT", "indirizzo", "numInd", "telefono", "email@email.itte", "message",
				"mainEditor", 0, 1, 0, "admin", "pageManager", Ticket.STATES_OPENED, null, false);
		try {
			assertEquals(0, this._ticketDAO.searchTicketIds(null).size());
			this._ticketDAO.addTicket(ticket);
			assertEquals(1, this._ticketDAO.searchTicketIds(null).size());

			Ticket modifiedTicket = this._helper.createTicket(code, new Date(), "nome2", "cognome2", "codFisc2", "comune2",
					"localita2", "ind2", "indirizzo2", "num2", "telefono2", "email2@email.itte", "message2", "editorCustomers",
					1, 0, 1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), true);
			TicketOperation operation1 = this._helper.createTicketOperation(1, code, "editorCustomers", TicketOperation.OPERATIONS_ANSWER, "note1", 0, 0, "pageManager", new Date());
			this._ticketDAO.updateTicketWithOperation(modifiedTicket, operation1);
			Ticket updatedTicket = this._ticketDAO.loadTicket(code);
			this.compareTickets(modifiedTicket, updatedTicket);

			TicketOperation operation2 = this._helper.createTicketOperation(2, code, "mainEditor", TicketOperation.OPERATIONS_UPDATE, "note2", 0, 0, null, new Date());
			this._ticketDAO.updateTicketWithOperation(ticket, operation2);
			updatedTicket = this._ticketDAO.loadTicket(code);
			this.compareTickets(ticket, updatedTicket);

			List<TicketOperation> operations = this._ticketDAO.loadTicketOperations(code);
			assertEquals(2, operations.size());
			for (TicketOperation operation : operations) {
				switch (operation.getId()) {
					case 1:
						this.compareTicketOperations(operation1, operation);
						break;
					case 2:
						this.compareTicketOperations(operation2, operation);
						break;
					default:
						fail();
				}
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testLoadTickets() throws Throwable {
		try {
			assertEquals(0, this._ticketDAO.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket("code1", new Date(), "nome1", "cognome1", "codFisc1", "comune1",
					"localita1", "ind1", "indirizzo1", "num1", "telefono1", "email1@email.itte", "message1",
					"mainEditor", 0, 1, 0, "pageManagerCustomers", "pageManager", Ticket.STATES_OPENED, null, false);
			this._ticketDAO.addTicket(t1);
			Ticket t2 = this._helper.createTicket("code2", new Date(), null, "message2", "editorCustomers",
					1, 1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
			this._ticketDAO.addTicket(t2);
			Ticket t3 = this._helper.createTicket("code3", new Date(), null, "message3", "mainEditor",
					2, 2, "editorCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketDAO.addTicket(t3);
			Ticket t4 = this._helper.createTicket("code4", new Date(), null, "message4", "pageManagerCustomers",
					1, 0, "editorCustomers", null, Ticket.STATES_ASSIGNABLE, new Date(), true);
			this._ticketDAO.addTicket(t4);

			List<Ticket> tickets = this._ticketDAO.loadTickets();
			assertEquals(4, tickets.size());
			for (Ticket ticket : tickets) {
				String code = ticket.getCode();
				if ("code1".equals(code)) {
					this.compareTickets(t1, ticket);
				} else if ("code2".equals(code)) {
					this.compareTickets(t2, ticket);
				} else if ("code3".equals(code)) {
					this.compareTickets(t3, ticket);
				} else if ("code4".equals(code)) {
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
			assertEquals(0, this._ticketDAO.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket("code1", new Date(), "nome1", "cognome1", "codFisc1", "comune1",
					"localita1", "ind1", "indirizzo1", "num1", "telefono1", "email1@email.itte", "message1",
					"mainEditor", 0, 1, 0, "pageManagerCustomers", "pageManager", Ticket.STATES_OPENED, null, false);
			this._ticketDAO.addTicket(t1);
			Ticket t2 = this._helper.createTicket("code2", new Date(), null, "message2", "editorCustomers",
					1, 1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
			this._ticketDAO.addTicket(t2);
			Ticket t3 = this._helper.createTicket("code3", new Date(), null, "message3", "mainEditor",
					2, 2, "editorCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketDAO.addTicket(t3);
			Ticket t4 = this._helper.createTicket("code4", new Date(), null, "message4", "pageManagerCustomers",
					1, 0, "editorCustomers", null, Ticket.STATES_WORKING, new Date(), true);
			this._ticketDAO.addTicket(t4);

			// message = 'message2'
			TicketSearchBean searchBean = this._helper.createSearchBean("message2", null, null, null, null, null, null, null, null);
			List<String> ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code2" }, ticketIds);

			// Tutti i message
			searchBean.setMessage("ess");
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code1", "code2", "code3", "code4" }, ticketIds);

			// author = 'mainEditor'
			searchBean.setAuthor("mainEditor");
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code1", "code3" }, ticketIds);

			// author = 'mainEditor', operator = 'editorCustomers'
			searchBean.setOperator("editorCustomers");
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code3" }, ticketIds);

			// operator = 'editorCustomers', priority = 0, status = { DISPATCHED, OPENED }
			searchBean.setAuthor(null);
			searchBean.setPriority(0);
			searchBean.setStates(new int[] { Ticket.STATES_WORKING, Ticket.STATES_OPENED });
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code4" }, ticketIds);

			// operator = 'editorCustomers', priority = 0
			searchBean.setStates(null);
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code4" }, ticketIds);

			// priority = 0, wttRoles = { }
			searchBean.setOperator(null);
			searchBean.setWttRoles(new String[] { "pageManager" });
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code1" }, ticketIds);

			// priority = 0
			searchBean.setOperator(null);
			searchBean.setWttRoles(new String[] { });
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code1", "code4" }, ticketIds);

			// priority = 0, userInterventionType = 0, assignedInterventionType = 1
			searchBean.setUserInterventionType(0);
			searchBean.setAssignedInterventionType(1);
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code1" }, ticketIds);

			// userInterventionType = 1
			searchBean.setPriority(null);
			searchBean.setUserInterventionType(1);
			searchBean.setAssignedInterventionType(null);
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code2", "code4" }, ticketIds);

			// userInterventionType = 1, resolved = true
			searchBean.setResolved(new Boolean(true));
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code4" }, ticketIds);

			// userInterventionType = 1, resolved = false
			searchBean.setResolved(new Boolean(false));
			ticketIds = this._ticketDAO.searchTicketIds(searchBean);
			this.verifyTicketIds(new String[] { "code2" }, ticketIds);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

    private void init() throws Exception {
    	try {
			TicketDAO ticketDAO = new TicketDAO();
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
			ticketDAO.setDataSource(dataSource);
			this._ticketDAO = ticketDAO;
		} catch (Exception e) {
			throw e;
		}
	}

	private ITicketDAO _ticketDAO = null;

}