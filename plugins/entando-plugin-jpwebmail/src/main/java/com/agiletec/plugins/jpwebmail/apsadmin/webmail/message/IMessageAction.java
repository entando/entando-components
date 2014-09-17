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
 * per la visualizzazione di un messaggio singolo.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IMessageAction extends IWebMailBaseAction {
	
	/**
	 * Effettua la richiesta di apertura di un messaggio singolo.
	 * I parametri necessari per l'individuazione del messaggio sono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String openMessage();
	
	/**
	 * Effettua la richiesta di apertura di un attachment di un messaggio singolo.
	 * I parametri necessari per l'individuazione dell'attachment sono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String openAttachment();
	
}
