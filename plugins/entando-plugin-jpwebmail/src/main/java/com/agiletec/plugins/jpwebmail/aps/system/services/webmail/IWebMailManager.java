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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Interfaccia dei servizi gestore della WebMail.
 * @author E.Santoboni
 */
public interface IWebMailManager {
	
	/**
	 * Restituisce la configurazione del servizio WebMail.
	 * @return La configurazione del servizio WebMail.
	 * @throws ApsSystemException In caso di errore.
	 * @deprecated use getConfiguration()
	 */
	public WebMailConfig loadConfig() throws ApsSystemException;
	
	public WebMailConfig getConfiguration() throws ApsSystemException;
	
	/**
	 * Aggiorna la configurazione del servizio WebMail.
	 * @param config La configurazione del servizio WebMail.
	 * @throws ApsSystemException In caso di errore.
	 */
	public void updateConfig(WebMailConfig config) throws ApsSystemException;
	
	/**
	 * Inizializza la connessione e restituisce lo Store corrispondente.
	 * @param username La username dell'utenza proprietario della casella di posta.
	 * @param password La password dell'utenza proprietario della casella di posta.
	 * @return Lo Store della casella di posta dell'utente.
	 * @throws ApsSystemException In caso di errore nella connessione.
	 */
	public Store initInboxConnection(String username, String password) throws ApsSystemException;
	
	/**
	 * Effettua la chiusura dello store.
	 * @param store Lo store da chiudere.
	 */
	public void closeConnection(Store store);
	
	/**
	 * Effettua la spedizione di una mail.
	 * @param msg Il messaggio da spedire.
	 * @param username Lo username dell'utente mittente.
	 * @param password La password dell'utente mittente.
	 * @throws ApsSystemException In caso di errore nella spedizione.
	 */
	public void sendMail(MimeMessage msg, String username, String password) throws ApsSystemException;
	
	/**
	 * Crea e restituisce un muovo messaggio vuoto.
	 * @param username Lo username dell'utente mittente.
	 * @param password La password dell'utente mittente.
	 * @return Il nuovo messaggio vuoto.
	 * @throws ApsSystemException In caso di errore.
	 */
	public MimeMessage createNewEmptyMessage(String username, String password) throws ApsSystemException;
	
	/**
	 * Restituisce il nome completo (comprensivo delle sottocartelle) della cartella 
	 * con le funzioni da cestino.
	 * @return Il nome completo (comprensivo delle sottocartelle) 
	 * della cartella con le funzioni da cestino.
	 */
	public String getTrashFolderName();
	
	/**
	 * Restituisce il nome completo (comprensivo delle sottocartelle) della cartella 
	 * nella quale devono essere salvati i messaggi inviati.
	 * @return Il nome completo della cartella 
	 * nella quale devono essere salvati i messaggi inviati.
	 */
	public String getSentFolderName();
	
	/**
	 * Restituisce il nome del dominio gestito dalla webmail.
	 * Il metodo viene utilizzato per costruire l'indirizzo e-mail dell'utente corrente, dato dalla concatenazione 
	 * del campo username e del nome dominio; l'operazione viene effettuata CurrentUserHelper di default del sistema.
	 * @return Il nome del dominio.
	 */
	public String getDomainName();
	
	public static final String CONTENTTYPE_TEXT_PLAIN = "text/plain";
	public static final String CONTENTTYPE_TEXT_HTML = "text/html";
	
}