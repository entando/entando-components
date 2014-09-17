/*
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * JAPS and its  source-code is  licensed under the  terms of the
 * GNU General Public License  as published by  the Free Software
 * Foundation (http://www.fsf.org/licensing/licenses/gpl.txt).
 * 
 * You may copy, adapt, and redistribute this file for commercial
 * or non-commercial use.
 * When copying,  adapting,  or redistributing  this document you
 * are required to provide proper attribution  to AgileTec, using
 * the following attribution line:
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpwtt.aps.system.services.ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.ITicketSearchBean;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.TicketOperation;

/**
 * Data Access Object per gli oggetti Ticket .
 * @version 1.1
 * @author S.Didaci - G.Cocco
 */
public class TicketDAO extends AbstractDAO implements ITicketDAO {
	
	/**
	 * Carica l' elenco dei ticket (chiamate all'assistenza tecnica) inserite nel sistema.
	 * @param conn la connessione al db.
	 * @param ctx Il contesto del sistema.
	 * @return La mappa dei ticket 
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	@Override
	public List<Ticket> loadTickets() throws ApsSystemException {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		List<Ticket> tickets = new ArrayList<Ticket>();
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_ALL_TICKETS);
			while(res.next()) {
				Ticket ticket = this.loadTicket(res);
				tickets.add(ticket);
			}
		} catch (Throwable t) {
			processDaoException(t, "Errore in caricamento tickets", "loadTickets");
		} finally {
			closeDaoResources(res, stat,conn);
		}
		return tickets;
	}
	
	@Override
	public List<String> searchTicketIds(ITicketSearchBean searchBean) throws ApsSystemException {
		List<String> tickets = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = this.createQueryString(searchBean);
			stat = conn.prepareStatement(query);
			this.buildStatement(searchBean, stat);
			res = stat.executeQuery();
			this.flowResult(tickets, searchBean, res);
		} catch (Throwable t) {
			processDaoException(t, "Errore in caricamento lista id ticket", "searchTicketIds");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return tickets;
	}
	
	protected void flowResult(List<String> tickets, ITicketSearchBean searchBean, ResultSet res) throws Throwable {
		if (searchBean!=null) {
			String message = searchBean.getMessage();
			if (message!=null && message.length()>0) {
				while (res.next()) {
					String ticketId = res.getString(1);
					String ticketMessage = res.getString(2);
					if (this.checkText(message, ticketMessage)) {
						tickets.add(ticketId);
					}
				}
				return;
			}
		}
		while (res.next()) {
			String ticketId = res.getString(1);
			tickets.add(ticketId);
		}
	}
	
	/**
	 * This utility method checks if the given Text matches or is contained inside
	 * another one.
	 * @param insertedText The text to look for
	 * @param text The text to search in
	 * @return True if an occurrence of 'insertedText' is found in 'text'.
	 */
	protected boolean checkText(String insertedText, String text) {
		if (insertedText.trim().length() == 0 || 
				(text != null && text.toLowerCase().indexOf(insertedText.trim().toLowerCase()) != -1)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Carica unno specifico ticket dato il suo identificativo (ticketNumber)
	 * @param res Il resultset da leggere
	 * @param ctx Il contesto di sistema
	 * @return Il ticket.
	 * @throws ApsSystemException 
	 */
	@Override
	public Ticket loadTicket(String code) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Ticket ticket = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_TICKET);
			stat.setString(1, code);
			res = stat.executeQuery();
			if (res.next()) {
				ticket = this.loadTicket(res);
			}
		} catch (Throwable t) {
			processDaoException(t, "Errore in caricamento ticket", "loadTicket");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return ticket;
	}
	
	@Override
	public List<TicketOperation> loadTicketOperations(String code) throws ApsSystemException {
		List<TicketOperation> operations = new ArrayList<TicketOperation>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_TICKETOPERATIONS);
			stat.setString(1, code);
			res = stat.executeQuery();
			while (res.next()) {
				TicketOperation operation = this.loadTicketOperation(res);
				operations.add(operation);
			}
		} catch (Throwable t) {
			processDaoException(t, "Errore in caricamento ticket operations", "loadTicketOperations");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return operations;
	}
	
	/**
	 * Costruisce e restituisce un ticket leggendo una riga di recordset.
	 * @param res Il resultset da leggere
	 * @param ctx Il contesto di sistema
	 * @return il ticket.
	 * @throws ApsSystemException 
	 */
	@Override
	public void addTicket(Ticket ticket) throws ApsSystemException {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(ADD_TICKET);
			stat.setString(1, ticket.getCode());
			stat.setTimestamp(2, new Timestamp(ticket.getCreationDate().getTime()));
			stat.setString(3, ticket.getNome());
			stat.setString(4, ticket.getCognome());
			stat.setString(5, ticket.getCodFisc());
			stat.setString(6, ticket.getComune());
			stat.setString(7, ticket.getLocalita());
			stat.setString(8, ticket.getTipoIndirizzo());
			stat.setString(9, ticket.getIndirizzo());
			stat.setString(10, ticket.getNumeroIndirizzo());
			stat.setString(11, ticket.getTelefono());
			stat.setString(12, ticket.getEmail());
			stat.setString(13, ticket.getMessage());
			stat.setString(14, ticket.getAuthor());
			stat.setInt(15, ticket.getUserInterventionType());
			stat.setInt(16, ticket.getOpInterventionType());
			stat.setInt(17, ticket.getPriority());
			stat.setString(18, ticket.getWttRole());
			stat.setString(19, ticket.getWttOperator());
			stat.setInt(20, ticket.getStatus());
			Timestamp closingTime = (ticket.getClosingDate()!=null) ? new Timestamp(ticket.getClosingDate().getTime()) : null;
			stat.setTimestamp(21, closingTime);
			int resolved = ticket.isResolved() ? 1 : 0;
			stat.setInt(22, resolved);
			stat.executeUpdate();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error updating ticket", "addTicket");
		} finally{			
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	public void updateTicketWithOperation(Ticket ticket, TicketOperation operation) throws ApsSystemException {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.addTicketOperation(operation, conn);
			this.updateTicket(ticket, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error updating Ticket with operation", "updateTicketWithOperation");
		} finally {
			closeConnection(conn);
		}
	}
	
	protected void updateTicket(Ticket ticket, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_TICKET);
			stat.setTimestamp(1, new Timestamp(ticket.getCreationDate().getTime()));
			stat.setString(2, ticket.getNome());
			stat.setString(3, ticket.getCognome());
			stat.setString(4, ticket.getCodFisc());
			stat.setString(5, ticket.getComune());
			stat.setString(6, ticket.getLocalita());
			stat.setString(7, ticket.getTipoIndirizzo());
			stat.setString(8, ticket.getIndirizzo());
			stat.setString(9, ticket.getNumeroIndirizzo());
			stat.setString(10, ticket.getTelefono());
			stat.setString(11, ticket.getEmail());
			stat.setString(12, ticket.getMessage());
			stat.setString(13, ticket.getAuthor());
			stat.setInt(14, ticket.getUserInterventionType());
			stat.setInt(15, ticket.getOpInterventionType());
			stat.setInt(16, ticket.getPriority());
			stat.setString(17, ticket.getWttRole());
			stat.setString(18, ticket.getWttOperator());
			stat.setInt(19, ticket.getStatus());
			Timestamp closingTime = (ticket.getClosingDate()!=null) ? new Timestamp(ticket.getClosingDate().getTime()) : null;
			stat.setTimestamp(20, closingTime);
			int resolved = ticket.isResolved() ? 1 : 0;
			stat.setInt(21, resolved);
			stat.setString(22, ticket.getCode());
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error updating ticket", "updateTicket");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected void addTicketOperation(TicketOperation operation, Connection conn) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_TICKETOPERATION);
			stat.setInt(1, operation.getId());
			stat.setString(2, operation.getTicketCode());
			stat.setString(3, operation.getOperator());
			stat.setInt(4, operation.getOperationCode());
			stat.setInt(5, operation.getInterventionType());
			stat.setInt(6, operation.getPriority());
			stat.setString(7, operation.getWttRole());
			stat.setString(8, operation.getNote());
			Timestamp time = (operation.getDate()!=null) ? new Timestamp(operation.getDate().getTime()) : null;
			stat.setTimestamp(9, time);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error adding ticket operation", "addTicketOperation");
		} finally{			
			this.closeDaoResources(null, stat);
		}
	}
	
	protected String createQueryString(ITicketSearchBean searchBean) {
		StringBuffer query = new StringBuffer();
		if (searchBean!=null) {
			String message = searchBean.getMessage();
			if (message!=null && message.length()>0) {
				query.append(SELECT_TICKET_CODES_FLOWRESULT);
			} else {
				query.append(SELECT_TICKET_CODES);
			}
			boolean appendWhere = true;
			String author = searchBean.getAuthor();
			if (author!=null && author.length()>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_AUTHOR_CLAUSE);
				appendWhere = false;
			}
			int[] states = searchBean.getStates();
			if (states!=null && states.length>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				int size = states.length;
				if (size==1) {
					query.append(APPEND_STATUS_CLAUSE);
				} else {
					query.append(APPEND_STATUSIN_STARTCLAUSE);
					query.append(APPEND_IN_FIRSTVALUECLAUSE);
					while (size>1) {
						query.append(APPEND_IN_OTHERVALUECLAUSE);
						size--;
					}
					query.append(APPEND_IN_ENDCLAUSE);
				}
				appendWhere = false;
			}
			if (searchBean.getPriority()!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_PRIORITY_CLAUSE);
				appendWhere = false;
			}
			if (searchBean.getUserInterventionType()!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_USER_INTERVENTIONTYPE_CLAUSE);
				appendWhere = false;
			}
			if (searchBean.getAssignedInterventionType()!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_OP_INTERVENTIONTYPE_CLAUSE);
				appendWhere = false;
			}
			if (searchBean.getResolved()!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_RESOLVED_CLAUSE);
				appendWhere = false;
			}
			String operator = searchBean.getOperator();
			if (operator!=null && operator.length()>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_OPERATOR_CLAUSE);
				appendWhere = false;
			}
			String[] roles = searchBean.getWttRoles();
			if (roles!=null && roles.length>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				int size = roles.length;
				if (size==1) {
					query.append(APPEND_ROLES_CLAUSE);
				} else {
					query.append(APPEND_ROLESIN_STARTCLAUSE);
					query.append(APPEND_IN_FIRSTVALUECLAUSE);
					while (size>1) {
						query.append(APPEND_IN_OTHERVALUECLAUSE);
						size--;
					}
					query.append(APPEND_IN_ENDCLAUSE);
				}
				appendWhere = false;
			}
		} else {
			query.append(SELECT_TICKET_CODES);
		}
		query.append(APPEND_ORDERBY_CLAUSE);
		return query.toString();
	}
	
	protected int buildStatement(ITicketSearchBean searchBean, PreparedStatement stat) throws SQLException {
		int pos = 1;
		if (searchBean!=null) {
			String author = searchBean.getAuthor();
			if (author!=null && author.length()>0) {
				stat.setString(pos++, author);
			}
			int[] states = searchBean.getStates();
			if (states!=null && states.length>0) {
				for (int status : states) {
					stat.setInt(pos++, status);
				}
			}
			Integer priority = searchBean.getPriority();
			if (priority!=null) {
				stat.setInt(pos++, priority.intValue());
			}
			Integer userInterventionType = searchBean.getUserInterventionType();
			if (userInterventionType!=null) {
				stat.setInt(pos++, userInterventionType.intValue());
			}
			Integer opInterventionType = searchBean.getAssignedInterventionType();
			if (opInterventionType!=null) {
				stat.setInt(pos++, opInterventionType.intValue());
			}
			Boolean resolved = searchBean.getResolved();
			if (resolved!=null) {
				int resolvedValue = resolved.booleanValue() ? 1 : 0;
				stat.setInt(pos++, resolvedValue);
			}
			String operator = searchBean.getOperator();
			if (operator!=null && operator.length()>0) {
				stat.setString(pos++, operator);
			}
			String[] roles = searchBean.getWttRoles();
			if (roles!=null && roles.length>0) {
				for (String role : roles) {
					stat.setString(pos++, role);
				}
			}
		}
		return pos;
	}
	
	/**
	 * Costruisce e restituisce un ticket leggendo una riga di recordset.
	 * @param res Il resultset da leggere
	 * @param ctx Il contesto di sistema
	 * @return Il ticket.
	 * @throws ApsSystemException 
	 */
	private Ticket loadTicket(ResultSet res) throws ApsSystemException {
		Ticket ticket = new Ticket();
		try {
			ticket.setCode(res.getString(1));
			ticket.setCreationDate(res.getTimestamp(2));
			ticket.setNome(res.getString(3));
			ticket.setCognome(res.getString(4));
			ticket.setCodFisc(res.getString(5));
			ticket.setComune(res.getString(6));
			ticket.setLocalita(res.getString(7));
			ticket.setTipoIndirizzo(res.getString(8));
			ticket.setIndirizzo(res.getString(9));
			ticket.setNumeroIndirizzo(res.getString(10));
			ticket.setTelefono(res.getString(11));
			ticket.setEmail(res.getString(12));
			ticket.setMessage(res.getString(13));
			ticket.setAuthor(res.getString(14));
			ticket.setUserInterventionType(res.getInt(15));
			ticket.setOpInterventionType(res.getInt(16));
			ticket.setPriority(res.getInt(17));
			ticket.setWttRole(res.getString(18));
			ticket.setWttOperator(res.getString(19));
			ticket.setStatus(res.getInt(20));
			ticket.setClosingDate(res.getTimestamp(21));
			int resolved = res.getInt(22);
			ticket.setResolved(resolved==1);
		} catch (Throwable t) {
			throw new ApsSystemException("Errore in caricamento ticket", t);
		}
		return ticket;
	}
	
	private TicketOperation loadTicketOperation(ResultSet res) throws SQLException {
		TicketOperation ticketOperation = new TicketOperation();
		ticketOperation.setId(res.getInt(1));
		ticketOperation.setTicketCode(res.getString(2));
		ticketOperation.setOperator(res.getString(3));
		ticketOperation.setOperationCode(res.getInt(4));
		ticketOperation.setInterventionType(res.getInt(5));
		ticketOperation.setPriority(res.getInt(6));
		ticketOperation.setWttRole(res.getString(7));
		ticketOperation.setNote(res.getString(8));
		ticketOperation.setDate(res.getTimestamp(9));
		return ticketOperation;
	}
	
	private final String SELECT_TICKET_CODES = 
		"SELECT code FROM jpwtt_tickets ";
	
	private final String SELECT_TICKET_CODES_FLOWRESULT = 
		"SELECT code, message FROM jpwtt_tickets ";
	
	private final String APPEND_WHERE = "WHERE ";
	private final String APPEND_AND = "AND ";
	private final String APPEND_AUTHOR_CLAUSE  = "author = ? ";
	
	private final String APPEND_STATUS_CLAUSE  = "status = ? ";
	private final String APPEND_STATUSIN_STARTCLAUSE  = "status IN ( ";
	private final String APPEND_IN_ENDCLAUSE  = ") ";
	private final String APPEND_IN_FIRSTVALUECLAUSE  = "? ";
	private final String APPEND_IN_OTHERVALUECLAUSE  = ", ? ";
	
	private final String APPEND_ROLES_CLAUSE  = "wttrole = ? ";
	private final String APPEND_ROLESIN_STARTCLAUSE  = "wttrole IN ( ";
	
	private final String APPEND_PRIORITY_CLAUSE  = "priority = ? ";
	private final String APPEND_USER_INTERVENTIONTYPE_CLAUSE  = "user_interventiontype = ? ";
	private final String APPEND_OP_INTERVENTIONTYPE_CLAUSE  = "op_interventiontype = ? ";
	private final String APPEND_RESOLVED_CLAUSE  = "resolved = ? ";
	private final String APPEND_ORDERBY_CLAUSE = "ORDER BY creationdate DESC ";
	private final String APPEND_OPERATOR_CLAUSE  = "wttoperator = ? ";
	
	private final String LOAD_ALL_TICKETS = 
		"SELECT code, creationdate, nome, cognome, codfisc, comune, localita, tipoindirizzo, indirizzo, " +
		"numeroindirizzo, telefono, email, message, author, user_interventiontype, op_interventiontype, priority, " +
		"wttrole, wttoperator, status, closingdate, resolved FROM jpwtt_tickets ORDER BY code desc";
	
	private final String LOAD_TICKET = 
		"SELECT code, creationdate, nome, cognome, codfisc, comune, localita, tipoindirizzo, indirizzo, " +
		"numeroindirizzo, telefono, email, message, author, user_interventiontype, op_interventiontype, priority, " +
		"wttrole, wttoperator, status, closingdate, resolved FROM jpwtt_tickets WHERE code = ? ";
	
	private final String LOAD_TICKETOPERATIONS = 
		"SELECT id, ticketcode, operator, operationcode, interventiontype, " +
		"priority, wttrole, note, date FROM jpwtt_ticketoperations WHERE ticketcode = ? ";
	
	private final String ADD_TICKET =
		"INSERT INTO jpwtt_tickets ( code, creationdate, nome, cognome, codfisc, comune, localita, tipoindirizzo, indirizzo, " +
		"numeroindirizzo, telefono, email, message, author, user_interventiontype, op_interventiontype, priority, wttrole, " +
		"wttoperator, status, closingdate, resolved ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ? )";
	
	private final String UPDATE_TICKET =
		"UPDATE jpwtt_tickets SET creationdate = ?, nome = ?, cognome = ?, codfisc = ?, comune = ?, localita = ?, tipoindirizzo = ?, " +
		"indirizzo = ?, numeroindirizzo = ?, telefono = ?, email = ?, message = ?, author = ?, user_interventiontype = ?, " +
		"op_interventiontype = ?, priority = ?, wttrole = ?, wttoperator = ?, status = ?, closingdate = ?, resolved = ? WHERE code = ?";
	
	private final String ADD_TICKETOPERATION =
		"INSERT INTO jpwtt_ticketoperations ( id, ticketcode, operator, operationcode, interventiontype, " +
		"priority, wttrole, note, date ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
	
}