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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message;

import com.agiletec.plugins.jpwebmail.apsadmin.webmail.IWebMailBaseAction;

/**
 * Interfaccia base per le classi action che gestiscono le operazioni 
 * per la gestione base di un nuovo messaggio singolo.
 * @version 1.0
 * @author E.Santoboni
 */
public interface INewMessageAction extends IWebMailBaseAction {
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio vuoto.
	 * @return Il codice del risultato dell'azione.
	 */
	public String newMessage();
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio ottenuto dal reply di un'altra mail.
	 * I parametri necessari per effettuare l'operazione vengono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String reply();
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio ottenuto dal replyAll di un'altra mail.
	 * I parametri necessari per effettuare l'operazione vengono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String replyAll();
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio ottenuto dal forward di un'altra mail.
	 * I parametri necessari per effettuare l'operazione vengono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String forward();
	
	/**
	 * Effettua la richiesta di spedizione di una mail.
	 * @return Il codice del risultato dell'azione.
	 */
	public String send();
	
}
