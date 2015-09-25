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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message;

import com.agiletec.plugins.jpwebmail.apsadmin.webmail.IWebMailBaseAction;

/**
 * Interfaccia base per le classi action che gestiscono le operazioni 
 * per la gestione base di un nuovo messaggio singolo.
 * @version 1.0
 * @author E.Santoboni
 */
public interface INewMessageAction extends IWebMailBaseAction {
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio vuoto.
	 * @return Il codice del risultato dell'azione.
	 */
	public String newMessage();
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio ottenuto dal reply di un'altra mail.
	 * I parametri necessari per effettuare l'operazione vengono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String reply();
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio ottenuto dal replyAll di un'altra mail.
	 * I parametri necessari per effettuare l'operazione vengono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String replyAll();
	
	/**
	 * Effettua la richiesta di apertura di un nuovo messaggio ottenuto dal forward di un'altra mail.
	 * I parametri necessari per effettuare l'operazione vengono inseriti nella richiesta corrente.
	 * @return Il codice del risultato dell'azione.
	 */
	public String forward();
	
	/**
	 * Effettua la richiesta di spedizione di una mail.
	 * @return Il codice del risultato dell'azione.
	 */
	public String send();
	
}
