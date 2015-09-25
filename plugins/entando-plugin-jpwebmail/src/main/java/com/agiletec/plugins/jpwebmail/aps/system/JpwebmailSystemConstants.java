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
package com.agiletec.plugins.jpwebmail.aps.system;

public interface JpwebmailSystemConstants {
	
	/**
	 * Nome del servizio delegato alla gestione della Web Mail.
	 */
	public static final String WEBMAIL_MANAGER = "jpwebmailWebMailManager";
	
	/**
	 * Password usata dalla utility SSL per registrare i certificati autogenerati
	 */
	public static final String WEBMAIL_SSL_CERTIFICATE_STORE_PWD = "changeit";
	
	public static final String WEBMAIL_CONFIG_ITEM = "jpwebmail_config";
	
	public static final String SESSIONPARAM_CURRENT_MESSAGE_ON_EDIT = "jpwebmail_currentMessageOnEdit";
	
	public static final String SESSIONPARAM_CURRENT_MAIL_USER_PASSWORD = "jpwebmail_currentMailUserPassword";
	
	public static final String INBOX_FOLDER = "INBOX";
	
	public static final String TO_RECIPIENT = "1";
	
	public static final String CC_RECIPIENT = "2";
	
	public static final String BCC_RECIPIENT = "3";
	
}