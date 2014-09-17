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

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.apsadmin.ApsAdminPluginBaseTestCase;
import com.opensymphony.xwork2.Action;

public class TestUserTicketAction extends ApsAdminPluginBaseTestCase {

	public void testView() throws Throwable {
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), null, "message1", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);

			String result = this.executeView("pageManagerCustomers", t1.getCode());
			assertEquals("ticketNotFound", result);

			result = this.executeView("mainEditor", t1.getCode());
			assertEquals(Action.SUCCESS, result);

			UserTicketAction action = (UserTicketAction) this.getAction();
			Ticket ticket = action.getTicket();
			this.compareTickets(t1, ticket);
			assertEquals(0, action.getTicketOperations().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testNew() throws Throwable {
		String result = this.executeNew(SystemConstants.GUEST_USER_NAME);
		assertEquals(Action.SUCCESS, result);

		result = this.executeNew("mainEditor");
		assertEquals(Action.SUCCESS, result);
	}

	public void testSaveUnsuccessful() throws Throwable {
		String username = SystemConstants.GUEST_USER_NAME;
		assertEquals(0, this._ticketManager.searchTicketIds(null).size());
		Map<String, String> params = new HashMap<String, String>();

		String result = this.executeSave(username, params);
		assertEquals(Action.INPUT, result);
		Map fieldErrors = this.getAction().getFieldErrors();
		assertEquals(6, fieldErrors.size());
		assertEquals(1, ((List) fieldErrors.get("nome")).size());
		assertEquals(1, ((List) fieldErrors.get("cognome")).size());
		assertEquals(1, ((List) fieldErrors.get("codFisc")).size());
		assertEquals(1, ((List) fieldErrors.get("comune")).size());
		assertEquals(1, ((List) fieldErrors.get("indirizzo")).size());
		assertEquals(1, ((List) fieldErrors.get("trattamentoDati")).size());
		assertEquals(0, this.getAction().getActionErrors().size());

		params.put("nome", "nome");
		params.put("cognome", "cognome");
		params.put("codFisc", "codFisc");
		params.put("comune", "comune");
		params.put("indirizzo", "indirizzo");
		params.put("trattamentoDati", "false");
		params.put("email", "email");
		params.put("interventionType", "10");
		result = this.executeSave(username, params);
		assertEquals(Action.INPUT, result);
		fieldErrors = this.getAction().getFieldErrors();
		assertEquals(4, fieldErrors.size());
		assertEquals(1, ((List) fieldErrors.get("interventionType")).size());
		assertEquals(1, ((List) fieldErrors.get("trattamentoDati")).size());
		assertEquals(1, ((List) fieldErrors.get("email")).size());
		assertEquals(1, ((List) fieldErrors.get("codFisc")).size());
		Collection actionErrors = this.getAction().getActionErrors();
		assertEquals(1, actionErrors.size());
		assertEquals("errors.email.wrongConfirm", actionErrors.toArray()[0]);
	}

	public void testSaveSuccessful() throws Throwable {
		String username = "mainEditor";
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Map<String, String> params = new HashMap<String, String>();

			params.put("nome", "nome");
			params.put("cognome", "cognome");
			params.put("codFisc", "codFisc890123456");
			params.put("comune", "comune");
			params.put("indirizzo", "indirizzo");
			params.put("trattamentoDati", "true");
			params.put("email", "email@email.it");
			params.put("emailConfirm", "email@email.it");
			params.put("interventionType", "1");
			String result = this.executeSave(username, params);
			assertEquals(Action.SUCCESS, result);

			List<String> ticketIds = this._ticketManager.searchTicketIds(null);
			assertEquals(1, ticketIds.size());
			Ticket ticket = this._ticketManager.getTicket(ticketIds.get(0));
			assertEquals("nome", ticket.getNome());
			assertEquals("cognome", ticket.getCognome());
			assertEquals("codFisc890123456", ticket.getCodFisc());
			assertEquals("comune", ticket.getComune());
			assertEquals("indirizzo", ticket.getIndirizzo());
			assertEquals("email@email.it", ticket.getEmail());
			assertEquals(1, ticket.getUserInterventionType());
			assertNull(ticket.getMessage());
			assertNull(ticket.getTelefono());
			assertNull(ticket.getNumeroIndirizzo());
			assertNull(ticket.getTipoIndirizzo());
			assertNull(ticket.getLocalita());

			assertEquals(Ticket.STATES_OPENED, ticket.getStatus());
			assertEquals(username, ticket.getAuthor());
			assertNotNull(ticket.getCreationDate());
			assertNull(ticket.getClosingDate());
			assertNull(ticket.getWttOperator());
			assertFalse(ticket.isResolved());
			assertEquals(0, ticket.getPriority());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	private String executeView(String username, String code) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket/User", "view");
		this.addParameter("code", code);
		String result = this.executeAction();
		return result;
	}

	private String executeNew(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket/User", "new");
		String result = this.executeAction();
		return result;
	}

	private String executeSave(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket/User", "save");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}

	protected void compareTickets(Ticket t1, Ticket t2) {
		assertEquals(t1.getCode(), t2.getCode());
		assertEquals(t1.getCreationDate(), t2.getCreationDate());
		assertEquals(t1.getMessage(), t2.getMessage());
		assertEquals(t1.getAuthor(), t2.getAuthor());
		assertEquals(t1.getUserInterventionType(), t2.getUserInterventionType());
		assertEquals(t1.getPriority(), t2.getPriority());
		assertEquals(t1.getWttOperator(), t2.getWttOperator());
		assertEquals(t1.getStatus(), t2.getStatus());
		assertEquals(t1.getClosingDate(), t2.getClosingDate());
		assertEquals(t1.isResolved(), t2.isResolved());
	}

}