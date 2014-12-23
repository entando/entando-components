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
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model;

/**
 * Oggetto contenente la configurazione, per il servizio di newsletter, di un singolo Tipo di Contenuto.
 * @author E.Mezzano
 */
public class NewsletterContentType {
	
	/**
	 * Restituisce il codice del tipo di contenuto.
	 * @return Il codice del tipo di contenuto.
	 */
	public String getContentTypeCode() {
		return _contentTypeCode;
	}
	
	/**
	 * Imposta il codice del tipo di contenuto.
	 * @param contentTypeCode Il codice del tipo di contenuto.
	 */
	public void setContentTypeCode(String contentTypeCode) {
		this._contentTypeCode = contentTypeCode;
	}
	
	/**
	 * Restituisce il codice del modello per testo semplice relativo al tipo di contenuto.
	 * @return Il codice del modello per testo semplice relativo al tipo di contenuto.
	 */
	public int getSimpleTextModel() {
		return _simpleTextModel;
	}
	/**
	 * Imposta il codice del modello per testo semplice relativo al tipo di contenuto.
	 * @param simpleTextModel Il codice del modello per testo semplice relativo al tipo di contenuto.
	 */
	public void setSimpleTextModel(int simpleTextModel) {
		this._simpleTextModel = simpleTextModel;
	}
	
	/**
	 * Imposta il codice del modello per testo html relativo al tipo di contenuto.
	 * @return Il codice del modello per testo html relativo al tipo di contenuto.
	 */
	public int getHtmlModel() {
		return _htmlModel;
	}
	
	/**
	 * Restituisce il codice del modello per testo html relativo al tipo di contenuto.
	 * @param htmlModel Il codice del modello per testo html relativo al tipo di contenuto.
	 */
	public void setHtmlModel(int htmlModel) {
		this._htmlModel = htmlModel;
	}
	
	/**
	 * Restituisce true se la configurazione prevede una mail in formato html.
	 * @return true se la configurazione prevede una mail in formato html.
	 */
	public boolean hasHtml() {
		return this.getHtmlModel()>0;
	}
	
	private String _contentTypeCode;
	private int _simpleTextModel;
	private int _htmlModel;
	
}