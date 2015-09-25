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
package com.agiletec.plugins.jpmail.aps.services;

/**
 * Class containing constants for the jpmail plugin.
 * 
 * @version 1.0
 * @author E.Mezzano
 *
 */
public interface JpmailSystemConstants {
	
	/**
	 * Name of the service delegated to send eMail.
	 */
	public static final String MAIL_MANAGER = "jpmailMailManager";
	
	/**
	 * Name of the sysconfig parameter containing the Plugin configuration
	 */
	public static final String MAIL_CONFIG_ITEM = "jpmail_config";
	
	/**
	 * Supported protocols
	 */
	public static final int PROTO_STD = 0;
	public static final int PROTO_SSL = 1;
	public static final int PROTO_TLS = 2;

	public static final Integer DEFAULT_SMTP_PORT = 25;
	
}