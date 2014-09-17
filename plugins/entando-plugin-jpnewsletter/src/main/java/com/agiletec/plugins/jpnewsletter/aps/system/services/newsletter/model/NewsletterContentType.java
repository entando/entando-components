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