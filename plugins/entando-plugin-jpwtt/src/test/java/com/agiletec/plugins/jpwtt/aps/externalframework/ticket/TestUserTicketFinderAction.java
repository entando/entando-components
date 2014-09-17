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

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.common.AbstractTicketFinderAction;
import com.opensymphony.xwork2.Action;

public class TestUserTicketFinderAction extends ApsAdminPluginBaseTestCase {

	public void testList() throws Throwable {
		String username = "mainEditor";
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), "subject1", "message1", username, 0, 0, "pageManagerCustomers", null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), "subject2", "message2", username, 1, 1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), "subject3", "message3", username, 2, 2, "editorCustomers", null, Ticket.STATES_CLOSED, null, false);
			this._ticketManager.addTicket(t3);

			this.setUserOnSession(username);
			this.initAction("/do/jpwtt/Ticket/User", "list");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			AbstractTicketFinderAction action = (AbstractTicketFinderAction) this.getAction();

			List<String> ticketIds = action.getTicketIds();
			assertEquals(3, ticketIds.size());
			assertTrue(ticketIds.contains(t1.getCode()));
			assertTrue(ticketIds.contains(t2.getCode()));
			assertTrue(ticketIds.contains(t3.getCode()));
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	public void testSearch() throws Throwable {
		String username = "mainEditor";
		try {
			assertEquals(0, this._ticketManager.searchTicketIds(null).size());
			Ticket t1 = this._helper.createTicket(null, new Date(), null, "message1", username, 0, 0, "pageManagerCustomers", null, Ticket.STATES_OPENED, null, false);
			this._ticketManager.addTicket(t1);
			Ticket t2 = this._helper.createTicket(null, new Date(), null, "message2", username, 1, 1, "mainEditor", null, Ticket.STATES_ASSIGNED, new Date(), false);
			this._ticketManager.addTicket(t2);
			Ticket t3 = this._helper.createTicket(null, new Date(), null, "message3", username, 2, 2, "editorCustomers", null, Ticket.STATES_ASSIGNABLE, null, true);
			this._ticketManager.addTicket(t3);
			Ticket t4 = this._helper.createTicket(null, new Date(), null, "message4", "pageManagerCustomers", 1, 0, "editorCustomers", null, Ticket.STATES_CLOSED, new Date(), false);
			this._ticketManager.addTicket(t4);

			Map<String, String> params = this.prepareParams("message2", "", "", "", "", "", "", "");
			String result = this.executeSearch(username, params); // message2 = 'message2'
			assertEquals(Action.SUCCESS, result);
			List<String> ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
			this.verifyTicketIds(new String[] { t2.getCode() }, ticketIds);

			params.put("message", "ess");
			result = this.executeSearch(username, params); // subject = 'ess'
			assertEquals(Action.SUCCESS, result);
			ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
			this.verifyTicketIds(new String[] { t1.getCode(), t2.getCode(), t3.getCode() }, ticketIds);

			params.put("priority", "0");
			result = this.executeSearch(username, params); // priority = '0'
			assertEquals(Action.SUCCESS, result);
			ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
			this.verifyTicketIds(new String[] { t1.getCode() }, ticketIds);

			params.put("userInterventionType", "1");
			result = this.executeSearch(username, params); // priority = '0', interventionType = '1'
			assertEquals(Action.SUCCESS, result);
			ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
			this.verifyTicketIds(new String[] { }, ticketIds);

			params.put("priority", "");
			result = this.executeSearch(username, params); // interventionType = '1'
			assertEquals(Action.SUCCESS, result);
			ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
			this.verifyTicketIds(new String[] { t2.getCode() }, ticketIds);

			params.put("status", String.valueOf(Ticket.STATES_ASSIGNABLE));
			result = this.executeSearch(username, params); // interventionType = '1', status = 'RESOLVED'
			assertEquals(Action.SUCCESS, result);
			ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
			this.verifyTicketIds(new String[] { }, ticketIds);

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
			this.verifyTicketIds(new String[] { t1.getCode(), t2.getCode() }, ticketIds);

			params.put("resolved", "");
			result = this.executeSearch(username, params); // resolved = ''
			assertEquals(Action.SUCCESS, result);
			ticketIds = ((AbstractTicketFinderAction) this.getAction()).getTicketIds();
			this.verifyTicketIds(new String[] { t1.getCode(), t2.getCode(), t3.getCode() }, ticketIds);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.deleteTickets();
		}
	}

	private String executeSearch(String username, Map<String, String> params) throws Throwable {
		this.setUserOnSession(username);
		this.initAction("/do/jpwtt/Ticket/User", "search");
		this.addParameters(params);
		String result = this.executeAction();
		return result;
	}

}