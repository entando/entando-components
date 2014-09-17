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

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterContentReportVO;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterSearchBean;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

/**
 * Interfaccia del Servizio gestore delle newsletter.
 * @author E.Santoboni, A.Turrini
 */
public interface INewsletterManager {
	
	/**
	 * Restituisce la configurazione del servizio newsletter.
	 * @return La configurazione del servizio newsletter.
	 */
	public NewsletterConfig getNewsletterConfig();
	
	/**
	 * Aggiorna la configurazione del servizio newsletter.
	 * @param config La configurazione del servizio newsletter.
	 * @throws ApsSystemException In caso di errore nella generazione dei dati.
	 */
	public void updateNewsletterConfig(NewsletterConfig config) throws ApsSystemException;
	
	/**
	 * Aggiunge il contenuto di id dato alla coda della newsletter.
	 * @param contentId L'id del contenuto da aggiungere alla coda.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	public void addContentToQueue(String contentId) throws ApsSystemException;
	
	/**
	 * Rimuove il contenuto di id dato dalla coda della newsletter.
	 * @param contentId L'id del contenuto da rimuovere dalla coda.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	public void removeContentFromQueue(String contentId) throws ApsSystemException;
	
	/**
	 * Recupera la lista degli id dei contenuti in coda per la newsletter.
	 * @return La coda della newsletter, contenente gli id dei contenuti da inviare.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	public List<String> getContentQueue() throws ApsSystemException;
	
	/**
	 * Returns true if the content has been sent, false otherwise.
	 * @param contentId The id of the content.
	 * @return true if the content has been sent, false otherwise.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	public boolean existsContentReport(String contentId) throws ApsSystemException;
	
	/**
	 * Recupera la lista completa degli id dei contenuti inviati tramite newsletter.
	 * @return Gli id dei contenuti inviati tramite newsletter.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	public List<String> getSentContentIds() throws ApsSystemException;
	
	/**
	 * Recupera il report di una newsletter inviata.
	 * @return Il report della newsletter inviata.
	 */
	public NewsletterContentReportVO getContentReport(String contentId) throws ApsSystemException;
	
	/**
	 * Recupera gli id dei contenuti inviabili tramite newsletter in base ai parametri di filtro dati.
	 * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca deve essere effettuata.
	 * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente la lista. 
	 * Se la collezione è vuota o nulla, gli identificativi di contenuto erogati saranno 
	 * relativi al gruppo definito "ad accesso libero". Nel caso nella collezione sia presente 
	 * il codice del gruppo degli amministratori, non sarà applicato alcun il filtro sul gruppo.
	 * @param searchBean L'insieme dei filtri relativi alla newsletter.
	 * @return La lista degli id dei contenuti cercati.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	public List<String> loadNewsletterContentIds(EntitySearchFilter[] filters, 
			Collection<String> userGroupCodes, NewsletterSearchBean searchBean) throws ApsSystemException;
	
	/**
	 * Esegue l'invio della newsletter.
	 * @throws ApsSystemException In caso di errore nell'accesso al db.
	 */
	public void sendNewsletter() throws ApsSystemException;
	
	/**
	 * @param content Il contenuto per cui inviare la newsletter.
	 * @param html Indica se il testo deve essere generato conl modello html.
	 * @return Il testo formattato
	 * @throws ApsSystemException In caso di errore nella generazione del testo della mail.
	 */
	public String buildMailBody(Content content, boolean html) throws ApsSystemException;
	
	
	/**
	 * Carica la lista degli indirizzi e-mail degli utenti sottoscritti al
	 * servizio
	 * 
	 * @return la lista degli utenti sottoscritti al servizio
	 * @throws ApsSystemException
	 */
	public List<Subscriber> loadSubscribers() throws ApsSystemException;
	
	/**
	 * Carica la lista degli indirizzi e-mail degli utenti sottoscritti al
	 * servizio
	 * 
	 * @param insertedMailAddress indirizzo e-mail da ricercare
	 * @param insertedActive stato di attivazione da ricercare
	 * @return la lista degli utenti sottoscritti al servizio
	 * @throws ApsSystemException
	 */
	public List<Subscriber> searchSubscribers(String mailAddress, Boolean active) throws ApsSystemException;
	
	/**
	 * Carica un sottoscritto al servizio di newsletter
	 * 
	 * @param mailAddress l'indirizzo e-mail di un sottoscritto
	 * @return il sottoscritto al servizio, se presente
	 * @throws ApsSystemException
	 */
	public Subscriber loadSubscriber(String mailAddress) throws ApsSystemException;
	
	/**
	 * Aggiunge una richiesta di sottoscrizione al servizio
	 * 
	 * @param mailAddress The address of the subscription.
	 * @throws ApsSystemException
	 */
	public void addSubscriber(String mailAddress) throws ApsSystemException;
	
	/**
	 * Aggiorna una richiesta di sottoscrizione resettando il token.
	 * 
	 * @param mailAddress The address of the subscription.
	 * @throws ApsSystemException
	 */
	public void resetSubscriber(String mailAddress) throws ApsSystemException;
	
	/**
	 * Cancella definitivamente una sottoscrizione al servizio
	 * @param mailAddress The address of the subscription.
	 * @throws ApsSystemException
	 */
	public void deleteSubscriber(String mailAddress) throws ApsSystemException;
	
	/**
	 * Attiva una richiesta di sottoscrizione al servizio
	 * 
	 * @param mailAddress The address of the subscription.
	 * @param token The token to activate the subscription.
	 * @throws ApsSystemException
	 */
	public void activateSubscriber(String mailAddress, String token) throws ApsSystemException;
	
	/**
	 * Load address from associated token if exist
	 * 
	 * @param token The token of the subscription.
	 * */
	public String getAddressFromToken(String token) throws ApsSystemException;
	
	/**
	 * Remove the old not activated subscribers
	 * */
	public void cleanOldSubscribers() throws ApsSystemException;
	
	/**
	 * Verifica se l'indirizzo e-mail di chi richiede il servizio è già registrato con un profilo utente Cittadino.
	 * 
	 * @param mailAddress l'indirizzo e-mail del visitatore che richiede la sottoscrizione
	 * @return un valore booleano risultato della ricerca
	 * @throws ApsSystemException 
	 */
	public Boolean isAlreadyAnUser(String mailAddress) throws ApsSystemException;
	
}