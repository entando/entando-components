/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content;

import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentState;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentSuspendMove;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentThreadConfig;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;

/**
 * Interfaccia che espone i servizi necessari al thread di
 * pubblicazione/sospensione automatica
 */
public interface IContentSchedulerManager {

	/**
	 * Restituisce gli identificatori dei contenuti da pubblicare
	 * 
	 * @return
	 * @throws ApsSystemException
	 */
	public List<String> getContentIdToPublish() throws ApsSystemException;

	/**
	 * Restituisce una lista di identificatori dei contenuti da sospendere o
	 * spostare e l'azione da eseguire
	 * 
	 * @return
	 * @throws ApsSystemException
	 */
	public List<ContentSuspendMove> getContentAttrDataFine() throws ApsSystemException;

	/**
	 * Restituisce la configurazione del thread (gruppi, utenti, formato mail..)
	 * 
	 * @return
	 */
	public ContentThreadConfig getConfig();

	public void updateConfig(ContentThreadConfig config) throws ApsSystemException;

	public void sendMailWithResults(List<ContentState> publishedContents, List<ContentState> suspendedContents, List<ContentState> moveContents, Date startJobDate, Date endJobDate)
			throws ApsSystemException;

	/**
	 * Return the desired system parameter
	 *
	 * @param paramName
	 * @return
	 */
	public String getSystemParam(String paramName);

	/**
	 * Rimuove un contenuto OnLine. L'operazione non cancella il contenuto ma ne
	 * rimuove la possibilita' di visualizzazione nel portale. Il contenuto
	 * ancora presente verrà messo in stato cancellato.
	 * 
	 * @param content
	 * Il contenuto da rimuovere.
	 * @param updateLastModified
	 * Se impostato a true aggiorna la data di ultima modifica
	 * @throws ApsSystemException
	 * in caso di errore nell'accesso al db.
	 */
	public void removeOnLineContent(Content content, boolean updateLastModified) throws ApsSystemException;

	/**
	 * Effettua l'aggiornamento di un contenuto sul DB senza modificarne stato e
	 * data di ultima modifica. Il metodo è stato aggiunto per procedure di tipo
	 * sheduler nelle quali potrebbe essere necessario non modificare la data di
	 * ultima modifica(lastModified) e lo stato del contenuto
	 * 
	 * @param content
	 * Il contenuto da modificare.
	 * @param updateDate
	 * Se impostato a true incrementa la versione
	 * @param updateLastModified
	 * Se impostato a true aggiorna la data di ultima modifica
	 * @throws ApsSystemException
	 */
	public void moveOnLineContent(Content content, boolean updateDate, boolean updateLastModified) throws ApsSystemException;

}
