/*
 * Copyright 2013-Present Entando Corporation (http://www.entando.com) All rights reserved.
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