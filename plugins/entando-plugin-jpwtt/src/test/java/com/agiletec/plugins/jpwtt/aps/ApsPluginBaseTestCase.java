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
package com.agiletec.plugins.jpwtt.aps;

import java.util.List;

import javax.sql.DataSource;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpwtt.PluginConfigTestUtils;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.util.JpwttTestHelper;

public class ApsPluginBaseTestCase extends BaseTestCase {

	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}

    protected void setUp() throws Exception {
    	super.setUp();
    	this.init();
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
		assertEquals(DateConverter.getFormattedDate(t1.getCreationDate(), "ddMMyyyyHHmmss"),
				DateConverter.getFormattedDate(t2.getCreationDate(), "ddMMyyyyHHmmss"));
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
		assertEquals(DateConverter.getFormattedDate(t1.getClosingDate(), "ddMMyyyyHHmmss"),
				DateConverter.getFormattedDate(t2.getClosingDate(), "ddMMyyyyHHmmss"));
		assertEquals(t1.isResolved(), t2.isResolved());
	}

	protected void compareTicketOperations(TicketOperation t1, TicketOperation t2) {
		assertEquals(t1.getId(), t2.getId());
		assertEquals(t1.getTicketCode(), t2.getTicketCode());
		assertEquals(t1.getOperator(), t2.getOperator());
		assertEquals(t1.getOperationCode(), t2.getOperationCode());
		assertEquals(t1.getNote(), t2.getNote());
		assertEquals(DateConverter.getFormattedDate(t1.getDate(), "ddMMyyyyHHmmss"),
				DateConverter.getFormattedDate(t2.getDate(), "ddMMyyyyHHmmss"));
	}

    private void init() throws Exception {
    	try {
    		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
    		this._helper = new JpwttTestHelper();
    		this._helper.setDataSource(dataSource);
		} catch (Exception e) {
			throw e;
		}
	}

	protected JpwttTestHelper _helper;

}
