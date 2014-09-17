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
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.common.AbstractTicketFinderAction;
import com.opensymphony.xwork2.Action;

public class TestTicketFinderAction extends ApsAdminPluginBaseTestCase {

	public void testListUserNotAllowed() throws Throwable {
		String result = this.executeList(SystemConstants.GUEST_USER_NAME);
		assertEquals("apslogin", result);

		result = this.executeList("pageManagerCustomers");
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);
	}

	public void testList() throws Throwable {
		String username = "admin";
		assertEquals(0, this._ticketManager.searchTicketIds(null).size());
		Ticket t1 = this._helper.createTicket(null, new Date(), null, "message1", username, 0, 0, "pageManagerCustomers",
				null, Ticket.STATES_OPENED, null, false);
		this._ticketManager.addTicket(t1);
		Ticket t2 = this._helper.createTicket(null, new Date(), null, "message2", username, 1, 1, "mainEditor",
				null, Ticket.STATES_ASSIGNED, new Date(), false);
		this._ticketManager.addTicket(t2);
		Ticket t3 = this._helper.createTicket(null, new Date(), null, "message3", username, 2, 2, "editorCustomers",
				null, Ticket.STATES_CLOSED, null, false);
		this._ticketManager.addTicket(t3);
		Ticket t4 = this._helper.createTicket(null, new Date(), null, "message4", "pageManagerCustomers", 1,
				0, "editorCustomers", null, Ticket.STATES_ASSIGNABLE, new Date(), true);
		this._ticketManager.addTicket(t4);

		String result = this.executeList(username);
		assertEquals(Action.SUCCESS, result);

		AbstractTicketFinderAction action = (AbstractTicketFinderAction) this.getAction();

		List<String> ticketIds = action.getTicketIds();
		assertEquals(4, ticketIds.size());
		assertTrue(ticketIds.contains(t1.getCode()));
		assertTrue(ticketIds.contains(t2.getCode()));
		assertTrue(ticketIds.contains(t3.getCode()));
		assertTrue(ticketIds.contains(t4.getCode()));
	}

	public void testSearchAdmin() throws Throwable {
		String username = "supervisorCoach";// User with jpwwtAdmin permission
		assertEquals(0, this._ticketManager.searchTicketIds(null).size());
		Ticket t1 = this._helper.createTicket(null, new Date(), "nome1", "cognome1", "codFisc1", "comune1",
				"localita1", "ind1", "indirizzo1", "num1", "telefono1", "email1@email.itte", "message1",
				username, 0, 1, 0, "pageManagerCustomers", "pageManager", Ticket.STATES_OPENED, null, false);
		this._ticketManager.addTicket(t1);
		Ticket t2 = this._helper.createTicket(null, new Date(), null, "message2", username, 1,
				1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
		this._ticketManager.addTicket(t2);
		Ticket t3 = this._helper.createTicket(null, new Date(), null, "message3", null, 2,
				2, "editorCustomers", null, Ticket.STATES_DISPATCHED, null, true);
		this._ticketManager.addTicket(t3);
		Ticket t4 = this._helper.createTicket(null, new Date(), null, null, "pageManagerCustomers", 1,
				0, "editorCustomers", null, Ticket.STATES_CLOSED, new Date(), false);
		this._ticketManager.addTicket(t4);

		Map<String, String> params = this.prepareParams("message2", "", "", "", "", "", "", "");
		String result = this.executeSearch(username, params); // message = 'message2'
		assertEquals(Action.SUCCESS, result);
		List<String> ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t2.getCode() }, ticketIds);

		params.put("message", "ess");
		result = this.executeSearch(username, params); // message = 'ess'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t1.getCode(), t2.getCode(), t3.getCode() }, ticketIds);

		params.put("priority", "0");
		params.remove("message");
		result = this.executeSearch(username, params); // priority = '0'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t1.getCode(), t4.getCode() }, ticketIds);

		params.put("userInterventionType", "0");
		params.put("assignedInterventionType", "1");
		result = this.executeSearch(username, params); // priority = '0', userInterventionType = '0', assignedInterventionType = '1'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t1.getCode() }, ticketIds);

		params.put("userInterventionType", "1");
		params.put("assignedInterventionType", "");
		result = this.executeSearch(username, params); // priority = '0', userInterventionType = '1'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t4.getCode() }, ticketIds);

		params.put("priority", "");
		result = this.executeSearch(username, params); // userInterventionType = '1'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t2.getCode(), t4.getCode() }, ticketIds);

		params.put("status", String.valueOf(Ticket.STATES_DISPATCHED));
		result = this.executeSearch(username, params); // userInterventionType = '1', status = 'DISPATCHED'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { }, ticketIds);

		params.put("userInterventionType", "");
		result = this.executeSearch(username, params); // status = 'DISPATCHED'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t3.getCode() }, ticketIds);

		params.put("status", "");
		params.put("userInterventionType", "");
		params.put("resolved", "1");
		result = this.executeSearch(username, params); // resolved = 'true'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t3.getCode() }, ticketIds);

		params.put("resolved", "0");
		result = this.executeSearch(username, params); // resolved = 'false'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t1.getCode(), t2.getCode(), t4.getCode() }, ticketIds);

		params.put("resolved", "");
		result = this.executeSearch(username, params); // resolved = ''
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t1.getCode(), t2.getCode(), t3.getCode(), t4.getCode() }, ticketIds);
	}

	public void testSearchOperator() throws Throwable {
		String username = "mainEditor";
		assertEquals(0, this._ticketManager.searchTicketIds(null).size());
		Ticket t1 = this._helper.createTicket(null, new Date(), null, "message1", username, 1,
				1, "mainEditor", "supervisor", Ticket.STATES_ASSIGNABLE, new Date(), false);
		this._ticketManager.addTicket(t1);
		Ticket t2 = this._helper.createTicket(null, new Date(), null, "message2", username, 1,
				1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
		this._ticketManager.addTicket(t2);
		Ticket t3 = this._helper.createTicket(null, new Date(), null, "message3", null, 2,
				2, "mainEditor", "editor", Ticket.STATES_WORKING, null, true);
		this._ticketManager.addTicket(t3);
		Ticket t4 = this._helper.createTicket(null, new Date(), null, "message4", "pageManagerCustomers", 1,
				0, "editorCustomers", null, Ticket.STATES_ASSIGNED, new Date(), false);
		this._ticketManager.addTicket(t4);
		Ticket t5 = this._helper.createTicket(null, new Date(), null, "message5", "pageManagerCustomers", 1,
				0, "editorCustomers", "editor", Ticket.STATES_ASSIGNABLE, new Date(), false);
		this._ticketManager.addTicket(t5);

		Map<String, String> params = this.prepareParams("ess", "", "admin", "", "", "", "", "");
		String result = this.executeSearch(username, params); // message = 'ess'
		assertEquals(Action.SUCCESS, result);
		List<String> ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t2.getCode(), t5.getCode() }, ticketIds);

		params.put("status", String.valueOf(Ticket.STATES_ASSIGNABLE));
		result = this.executeSearch(username, params); // message = 'ess'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t5.getCode() }, ticketIds);

		params.put("status", String.valueOf(Ticket.STATES_ASSIGNED));
		result = this.executeSearch(username, params); // message = 'ess'
		assertEquals(Action.SUCCESS, result);
		ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
		this.verifyTicketIds(new String[] { t2.getCode() }, ticketIds);
	}

	private String executeSearch(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket", "search");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}

	private String executeList(String username) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket", "list");
		String result = this.executeAction();
		return result;
	}

}