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
package com.agiletec.plugins.jpmail.aps.services.mail;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Basic interface for the services using jpmail plugin and IMailManager.
 * 
 * @version 1.0
 * @author E.Mezzano
 *
 */
public interface MailSendersUtilizer {
	
	/**
	 * Returns the identifier of the IMailManager utilizer service.
	 * @return The identifier of the IMailManager utilizer service.
	 */
	public String getName();
	
	/**
	 * Returns the array of the used sender codes.
	 * Restituisce l'array dei codici degli indirizzi mittente utilizzati.
	 * @return The array of the used sender codes.
	 * @throws ApsSystemException In case of exceptions.
	 */
	public String[] getSenderCodes();
	
}