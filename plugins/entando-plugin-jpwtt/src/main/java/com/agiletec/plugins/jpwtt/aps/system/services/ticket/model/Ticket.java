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
package com.agiletec.plugins.jpwtt.aps.system.services.ticket.model;

import java.util.Date;

public class Ticket implements Cloneable {
	
	@Override
	public Ticket clone() {
		Ticket ticket = new Ticket();
		ticket.setCode(this.getCode());
		ticket.setCreationDate(this.getCreationDate());
		ticket.setNome(this.getNome());
		ticket.setCognome(this.getCognome());
		ticket.setCodFisc(this.getCodFisc());
		ticket.setComune(this.getComune());
		ticket.setLocalita(this.getLocalita());
		ticket.setTipoIndirizzo(this.getTipoIndirizzo());
		ticket.setIndirizzo(this.getIndirizzo());
		ticket.setNumeroIndirizzo(this.getNumeroIndirizzo());
		ticket.setTelefono(this.getTelefono());
		ticket.setEmail(this.getEmail());
		ticket.setMessage(this.getMessage());
		ticket.setAuthor(this.getAuthor());
		ticket.setUserInterventionType(this.getUserInterventionType());
		ticket.setOpInterventionType(this.getOpInterventionType());
		ticket.setPriority(this.getPriority());
		ticket.setWttOperator(this.getWttOperator());
		ticket.setWttRole(this.getWttRole());
		ticket.setStatus(this.getStatus());
		ticket.setClosingDate(this.getClosingDate());
		ticket.setResolved(this.isResolved());
		return ticket;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	public Date getCreationDate() {
		return _creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this._creationDate = creationDate;
	}
	
	public String getNome() {
		return _nome;
	}
	public void setNome(String nome) {
		this._nome = nome;
	}
	
	public String getCognome() {
		return _cognome;
	}
	public void setCognome(String cognome) {
		this._cognome = cognome;
	}
	
	public String getCodFisc() {
		return _codFisc;
	}
	public void setCodFisc(String codFisc) {
		this._codFisc = codFisc;
	}
	
	public String getComune() {
		return _comune;
	}
	public void setComune(String comune) {
		this._comune = comune;
	}
	
	public String getLocalita() {
		return _localita;
	}
	public void setLocalita(String localita) {
		this._localita = localita;
	}
	
	public String getTipoIndirizzo() {
		return _tipoIndirizzo;
	}
	public void setTipoIndirizzo(String tipoIndirizzo) {
		this._tipoIndirizzo = tipoIndirizzo;
	}
	
	public String getIndirizzo() {
		return _indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this._indirizzo = indirizzo;
	}
	
	public String getNumeroIndirizzo() {
		return _numeroIndirizzo;
	}
	public void setNumeroIndirizzo(String numeroIndirizzo) {
		this._numeroIndirizzo = numeroIndirizzo;
	}
	
	public String getTelefono() {
		return _telefono;
	}
	public void setTelefono(String telefono) {
		this._telefono = telefono;
	}
	
	public String getEmail() {
		return _email;
	}
	public void setEmail(String email) {
		this._email = email;
	}
	
	public String getMessage() {
		return _message;
	}
	public void setMessage(String message) {
		this._message = message;
	}
	
	public String getAuthor() {
		return _author;
	}
	public void setAuthor(String author) {
		this._author = author;
	}
	
	public int getUserInterventionType() {
		return _userInterventionType;
	}
	public void setUserInterventionType(int userInterventionType) {
		this._userInterventionType = userInterventionType;
	}
	
	public int getOpInterventionType() {
		return _opInterventionType;
	}
	public void setOpInterventionType(int opInterventionType) {
		this._opInterventionType = opInterventionType;
	}
	
	public int getPriority() {
		return _priority;
	}
	public void setPriority(int priority) {
		this._priority = priority;
	}
	
	public String getWttOperator() {
		return _wttOperator;
	}
	public void setWttOperator(String wttOperator) {
		this._wttOperator = wttOperator;
	}
	
	public String getWttRole() {
		return _wttRole;
	}
	public void setWttRole(String wttRole) {
		this._wttRole = wttRole;
	}
	
	public int getStatus() {
		return _status;
	}
	public void setStatus(int status) {
		this._status = status;
	}
	
	public Date getClosingDate() {
		return _closingDate;
	}
	public void setClosingDate(Date closingDate) {
		this._closingDate = closingDate;
	}
	
	public boolean isResolved() {
		return _resolved;
	}
	public void setResolved(boolean resolved) {
		this._resolved = resolved;
	}
	
	private String _code;
	private Date _creationDate;
	private String _nome;
	private String _cognome;
	private String _codFisc;
	private String _comune;
	private String _localita;
	private String _tipoIndirizzo;
	private String _indirizzo;
	private String _numeroIndirizzo;
	private String _telefono;
	private String _email;
	private String _message;
	private String _author;
	private int _userInterventionType;
	private int _opInterventionType;
	private int _priority;
	private String _wttOperator;
	private String _wttRole;
	private int _status;
	private Date _closingDate;
	private boolean _resolved;
	
	public static final int STATES_OPENED = 1;
	public static final int STATES_WORKING = 2;
	public static final int STATES_ASSIGNABLE = 5;
	public static final int STATES_ASSIGNED = 10;
	public static final int STATES_DISPATCHED = 15;
	public static final int STATES_CLOSED = 20;
	
}