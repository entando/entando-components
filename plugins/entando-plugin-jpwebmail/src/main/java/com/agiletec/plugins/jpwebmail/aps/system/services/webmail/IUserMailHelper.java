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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * Interfaccia base per la classe helper delegata alla restituzione 
 * dell'indirizzo email dell'utente specificato.
 * @version 1.0
 * @author E.Santoboni
 */
public interface IUserMailHelper {
	
	/** 
	 * Restituisce la mail associata all'utente dato.
	 * L'indirizzo email può essere restituito nella forma semplice (solo indirizzo email) o 
	 * nella froma completa (nome con indirizzo email rachiuso tra parentesi "<>")
	 * @param user L'utente cui restituire l'indirizzo email corrispondente.
	 * @return L'indirizzo email dell'utente specificato.
	 * @throws ApsSystemException In caso di errore.
	 */
	public String getEmailAddress(UserDetails user) throws ApsSystemException;
	
}