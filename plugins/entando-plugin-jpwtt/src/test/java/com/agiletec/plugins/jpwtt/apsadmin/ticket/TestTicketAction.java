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

import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.common.AbstractTicketAction;
import com.opensymphony.xwork2.Action;

public class TestTicketAction extends ApsAdminPluginBaseTestCase {

	public void testView() throws Throwable {
		assertEquals(0, this._ticketManager.searchTicketIds(null).size());
		Ticket t1 = this._helper.createTicket(null, new Date(), null, "message1", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNED, null, false);
		this._ticketManager.addTicket(t1);
		String ticketCode = t1.getCode();
		TicketOperation op1 = this._helper.createTicketOperation(0, ticketCode, "pageManagerCustomers", TicketOperation.OPERATIONS_TAKEINCHARGE, "notw", 0, 0, null, new Date());
		this._ticketManager.updateTicketWithOperation(t1, op1);

		String result = this.executeAction("pageManagerCoach", "view", ticketCode);
		assertEquals(BaseAction.USER_NOT_ALLOWED, result);

		result = this.executeAction("admin", "view", ticketCode);
		assertEquals(Action.SUCCESS, result);
		Ticket ticket = ((AbstractTicketAction) this.getAction()).getTicket();
		this.compareTickets(t1, ticket);
		List<TicketOperation> operations = ((AbstractTicketAction) this.getAction()).getTicketOperations();
		assertEquals(1, operations.size());
	}

	public void testTakeInCharge() throws Throwable {
		String actionCode = "takeInCharge";
		assertEquals(0, this._ticketManager.searchTicketIds(null).size());
		Ticket t1 = this._helper.createTicket(null, new Date(), null, "message1", null, 0, 0, null, null, Ticket.STATES_OPENED, null, false);
		this._ticketManager.addTicket(t1);
		Ticket t2 = this._helper.createTicket(null, new Date(), null, "message2", null, 0, 0, "pageManagerCustomers", Permission.SUPERVISOR, Ticket.STATES_ASSIGNABLE, null, false);
		this._ticketManager.addTicket(t2);
		Ticket t3 = this._helper.createTicket(null, new Date(), null, "message3", null, 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNED, null, false);
		this._ticketManager.addTicket(t3);
		Ticket t4 = this._helper.createTicket(null, new Date(), null, "message4", null, 0, 0, "pageManagerCustomers", "editor", Ticket.STATES_ASSIGNABLE, null, false);
		this._ticketManager.addTicket(t4);
		Ticket t5 = this._helper.createTicket(null, new Date(), null, "message5", null, 0, 0, "pageManagerCustomers", null, Ticket.STATES_CLOSED, null, false);
		this._ticketManager.addTicket(t5);

		// t1 OPENED
		Ticket current = t1;
		String result = this.executeAction("admin", actionCode, current.getCode());
		assertEquals(Action.SUCCESS, result);
		Ticket ticket = this._ticketManager.getTicket(current.getCode());
		assertEquals(Ticket.STATES_ASSIGNED, ticket.getStatus());
		assertEquals("admin", ticket.getWttOperator());
		List<TicketOperation> operations = this._ticketManager.getTicketOperations(current.getCode());
		assertEquals(1, operations.size());
		TicketOperation operation = operations.get(0);
		assertEquals(TicketOperation.OPERATIONS_TAKEINCHARGE, operation.getOperationCode());
		assertEquals("admin", operation.getOperator());

		// t2 ASSIGNABLE
		current = t2;
		result = this.executeAction("admin", actionCode, current.getCode());
		assertEquals("opNotAllowed", result);
		ticket = this._ticketManager.getTicket(current.getCode());
		this.compareTickets(current, ticket);
		assertEquals(0, this._ticketManager.getTicketOperations(current.getCode()).size());

		// t3 ASSIGNED
		current = t3;
		result = this.executeAction("admin", actionCode, current.getCode());
		assertEquals("opNotAllowed", result);
		ticket = this._ticketManager.getTicket(current.getCode());
		this.compareTickets(current, ticket);
		assertEquals(0, this._ticketManager.getTicketOperations(current.getCode()).size());

		// t4 ASSIGNABLE
		current = t4;
		result = this.executeAction("mainEditor", actionCode, current.getCode());
		assertEquals(Action.SUCCESS, result);
		ticket = this._ticketManager.getTicket(current.getCode());
		assertEquals(Ticket.STATES_ASSIGNED, ticket.getStatus());
		assertEquals("mainEditor", ticket.getWttOperator());
		operations = this._ticketManager.getTicketOperations(current.getCode());
		assertEquals(1, operations.size());
		operation = operations.get(0);
		assertEquals(TicketOperation.OPERATIONS_TAKEINCHARGE, operation.getOperationCode());
		assertEquals("mainEditor", operation.getOperator());

		// t5 CLOSED
		current = t5;
		result = this.executeAction("admin", actionCode, current.getCode());
		assertEquals("opNotAllowed", result);
		ticket = this._ticketManager.getTicket(current.getCode());
		this.compareTickets(current, ticket);
		assertEquals(0, this._ticketManager.getTicketOperations(current.getCode()).size());
	}

	public void testAssign() throws Throwable {
		String actionCode = "assign";
		Map<String, String> params = new HashMap<String, String>();
		params.put("note", "note");
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), "email1@inesistente.itte", "message1", "mainEditor", 0, 0, null, null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), "email2@inesistente.itte", "message2", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNABLE, null, false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), "email3@inesistente.itte", "message3", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t3);
			Ticket t4 = this._helper.createTicket(null, new Date(), "email4@inesistente.itte", "message4", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t4);

			String ticketCode = t1.getCode();
			String result = this.executeAction("mainEditor", actionCode, ticketCode, params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());
			assertEquals(0, this.getAction().getFieldErrors().size());

			result = this.executeAction("admin", actionCode, ticketCode, params);
			assertEquals("input", result);
			Map<String, List<String>> fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertEquals(1, fieldErrors.get("roleName").size());
			assertEquals(0, this.getAction().getActionErrors().size());

			params.put("roleName", "editor");
			result = this.executeAction("admin", actionCode, ticketCode, params);
			assertEquals(Action.SUCCESS, result);
			assertEquals(Ticket.STATES_ASSIGNABLE, this._ticketManager.getTicket(ticketCode).getStatus());
			List<TicketOperation> operations = this._ticketManager.getTicketOperations(ticketCode);
			assertEquals(1, operations.size());
			TicketOperation operation = operations.get(0);
			assertEquals(TicketOperation.OPERATIONS_SETASSIGNABLE, operation.getOperationCode());
			assertEquals(null, operation.getNote());

			result = this.executeAction("admin", actionCode, t2.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			result = this.executeAction("admin", actionCode, t3.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			result = this.executeAction("admin", actionCode, t4.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testAnswer() throws Throwable {
		String actionCode = "answer";
		Map<String, String> params = new HashMap<String, String>();
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), "email1@inesistente.itte", "message1", "mainEditor", 0, 0, null, null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), "email2@inesistente.itte", "message2", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNABLE, null, false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), "email3@inesistente.itte", "message3", "mainEditor", 0, 0, "admin", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t3);
			Ticket t4 = this._helper.createTicket(null, new Date(), "", "message4", "mainEditor", 0, 0, "admin", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t4);
			Ticket t5 = this._helper.createTicket(null, new Date(), "email5@inesistente.itte", "message5", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t5);
			Ticket t6 = this._helper.createTicket(null, new Date(), "email6@inesistente.itte", "message6", "mainEditor", 0, 0, "mainEditor", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t6);

			Ticket current = t3;
			String result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals(Action.INPUT, result);
			assertEquals(0, this.getAction().getActionErrors().size());
			Map fieldErrors = this.getAction().getFieldErrors();
			assertEquals(1, fieldErrors.size());
			assertNotNull(fieldErrors.get("message"));

			params.put("message", "note");
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals(Action.SUCCESS, result);
			Ticket ticket = this._ticketManager.getTicket(current.getCode());
			assertEquals(Ticket.STATES_ASSIGNED, ticket.getStatus());
			List<TicketOperation> operations = this._ticketManager.getTicketOperations(current.getCode());
			assertEquals(1, operations.size());
			TicketOperation operation = operations.get(0);
			assertEquals(TicketOperation.OPERATIONS_ANSWER, operation.getOperationCode());
			assertEquals("note", operation.getNote());

			current = t1;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t2;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t5;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t6;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t6;
			result = this.executeAction("mainEditor", actionCode, current.getCode(), params);
			assertEquals(Action.SUCCESS, result);

			current = t4;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals(Action.INPUT, result);
			assertEquals(1, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testUpdate() throws Throwable {
		String actionCode = "update";
		Map<String, String> params = new HashMap<String, String>();
		params.put("interventionType", "10");
		params.put("priority", "10");
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), "email1@inesistente.itte", "message1", "mainEditor", 0, 0, null, null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), "email2@inesistente.itte", "message2", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNABLE, null, false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), "email3@inesistente.itte", "message3", "mainEditor", 0, 0, "admin", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t3);
			Ticket t4 = this._helper.createTicket(null, new Date(), "email4@inesistente.itte", "message4", "mainEditor", 0, 0, "mainEditor", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t4);
			Ticket t5 = this._helper.createTicket(null, new Date(), "email5@inesistente.itte", "message5", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t5);

			Ticket current = t3;
			String result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals(Action.INPUT, result);
			assertEquals(0, this.getAction().getActionErrors().size());
			Map fieldErrors = this.getAction().getFieldErrors();
			assertEquals(2, fieldErrors.size());
			assertNotNull(fieldErrors.get("interventionType"));
			assertNotNull(fieldErrors.get("priority"));

			params.put("interventionType", "1");
			params.put("priority", "1");
			params.put("note", "note");
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals(Action.SUCCESS, result);
			Ticket ticket = this._ticketManager.getTicket(current.getCode());
			assertEquals(Ticket.STATES_ASSIGNED, ticket.getStatus());
			assertEquals(1, ticket.getOpInterventionType());
			assertEquals(0, ticket.getUserInterventionType());
			assertEquals(1, ticket.getPriority());
			List<TicketOperation> operations = this._ticketManager.getTicketOperations(current.getCode());
			assertEquals(1, operations.size());
			TicketOperation operation = operations.get(0);
			assertEquals(TicketOperation.OPERATIONS_UPDATE, operation.getOperationCode());
			assertEquals("note", operation.getNote());

			current = t1;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t2;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t4;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			result = this.executeAction("mainEditor", actionCode, current.getCode(), params);
			assertEquals(Action.SUCCESS, result);

			current = t5;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testClose() throws Throwable {
		String actionCode = "close";
		Map<String, String> params = new HashMap<String, String>();
		params.put("interventionType", "10");
		params.put("priority", "10");
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), "email1@inesistente.itte", "message1", "mainEditor", 0, 0, null, null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), "email2@inesistente.itte", "message2", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNABLE, null, false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), "email3@inesistente.itte", "message3", "mainEditor", 0, 0, "admin", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t3);
			Ticket t4 = this._helper.createTicket(null, new Date(), "email4@inesistente.itte", "message4", "mainEditor", 0, 0, "mainEditor", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t4);
			Ticket t5 = this._helper.createTicket(null, new Date(), "email5@inesistente.itte", "message5", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t5);

			Ticket current = t1;
			String result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t2;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			current = t3;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals(Action.SUCCESS, result);
			Ticket ticket = this._ticketManager.getTicket(current.getCode());
			assertEquals(Ticket.STATES_CLOSED, ticket.getStatus());
			assertEquals(false, ticket.isResolved());
			List<TicketOperation> operations = this._ticketManager.getTicketOperations(current.getCode());
			assertEquals(1, operations.size());
			TicketOperation operation = operations.get(0);
			assertEquals(TicketOperation.OPERATIONS_CLOSE, operation.getOperationCode());

			current = t4;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			params.put("resolved", "true");
			result = this.executeAction("mainEditor", actionCode, current.getCode(), params);
			assertEquals(Action.SUCCESS, result);
			ticket = this._ticketManager.getTicket(current.getCode());
			assertEquals(Ticket.STATES_CLOSED, ticket.getStatus());
			assertEquals(true, ticket.isResolved());
			operations = this._ticketManager.getTicketOperations(current.getCode());
			assertEquals(1, operations.size());
			operation = operations.get(0);
			assertEquals(TicketOperation.OPERATIONS_CLOSE, operation.getOperationCode());

			current = t5;
			result = this.executeAction("admin", actionCode, current.getCode(), params);
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testRelease() throws Throwable {
		String actionCode = "release";
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), "subject1", "message1", "mainEditor", 0, 0, null, null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), "subject2", "message2", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_ASSIGNABLE, null, false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), "subject3", "message3", "mainEditor", 0, 0, "mainEditor", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t3);
			Ticket t5 = this._helper.createTicket(null, new Date(), "subject5", "message5", "mainEditor", 0, 0, "pageManagerCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t5);
			Ticket t4 = this._helper.createTicket(null, new Date(), "email4@inesistente.itte", "message4", "mainEditor", 0, 0, "mainEditor", null, Ticket.STATES_ASSIGNED, null, false);
			this._ticketManager.addTicket(t4);

			String ticketCode = t2.getCode();
			String result = this.executeAction("admin", actionCode, ticketCode);
			assertEquals(Action.SUCCESS, result);
			assertEquals(Ticket.STATES_WORKING, this._ticketManager.getTicket(ticketCode).getStatus());
			List<TicketOperation> operations = this._ticketManager.getTicketOperations(ticketCode);
			assertEquals(1, operations.size());
			assertEquals(TicketOperation.OPERATIONS_RELEASE, operations.get(0).getOperationCode());

			ticketCode = t3.getCode();
			result = this.executeAction("admin", actionCode, ticketCode);
			assertEquals(Action.SUCCESS, result);
			assertEquals(Ticket.STATES_WORKING, this._ticketManager.getTicket(ticketCode).getStatus());
			operations = this._ticketManager.getTicketOperations(ticketCode);
			assertEquals(1, operations.size());
			assertEquals(TicketOperation.OPERATIONS_RELEASE, operations.get(0).getOperationCode());

			ticketCode = t4.getCode();
			result = this.executeAction("mainEditor", actionCode, ticketCode);
			assertEquals(Action.SUCCESS, result);
			assertEquals(Ticket.STATES_WORKING, this._ticketManager.getTicket(ticketCode).getStatus());
			operations = this._ticketManager.getTicketOperations(ticketCode);
			assertEquals(1, operations.size());
			assertEquals(TicketOperation.OPERATIONS_RELEASE, operations.get(0).getOperationCode());

			result = this.executeAction("admin", actionCode, t1.getCode());
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			result = this.executeAction("admin", actionCode, t5.getCode());
			assertEquals("opNotAllowed", result);
			assertEquals(1, this.getAction().getActionErrors().size());

			result = this.executeAction("admin", actionCode, "notExistantCode");
			assertEquals("ticketNotFound", result);
			assertEquals(1, this.getAction().getActionErrors().size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	private String executeAction(String username, String action, String code) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket", action);
		this.addParameter("code", code);
		String result = this.executeAction();
		return result;
	}

	private String executeAction(String username, String action, String code, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket", action);
		this.addParameter("code", code);
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}

}