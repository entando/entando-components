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
package com.agiletec.plugins.jpwtt.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketSearchBean;

public class JpwttTestHelper extends AbstractDAO {

	public Ticket createTicket(String code, Date creationDate, String nome, String cognome, String codFisc,
			String comune, String localita, String tipoIndirizzo, String indirizzo, String numeroIndirizzo,
			String telefono, String email, String message, String author, int userInterventionType,
			int opInterventionType, int priority, String operator, String wttRole, int status, Date closingDate, boolean resolved) {
		Ticket ticket = new Ticket();
		ticket.setCode(code);
		ticket.setCreationDate(creationDate);
		ticket.setNome(nome);
		ticket.setCognome(cognome);
		ticket.setCodFisc(codFisc);
		ticket.setComune(comune);
		ticket.setLocalita(localita);
		ticket.setTipoIndirizzo(tipoIndirizzo);
		ticket.setIndirizzo(indirizzo);
		ticket.setNumeroIndirizzo(numeroIndirizzo);
		ticket.setTelefono(telefono);
		ticket.setEmail(email);
		ticket.setMessage(message);
		ticket.setAuthor(author);
		ticket.setUserInterventionType(userInterventionType);
		ticket.setOpInterventionType(opInterventionType);
		ticket.setPriority(priority);
		ticket.setWttOperator(operator);
		ticket.setWttRole(wttRole);
		ticket.setStatus(status);
		ticket.setClosingDate(closingDate);
		ticket.setResolved(resolved);
		return ticket;
	}

	public Ticket createTicket(String code, Date creationDate, String email, String message, String author,
			int interventionType, int priority, String operator, String wttRole, int status, Date closingDate, boolean resolved) {
		Ticket ticket = new Ticket();
		ticket.setCode(code);
		ticket.setCreationDate(creationDate);
		ticket.setEmail(email);
		ticket.setMessage(message);
		ticket.setAuthor(author);
		ticket.setUserInterventionType(interventionType);
		ticket.setOpInterventionType(interventionType);
		ticket.setPriority(priority);
		ticket.setWttOperator(operator);
		ticket.setWttRole(wttRole);
		ticket.setStatus(status);
		ticket.setClosingDate(closingDate);
		ticket.setResolved(resolved);
		return ticket;
	}

	public TicketOperation createTicketOperation(int id, String ticketCode,
			String operator, int operationCode, String note, int interventionType, int priority, String wttRole, Date date) {
		TicketOperation operation = new TicketOperation();
		operation.setId(id);
		operation.setTicketCode(ticketCode);
		operation.setOperator(operator);
		operation.setOperationCode(operationCode);
		operation.setNote(note);
		operation.setInterventionType(interventionType);
		operation.setPriority(priority);
		operation.setWttRole(wttRole);
		operation.setDate(date);
		return operation;
	}

	public TicketSearchBean createSearchBean(String message, String author, String operator, String[] wttRoles,
			int[] states, Integer priority, Integer userInterventionType, Integer opInterventionType, Boolean resolved) {
		TicketSearchBean searchBean = new TicketSearchBean();
		searchBean.setMessage(message);
		searchBean.setAuthor(author);
		searchBean.setOperator(operator);
		searchBean.setWttRoles(wttRoles);
		searchBean.setStates(states);
		searchBean.setPriority(priority);
		searchBean.setUserInterventionType(userInterventionType);
		searchBean.setAssignedInterventionType(opInterventionType);
		searchBean.setResolved(resolved);
		return searchBean;
	}

	public void deleteTickets() throws ApsSystemException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.executeQuery(DELETE_TICKETOPERATIONS, conn);
			this.executeQuery(DELETE_TICKETS, conn);
		} catch (Throwable t) {
			processDaoException(t, "Error deleting tickets", "deleteTicket");
		} finally {
			closeConnection(conn);
		}
	}

	public void initPermissions() throws ApsSystemException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.executeQuery(ADD_PERMISSION, "jpwttAdmin", "Admin WTT", conn);
			this.executeQuery(ADD_PERMISSION, "jpwttOperator", "Operator WTT", conn);
			this.executeQuery(ADD_ROLEPERMISSION, "editor", "jpwttOperator", conn);
			this.executeQuery(ADD_ROLEPERMISSION, "supervisor", "jpwttOperator", conn);
			this.executeQuery(ADD_ROLEPERMISSION, "supervisor", "jpwttAdmin", conn);
		} catch (Throwable t) {
			processDaoException(t, "Error adding permissions", "deleteTicket");
		} finally {
			closeConnection(conn);
		}
	}

	public void deletePermissions() throws ApsSystemException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.executeQuery(DELETE_ROLEPERMISSIONS, conn);
			this.executeQuery(DELETE_PERMISSIONS, conn);
		} catch (Throwable t) {
			processDaoException(t, "Error deleting permissions", "deleteTicket");
		} finally {
			closeConnection(conn);
		}
	}

	protected void executeQuery(String query, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			stat.executeUpdate();
		} catch (SQLException e) {
			processDaoException(e, "Error executing query", "executeQuery");
		} finally {
			closeDaoResources(null, stat);
		}
	}

	protected void executeQuery(String query, String param1, String param2, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(query);
			stat.setString(1, param1);
			stat.setString(2, param2);
			stat.executeUpdate();
		} catch (SQLException e) {
			processDaoException(e, "Error executing query", "executeQuery");
		} finally {
			closeDaoResources(null, stat);
		}
	}

	private final String ADD_PERMISSION =
		"INSERT INTO authpermissions ( permissionname, descr ) VALUES ( ?, ? )";

	private final String ADD_ROLEPERMISSION =
		"INSERT INTO authrolepermissions ( rolename, permissionname ) VALUES ( ?, ? )";

	private final String DELETE_PERMISSIONS =
		"DELETE FROM authpermissions WHERE permissionname = 'jpwttAdmin' OR permissionname = 'jpwttOperator'";

	private final String DELETE_ROLEPERMISSIONS =
		"DELETE FROM authrolepermissions WHERE permissionname = 'jpwttAdmin' OR permissionname = 'jpwttOperator'";

	private final String DELETE_TICKETS = "DELETE FROM jpwtt_tickets";

	private final String DELETE_TICKETOPERATIONS = "DELETE FROM jpwtt_ticketoperations";

}