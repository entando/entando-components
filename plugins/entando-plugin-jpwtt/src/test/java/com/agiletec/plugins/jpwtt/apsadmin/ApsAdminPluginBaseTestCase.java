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
package com.agiletec.plugins.jpwtt.apsadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.ApsAdminBaseTestCase;
import com.agiletec.plugins.jpwtt.PluginConfigTestUtils;
import com.agiletec.plugins.jpwtt.aps.system.services.JpWttSystemConstants;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.ITicketManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.TicketManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.util.JpwttTestHelper;

public class ApsAdminPluginBaseTestCase extends ApsAdminBaseTestCase {

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}

    protected void setUp() throws Exception {
    	super.setUp();
        this.init();
        IManager roleManager = (IManager) this.getService(SystemConstants.ROLE_MANAGER);
        try {
			roleManager.refresh();
		} catch (Throwable t) {
			throw new Exception(t);
		}
    }

	@Override
	protected void tearDown() throws Exception {
		this._helper.deleteTickets();
		this._helper.deletePermissions();
		super.tearDown();
	}

	protected void verifyTicketIds(String[] codes, List<String> ticketIds) {
		assertEquals(codes.length, ticketIds.size());
		for (int i=0; i<codes.length; i++) {
			String code = codes[i];
			assertTrue(ticketIds.contains(code));
		}
	}

	protected void compareTickets(Ticket t1, Ticket t2) {
		assertEquals(t1.getCode(), t2.getCode());
		assertEquals(DateConverter.getFormattedDate(t1.getCreationDate(), "ddMMyyyyHHmmssSSSS"),
				DateConverter.getFormattedDate(t2.getCreationDate(), "ddMMyyyyHHmmssSSSS"));
		assertEquals(t1.getNome(), t2.getNome());
		assertEquals(t1.getCognome(), t2.getCognome());
		assertEquals(t1.getCodFisc(), t2.getCodFisc());
		assertEquals(t1.getComune(), t2.getComune());
		assertEquals(t1.getLocalita(), t2.getLocalita());
		assertEquals(t1.getTipoIndirizzo(), t2.getTipoIndirizzo());
		assertEquals(t1.getIndirizzo(), t2.getIndirizzo());
		assertEquals(t1.getNumeroIndirizzo(), t2.getNumeroIndirizzo());
		assertEquals(t1.getTelefono(), t2.getTelefono());
		assertEquals(t1.getEmail(), t2.getEmail());
		assertEquals(t1.getMessage(), t2.getMessage());
		assertEquals(t1.getAuthor(), t2.getAuthor());
		assertEquals(t1.getUserInterventionType(), t2.getUserInterventionType());
		assertEquals(t1.getOpInterventionType(), t2.getOpInterventionType());
		assertEquals(t1.getPriority(), t2.getPriority());
		assertEquals(t1.getWttOperator(), t2.getWttOperator());
		assertEquals(t1.getWttRole(), t2.getWttRole());
		assertEquals(t1.getStatus(), t2.getStatus());
		assertEquals(DateConverter.getFormattedDate(t1.getClosingDate(), "ddMMyyyyHHmmssSSSS"),
				DateConverter.getFormattedDate(t2.getClosingDate(), "ddMMyyyyHHmmssSSSS"));
		assertEquals(t1.isResolved(), t2.isResolved());
	}

	protected void compareTicketOperations(TicketOperation t1, TicketOperation t2) {
		assertEquals(t1.getId(), t2.getId());
		assertEquals(t1.getTicketCode(), t2.getTicketCode());
		assertEquals(t1.getOperator(), t2.getOperator());
		assertEquals(t1.getOperationCode(), t2.getOperationCode());
		assertEquals(t1.getNote(), t2.getNote());
		assertEquals(t1.getDate(), t2.getDate());
	}

	protected Map<String, String> prepareParams(String message, String author, String operator, String status,
			String priority, String userInterventionType, String assignedInterventionType, String resolved) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("message", message);
		params.put("author", author);
		params.put("operator", operator);
		params.put("status", status);
		params.put("priority", priority);
		params.put("userInterventionType", userInterventionType);
		params.put("assignedInterventionType", assignedInterventionType);
		params.put("resolved", resolved);
		return params;
	}

	private void init() throws Exception {
    	try {
    		this._helper = new JpwttTestHelper();
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		this._helper.setDataSource(dataSource);
    		this._helper.initPermissions();
			this._ticketManager = (TicketManager) this.getService(JpWttSystemConstants.TICKET_MANAGER);
		} catch (Exception e) {
			throw e;
		}
    }

	protected JpwttTestHelper _helper;
	protected ITicketManager _ticketManager = null;

}
