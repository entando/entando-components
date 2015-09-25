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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail;

import javax.mail.Store;

/**
 * Interfaccia base per tutte le classi action del modulo WebMail.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IWebMailBaseAction {
	
	/**
	 * Setta lo store all'interno dell'azione.
	 * Il metodo viene chiamato dall'interceptor WebMailStoreFactoryInterceptor in fase di 
	 * inizializzazione; in tal modo la classe Action apposita è predisposta in partenza ad 
	 * effettuare una qualunque operazione all'interno della MailBox.
	 * Lo store viene poi chiuso direttamente dallo stesso interceptor una volta evasa la 
	 * richiesta nella sua totalità (Azione eseguita e view/jsp completamente renderizzata).
	 * @param store Lo store.
	 */
	public void setStore(Store store);
	
	/**
	 * Effettua la chiusura di cartelle eventualmente aperte.
	 * Il metodo viene chiamato dall'interceptor WebMailStoreFactoryInterceptor in fase di 
	 * chiusura della richiesta; in tal modo vengono rilasciate le cartelle 
	 * eventualmente aperte all'interno della MailBox.
	 * Per consentire questa operazione è necessario che ogni classe action tenga traccia di tutte 
	 * le cartelle aperte nel corso della elaborazione per permetterne una chiusura controllate 
	 * una volta finita l'elaborazione.
	 */
	public void closeFolders();
	
}