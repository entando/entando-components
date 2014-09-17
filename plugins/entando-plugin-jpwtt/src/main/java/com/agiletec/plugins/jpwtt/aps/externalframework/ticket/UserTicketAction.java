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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpmail.aps.services.mail.util.EmailAddressValidator;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.ITicketManager;
import com.agiletec.plugins.jpwtt.aps.system.services.ticket.model.Ticket;
import com.agiletec.plugins.jpwtt.apsadmin.ticket.common.AbstractTicketAction;

/**
 * Implementation for the action class providing, for the current user, functions of creation ad visualization of a Ticket.
 * @version 1.0
 * @author E.Mezzano
 *
 */
public class UserTicketAction extends AbstractTicketAction implements IUserTicketAction {
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	@Override
	public void validate() {
		super.validate();
		ITicketManager ticketManager = this.getTicketManager();
		Integer interventionType = this.getInterventionType();
		if (null!=interventionType && 0!=interventionType.intValue() && null==ticketManager.getInterventionType(interventionType)) {
			this.addFieldError("interventionType", this.getText("errors.interventionType.notValid"));
		}
		Boolean trattamentoDati = this.getTrattamentoDati();
		if (null==trattamentoDati || !trattamentoDati.booleanValue()) {
			this.addFieldError("trattamentoDati", this.getText("trattamentoDati.required"));
		}
		String eMail = this.getEmail();
		if ((eMail!=null && eMail.length()>0) && !EmailAddressValidator.isValidEmailAddress(eMail)) {
			String[] args = { eMail };
			this.addFieldError("email", this.getText("errors.email.notValid", args));
		}
	}
	
	@Override
	public String save() {
		try {
			Ticket ticket = this.createTicket();
			this.getTicketManager().addTicket(ticket);
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected Ticket createTicket() {
		Ticket ticket = new Ticket();
		ticket.setAuthor(this.getCurrentUser().getUsername());
		ticket.setCreationDate(new Date());
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
		if (null!=this.getInterventionType()) {
			int id = this.getInterventionType().intValue();
			ticket.setUserInterventionType(id);
			ticket.setOpInterventionType(id);
		}
		ticket.setStatus(Ticket.STATES_OPENED);
		return ticket;
	}
	
	@Override
	protected boolean isAccessAllowed(Ticket ticket) {
		boolean allowed = false;
		String currentUser = this.getCurrentUser().getUsername();
		allowed = ticket!=null && currentUser.equals(ticket.getAuthor());
		return allowed;
	}
	
	/**
	 * @return The username
	 */
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
	
	public String getEmailConfirm() {
		return _emailConfirm;
	}
	public void setEmailConfirm(String emailConfirm) {
		this._emailConfirm = emailConfirm;
	}
	
	public String getMessage() {
		return _message;
	}
	public void setMessage(String message) {
		this._message = message;
	}
	
	public Integer getInterventionType() {
		return _interventionType;
	}
	public void setInterventionType(Integer interventionType) {
		this._interventionType = interventionType;
	}
	
	public Boolean getTrattamentoDati() {
		return _trattamentoDati;
	}
	public void setTrattamentoDati(Boolean trattamentoDati) {
		this._trattamentoDati = trattamentoDati;
	}
	
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
	private String _emailConfirm;
	private String _message;
	private Integer _interventionType;
	private Boolean _trattamentoDati;
	
}