/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.addressbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.addressbook.model.AddressBookContact;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper.INewMessageActionHelper;

/**
 * @author E.Santoboni
 */
public class AddressBookAction extends BaseAction implements IAddressBookAction {
	
	@Override
	public String editRecipients() {
		try {			
			this.getNewMessageHelper().updateMessageOnSession(this.getRequest());
			// verifica la presenza del manager
			if (null == this.getAddressBookManager()) {
				this.addActionMessage(this.getText("Message.addressBookManager.noSuitableManagerFound"));
				return "noSuitableManagerFound";
			}
			// controlla se il recipient è valido
			List<String> allowedRecipents = Arrays.asList( 
					new String[] { JpwebmailSystemConstants.TO_RECIPIENT,
							JpwebmailSystemConstants.CC_RECIPIENT,
							JpwebmailSystemConstants.BCC_RECIPIENT } 
			);
			if (null == allowedRecipents || !allowedRecipents.contains(this.getActualRecipient())) {	    	
				ApsSystemUtils.getLogger().error("ERRORE: impossibile determinare il tipo di recipient selezionato");
				this.addActionError(this.getText("Error.addressBookManager.unknownRecipient",new String[]{this.getActualRecipient()}));
				return "unknownRecipient";
			}
			this.setSearchedReceivers(this.getAddressBookManager().searchContacts(this.getCurrentUser(), this.getText()));			
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "editRecipients");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String addRecipients() {
		try {
			this.setSearchedReceivers(this.getAddressBookManager().searchContacts(this.getCurrentUser(), this.getText()));
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "addRecipients");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String joinRecipients() {
		MimeMessage currentMessage = (MimeMessage) this.getRequest().getSession().getAttribute(JpwebmailSystemConstants.SESSIONPARAM_CURRENT_MESSAGE_ON_EDIT);
		if (null == this.getSelectedReceivers()) {
			return SUCCESS;
		}
		try {
			// premendo fine si cancellano gli indirizzi precedentementi immessi
			List<InternetAddress> addresses = new ArrayList<InternetAddress>(this.getSelectedReceivers().size());
			for (String username : this.getSelectedReceivers()) {
				AddressBookContact contact = this.getContact(username);
				if (null != contact) {
					InternetAddress address = new InternetAddress(contact.getFullName()+" <"+contact.getEmailAddress()+">");
					addresses.add(address);
				} else {
					return FAILURE;
				}
			}
			InternetAddress[] finalAddresses = new InternetAddress[addresses.size()];
			int i = 0;
			for (InternetAddress address : addresses) {
				finalAddresses[i++] = address;
			}
			switch (Integer.valueOf(this.getActualRecipient())) {
			case 1:
				currentMessage.setRecipients(RecipientType.TO, finalAddresses);
				break;
			case 2:
				currentMessage.setRecipients(RecipientType.CC, finalAddresses);
				break;
			case 3:
				currentMessage.setRecipients(RecipientType.BCC, finalAddresses);
				break;
			}
		} catch (MessagingException e) {
			ApsSystemUtils.logThrowable(e, this, "joinRecipients");
			return FAILURE;
		} catch (NumberFormatException e) {
			this.addActionError(this.getText("Error.addressBookManager.unknownRecipient",new String[]{this.getActualRecipient()}));
			return "unknownRecipient";
		}
		return SUCCESS;
	}
	
	@Override
	public String removeRecipient() {
		try {
			this.setSearchedReceivers(this.getAddressBookManager().searchContacts(this.getCurrentUser(), this.getText()));
			this.getSelectedReceivers().remove(this.getActualReceiver());
		} catch (Exception e) {
			ApsSystemUtils.logThrowable(e, this, "removeRecipient");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Restituisce una lista piatta popolata con lo username degli utenti trovati
	 */
	@Override
	public String searchRecipients() {
		try {
			List<AddressBookContact> tmp = this.getAddressBookManager().searchContacts(this.getCurrentUser(), this.getText());
			this.setSearchedReceivers(tmp);
		} catch (ApsSystemException e) {	
			ApsSystemUtils.logThrowable(e, this, "searchRecipients");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public AddressBookContact getContact(String username) {
		try {
			return this.getAddressBookManager().loadContactByUsername(this.getCurrentUser(), username);
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "getContact");
			return null;
		} 
	}
	
	public String getText() {
		return _text;
	}
	public void setText(String text) {
		this._text = text;
	}
	
	public String getActualReceiver() {
		return _actualReceiver;
	}
	public void setActualReceiver(String actualReceiver) {
		this._actualReceiver = actualReceiver;
	}
	
	public Set<String> getSelectedReceivers() {
		return _selectedReceivers;
	}
	public void setSelectedReceivers(Set<String> selectedReceivers) {
		this._selectedReceivers = selectedReceivers;
	}
	
	public List<AddressBookContact> getSearchedReceivers() {
		return _searchedReceivers;
	}
	public void setSearchedReceivers(List<AddressBookContact> searchedReceivers) {
		this._searchedReceivers = searchedReceivers;
	}
	
	public String getActualRecipient() {
		return _actualRecipient;
	}
	public void setActualRecipient(String actualRecipient) {
		this._actualRecipient = actualRecipient;
	}
	
	protected INewMessageActionHelper getNewMessageHelper() {
		return _newMessageHelper;
	}
	public void setNewMessageHelper(INewMessageActionHelper newMessageHelper) {
		this._newMessageHelper = newMessageHelper;
	}
	
	protected IAddressBookManager getAddressBookManager() {
		return _addressBookManager;
	}
	public void setAddressBookManager(IAddressBookManager addressBookManager) {
		this._addressBookManager = addressBookManager;
	}
	
	private String _text = null;
	// contiene la lista dei contatti selezionati tramite checkbox
	private Set<String> _selectedReceivers = new HashSet<String>();
	// contiene la lista dei contatti generati dalla ricerca
	private List<AddressBookContact> _searchedReceivers;
	// contiene lo username dell'utente sul quale si sta agendo
	private String _actualReceiver;
	// contiene il tipo di recipient al quale si devono aggiungere i dati
	private String _actualRecipient;
	
	// Ignorato nell'implementazione corrente
	private INewMessageActionHelper _newMessageHelper;	
	private IAddressBookManager _addressBookManager;
	
}