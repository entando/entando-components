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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Interfaccia base per la classe helper delegata alla restituzione 
 * dell'indirizzo email dell'utente specificato.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IUserMailHelper {
	
	/** 
	 * Restituisce la mail associata all'utente dato.
	 * L'indirizzo email può essere restituito nella forma semplice (solo indirizzo email) o 
	 * nella froma completa (nome con indirizzo email rachiuso tra parentesi "<>")
	 * @param user L'utente cui restituire l'indirizzo email corrispondente.
	 * @return L'indirizzo email dell'utente specificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public String getEmailAddress(UserDetails user) throws ApsSystemException;
	
}