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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter;

import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterReport;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

/**
 * Interfaccia base per i Data Access Object delegati alle operazioni 
 * sugli oggetti contenenti le informazioni di spedizione newsletter.
 * @author E.Mezzano, A.Turrini
 */
public interface INewsletterDAO {
	
	/**
	 * Aggiunge il contenuto di id dato alla coda della newsletter.
	 * @param contentId L'id del contenuto da aggiungere alla coda.
	 */
	public void addContentToQueue(String contentId);
	
	/**
	 * Rimuove il contenuto di id dato dalla coda della newsletter.
	 * @param contentId L'id del contenuto da rimuovere dalla coda.
	 */
	public void deleteContentFromQueue(String contentId);
	
	/**
	 * Recupera la lista degli id dei contenuti in coda per la newsletter.
	 * @return La coda della newsletter, contenente gli id dei contenuti da inviare.
	 */
	public List<String> loadContentQueue();
	
	/**
	 * Rimuove dalla coda gli id di contenuto dati.
	 * @param queue La lista di id di contenuto da rimuovere dalla newsletter.
	 */
	public void cleanContentQueue(List<String> queue);
	
	/**
	 * Aggiunge al database le newsletter inviate.
	 * @param newsletterReport Il report della newsletter inviata.
	 */
	public void addNewsletterReport(NewsletterReport newsletterReport);
	
	/**
	 * Recupera il report di una newsletter inviata.
	 * @return Il report della newsletter inviata.
	 */
	public NewsletterContentReportVO loadContentReport(String contentId);
	
	/**
	 * Recupera la lista completa degli id dei contenuti inviati tramite newsletter.
	 * @return Gli id dei contenuti inviati tramite newsletter.
	 */
	public List<String> loadSentContentIds();
	
	/**
	 * Restituisce true se risulta inviato il contenuto di id dato, false altrimenti.
	 * @param contentId L'id del contenuto dato.
	 * @return true se risulta inviato il contenuto di id dato, false altrimenti.
	 */
	public boolean existsContentReport(String contentId);
	
	/**
	 * Carica la lista dei sottoscritti al servizio di newsletter
	 * 
	 * @return la lista dei sottoscritti.
	 * @throws ApsSystemException 
	 */
	public List<Subscriber> loadSubscribers() throws ApsSystemException;
	
	/**
	 * Carica la lista dei sottoscritti al servizio di newsletter per indirizzo e-mail e stato di attivazione
	 * 
	 * @param mailAddress l'indirizzo e-mail da ricercare
	 * @param lo stato di attivazione da ricercare
	 * @return la lista dei sottoscritti.
	 * @throws ApsSystemException 
	 */
	public List<Subscriber> searchSubscribers(String mailAddress, Boolean active) throws ApsSystemException;
	
	/**
	 * Carica un sottoscritto al servizio di newsletter
	 * 
	 * @param mailAddress l'indirizzo e-mail di un sottoscritt
	 * @return il sottoscritto al servizio, se presente
	 * @throws ApsSystemException 
	 */
	public Subscriber loadSubscriber(String mailAddress) throws ApsSystemException;
	
	/**
	 * Aggiunge una sottoscrizione al servizio
	 * Se il token è diverso da null aggiunge il token.
	 * 
	 * @param subscriber The subscriber to add.
	 * @param token The token of the subscription.
	 * @throws ApsSystemException
	 */
	public void addSubscriber(Subscriber subscriber, String token) throws ApsSystemException;
	
	/**
	 * Aggiorna una sottoscrizione al servizio.
	 * Se il token è diverso da null aggiorna/aggiunge il token.
	 * 
	 * @param subscriber The subscriber to add.
	 * @param token The token of the subscription.
	 * @throws ApsSystemException
	 */
	public void updateSubscriber(Subscriber subscriber, String token) throws ApsSystemException;
	
	/**
	 * Cancella una sottoscrizione al servizio
	 * 
	 * @param mailAddress l'indirizzo e-mail da cancellare
	 * @return void
	 * @throws ApsSystemException  
	 */
	public void deleteSubscriber(String mailAddress) throws ApsSystemException;
	
	/**
	 * Attiva una sottoscrizione al servizio
	 * 
	 * @param mailAddress l'indirizzo e-mail da attivare
	 * @return void
	 * @throws ApsSystemException  
	 */
	public void activateSubscriber(String mailAddress) throws ApsSystemException;
	
	/**
	 * Returns the address from the associated token, if exist.
	 * */
	public String getAddressFromToken(String token);
	
	/**
	 * Remove from database a token.
	 * */
	public void removeToken(String token);
	
	/**
	 * Delete old request account, which has not been activated.
	 * */
	public void cleanOldSubscribers(Date date);
	
}