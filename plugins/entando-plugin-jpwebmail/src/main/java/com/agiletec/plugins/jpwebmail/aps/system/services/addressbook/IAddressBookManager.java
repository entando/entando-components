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