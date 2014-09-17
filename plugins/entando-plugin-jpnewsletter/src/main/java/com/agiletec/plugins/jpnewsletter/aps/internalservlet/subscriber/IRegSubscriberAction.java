/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpnewsletter.aps.internalservlet.subscriber;

/**
 * Interfaccia base per la classe SubscriberAction per la gestione dei
 * sottoscritti al servizio generico di newsletter in Front-End.
 * 
 * @author A.Turrini
 * @version 1.0, 06/08/10
 * @since 1.6
 */
public interface IRegSubscriberAction {

	/**
	 * Aggiunge una sottoscrizione al servizio di newsletter.
	 * 
	 * @return Stringa risultato dell'aggiunta della sottoscrizione
	 */
	public String addSubscription();

	/**
	 * Attiva la sottoscrizione al servizio di newsletter.
	 * 
	 * @return Stringa risultato dell'attivazione della sottoscrizione
	 */
	public String activateSubscription();

	/**
	 * Predispone la cancellazione di una sottoscrizione dal servizio di
	 * newsletter.
	 * 
	 * @return Stringa risultato dell'operazione
	 */
	public String trashSubscription();

	/**
	 * Cancella definitivamente una sottoscrizione al servizio di newsletter.
	 * 
	 * @return Stringa risultato della cancellazione della sottoscrizione
	 */
	public String deleteSubscription();

}
