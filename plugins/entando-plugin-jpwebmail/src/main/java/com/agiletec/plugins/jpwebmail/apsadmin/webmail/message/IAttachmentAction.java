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

/**
 * Interfaccia base per le classi action che gestiscono le operazioni 
 * per la gestione degli attachment (aggiunta/rimozione) in un nuovo messaggio singolo.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IAttachmentAction {
	
	/**
	 * Effettua la richiesta di aggiunta di un nuovo attachment al messaggio corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String addAttachment();
	
	/**
	 * Effettua la richiesta di rimozione di un attachment dal messaggio corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeAttachment();
	
}