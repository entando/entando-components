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