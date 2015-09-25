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