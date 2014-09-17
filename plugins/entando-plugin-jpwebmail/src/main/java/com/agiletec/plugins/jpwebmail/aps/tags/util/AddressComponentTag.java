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
package com.agiletec.plugins.jpwebmail.aps.tags.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * Questo tag prende in input una stringa nella forma "nome" + "cognome" + "email" che vengono caricati in un oggetto di tipo 'AddressComponents'
 * in modo da poter essere ottenuti singolarmente. È possibile forzare la dimensione massima dei singoli elementi con 'roundTo', oppure usare un
 * separatore apposito 'separator' così come utilizzare un default value 'defaultValue'.
 *
 */
public class AddressComponentTag extends TagSupport {
	
	public int doStartTag() throws JspException {
		AddressComponents result = null;
		try {
			if (null == getSeparator()) {
				result = new AddressComponents(this.getFullAddressString(), getRoundTo(), getDefaultValue());
			} else {
				result = new AddressComponents(this.getFullAddressString(), getRoundTo(), getSeparator(), getDefaultValue());
			}		
			this.pageContext.setAttribute(_ctxName, result);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Errore inizializzazione tag", t);
		}
		return SKIP_BODY;
	}
	
	public String getCtxName() {
		return _ctxName;
	}
	public void setCtxName(String ctxName) {
		this._ctxName = ctxName;
	}
	
	public String getFullAddressString() {
		return _fullAddressString;
	}
	public void setFullAddressString(String fullAddressString) {
		this._fullAddressString = fullAddressString;
	}
	
	public String getSeparator() {
		return _separator;
	}
	public void setSeparator(String separator) {
		this._separator = separator;
	}
	
	public Integer getRoundTo() {
		return _roundTo;
	}
	public void setRoundTo(Integer roundTo) {
		this._roundTo = roundTo;
	}
	
	public String getDefaultValue() {
		return _defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this._defaultValue = defaultValue;
	}
	
	/**
	 * nome con il quale l'oggetto viene posto nel contesto della pagina
	 */
	private String _ctxName;
	/**
	 * separatore usato per dividere il primo nome dal secondo
	 */
	private String _separator;
	/**
	 * Grandezza massima delle stringhe caricate nell'oggetto 
	 */
	private Integer _roundTo;
	/**
	 * valore di default nel caso i campi siano tutti nulli
	 */
	private String _defaultValue;
	/**
	 * Stringa contenente nome, cognome e indirizzo email
	 */
	private String _fullAddressString;
	
}