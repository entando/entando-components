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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * Data una stringa del tipo nome + cognome + < + email + > cerca di estrarre tutti gli elementi in maniera ordinata e inizializzare 
 * la classe.<br />
 */
public class AddressComponents {
	
	/**
	 * Scompone in token singoli un indirizzo completo cercando di estrapolare nome cognome e email<br />
	 * 
	 * @param length grandezza massima delle stringhe da restituire
	 * @param fullAddress indirizzo da analizzare nella forma "nome + cognome + \<email\>"
	 */
	public AddressComponents(String fullAddress, Integer length, String defaultValue) {
		if (null == fullAddress) {
			return;
		}
		_defaults = defaultValue;
		_faulty = false;
		_separator = " ";
		_delimiter = "...";
		if (null != length) {
			_length = length;
		} else {
			length = -1;
		}
		tokenizeAddress(fullAddress);
	}
	
	/**
	 * Scompone in token singoli un indirizzo completo cercando di estrapolare nome cognome e email, limitando il tutto alla
	 * lunghezza desiderata.
	 * 
	 * @param fullAddress indirizzo da analizzare nela forma "nome + cognome + \<email\>"
	 * @param separator usa la stringa data per separare gli argomenti nello username
	 */
	public AddressComponents(String fullAddress, Integer length, String separator, String defaultValue) {
		if (null == fullAddress) {
			return;
		}
		_faulty = false;
		_delimiter = "...";
		_defaults = defaultValue;
		if (null != length) {
			_length = length;
		} else {
			length = -1;
		}
		if (null == separator) {
			_separator = " ";
		} else {
			_separator = separator;
		}		
		tokenizeAddress(fullAddress);
	}
	
	public String extractEmail(String string) {
		String result = null;
		try {
			Pattern filenamePattern = Pattern.compile("<(.*)>");
			Matcher codeMatcher = filenamePattern.matcher("");
			codeMatcher.reset(string);
			if (codeMatcher.find()) {
				result = codeMatcher.group(1);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "extractEmail");
			_faulty = true;
		}
		return result;
	}
	
    /**
     * Cerca di estrapolare l'indirizzo email completo; inoltre salva l'utenza della mail e il dominio.
     * in campi separati, rispettivamente 'account' e 'domain'
     * @param email 
     */
	public void evaluateEmailFields(String email) {		
		try {
			if (email.contains(">") && email.contains("<")) {
				email = extractEmail(email);
			} else {
				if (email.contains(">") && email.contains("<")) {
					_faulty = true;
				}
			}
			String[] tokens = email.split("@");
			if (tokens.length != 2) {
				_faulty = true;
			}
			setAccount(tokens[0]);
			String domain = tokens[1];
			if (!domain.contains(".")) {
				_faulty = true;				
			} else {
				setDomain(domain);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "evaluateEmailFields");
			_faulty = true;
//			email = null;
		}
		if (null != email && email.length() > 0) {
			if (null == _email) {
				_email = email;
			} else {
				_email+=_separator+email;
			}
		}
	}
	
	/**
	 * Analizza la stringa nel suo complesso. Tutto quello che non è indirizzo email viene unito a 'fullname', eventualmente usando il giusto
	 * separatore. Inoltre viene valorizzato 'existing' che restituisce il primo campo non null trovato fra 'nome' e 'email'. Qualora specificato
	 * viene assegnato il valore di default a tutti i campi con l'esclusione di 'account' e 'domain' nel caso in cui la stringa sia nulla.
	 * @param fullAddress
	 */
	public void tokenizeAddress(String fullAddress) {
		this.setTokens(fullAddress.split(" "));
		// find email address
		for (int scan = 0; scan < getTokens().length; scan++) {
			String current = _tokens[scan];
			if (current.contains("@")) {
				evaluateEmailFields(current);
				_tokens[scan] = null;
			}
		}
		for (int scan = 0; scan < getTokens().length; scan++) {
			String current = _tokens[scan];
			if (null != current) {
				if (_fullname == null) {
					_fullname=current;
				} else {
					_fullname+=_separator+current;
				}
			}
		}
		if (null != _fullname && _fullname.length() > 0) {
			_existing=_fullname;
		} else {
			if (null != _email && _email.length() > 0) {
				_existing=_email;
			} else {
				_existing=_defaults;
				_fullname=_defaults;
				_email=_defaults;
			}
		}
	}
	
	public boolean isEmpty() {
		return (null != _fullname && _fullname.length() > 0 && 
				null != _email && _email.length() > 0 ? true:false);
	}
	
	/**
	 * Restituisce la stringa di lunghezza desiderata
	 * @param element la stringa da trattare
	 * @return la stringa della lunghezza opportuna
	 */
	public String roundedElement(String element) {
		if (null == element) {
			return null;
		}
		if (_length < 1 || _length >= element.length()) {
			return element;
		}
		return element.substring(0,_length)+_delimiter;
	}
	
	public String getEmail() {
		return roundedElement(_email);
	}
	public void setEmail(String email) {
		this._email = email;
	}	
	
	public boolean isFaulty() {
		return _faulty;
	}
	
	public String[] getTokens() {
		return _tokens;
	}
	public void setTokens(String[] tokens) {
		this._tokens = tokens;
	}
	
	public void setFullname(String fullname) {
		this._fullname = fullname;
	}
	
	public String getFullname() {
		return roundedElement(_fullname);
	}
	
	public void setSeparator(String separator) {
		this._separator = separator;
	}
	
	public String getSeparator() {
		return _separator;
	}
	
	public String getAccount() {
		return roundedElement(_account);
	}
	public void setAccount(String account) {
		this._account = account;
	}
	
	public String getDomain() {
		return _domain;
	}
	public void setDomain(String domain) {
		this._domain = domain;
	}
	
	public String getDelimiter() {
		return _delimiter;
	}
	public void setDelimiter(String delimiter) {
		this._delimiter = delimiter;
	}
	
	public int getLength() {
		return _length;
	}
	public void setLength(int length) {
		this._length = length;
	}
	
	public String getExisting() {
		return roundedElement(_existing);
	}
	public void setExisting(String existing) {
		this._existing = existing;
	}
	
	public String getDefaults() {
		return _defaults;
	}
	public void setDefaults(String defaults) {
		this._defaults = defaults;
	}
	
	/**
	 * Nome e cognome separati dallo spazio (default) o dal separatore indicato.
	 */
	private String _fullname;
	/**
	 * Indirizzo email. Qualora sia passato fra apici '<' e '>' questi vengono eliminati.
	 */
	private String _email;
	/**
	 * Nome dell'utenza di posta elettronica
	 */
	private String _account;
	/**
	 * dominio della posta elettronica
	 */
	private String _domain;
	/**
	 * Restituisce il primo degli elementi non nulli tra 'fullname' e 'email'
	 */
	private String _existing;
	/**
	 * l'array contenente tutti i tokens
	 */
	private String[] _tokens;
	/**
	 * String usata per separare tutto quello che non è email; default è lo spazio bianco
	 */
	private String _separator;
	/**
	 * Stringa postposta alla stringa troncata
	 */
	private String _delimiter;
	/**
	 * lunghezza massima ammessa nelle stringhe restuituite
	 */
	private int _length;
	/**
	 * nome da assegnare ai 'fullname' e 'email' qualora siano entrambi nulli
	 */
	private String _defaults;
	/**
	 * Indica una condizione d'errore, che potrebbe essere legata sia all'impossibilità di assegnare i campi propriamente, sia da
	 * elementi mal formattati (email)
	 */
	private boolean _faulty;
	
}