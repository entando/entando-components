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
package com.agiletec.plugins.jpnewsletter.apsadmin.newsletter;

import java.util.List;

public interface INewsletterFinderAction {
	
	/**
	 * Restituisce la lista identificativi di contenuti che deve essere erogata dall'interfaccia di 
	 * visualizzazione dei contenuti. La lista deve essere filtrata secondo i parametri di ricerca impostati.
	 * @return La lista di contenuti che deve essere erogata dall'interfaccia di 
	 * visualizzazione dei contenuti.
	 */
	public List<String> getContents();
	
	/**
	 * Esegue la rimozione del contenuto alla coda della newsletter.
	 * @return Il codice del risultato dell'azione.
	 */
	public String removeFromQueue();
	
	/**
	 * Esegue l'aggiunta del contenuto alla coda della newsletter.
	 * @return Il codice del risultato dell'azione.
	 */
	public String addToQueue();
	
}