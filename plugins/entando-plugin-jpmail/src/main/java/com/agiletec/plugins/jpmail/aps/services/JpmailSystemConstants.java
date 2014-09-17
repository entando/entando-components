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