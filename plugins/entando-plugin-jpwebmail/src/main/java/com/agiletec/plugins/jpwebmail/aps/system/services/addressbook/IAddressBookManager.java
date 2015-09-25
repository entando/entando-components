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
package com.agiletec.plugins.jpwebmail.aps.system.services.addressbook;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpwebmail.aps.system.services.addressbook.model.AddressBookContact;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public interface IAddressBookManager {
	
	/**
	 * Ottiene tutti i contatti dalla rubrica dell'utente dato.
	 * @param user L'utente per cui recuperare il contatto.
	 * @return Tutti i contatti della rubrica.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<AddressBookContact> getContacts(UserDetails user) throws ApsSystemException;
	
	/**
	 * Ricerca dalla rubrica personale dell'utente corrente un contatto in base al nome e ad un testo di ricerca.
	 * @param user L'utente per cui ricercare i contatti.
	 * @param text Il testo per la ricerca sui contatti.
	 * @return I contatti filtrati.
	 * @throws ApsSystemException In caso di errore.
	 */
	public List<AddressBookContact> searchContacts(UserDetails user, String text) throws ApsSystemException;
	
	/**
	 * Carica un utente completo dato il suo username univoco
	 * @param user L'utente per cui ricercare i contatti.
	 * @param username Il nome dell'utente di cui si desidera il contatto della rubrica.
	 * @return Il contatto dell'utente di username dato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public AddressBookContact loadContactByUsername(UserDetails user, String username) throws ApsSystemException;
	
}