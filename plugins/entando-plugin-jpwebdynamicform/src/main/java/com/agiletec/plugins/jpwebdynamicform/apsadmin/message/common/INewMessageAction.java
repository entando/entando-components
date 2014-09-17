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
package com.agiletec.plugins.jpwebdynamicform.apsadmin.message.common;

/**
 * Interface for editing operations on a Message.
 * @author E.Mezzano
 */
public interface INewMessageAction extends IMessageAction {

	/**
	 * Starting operation for the creation of a new Message.
	 * @return The action result code.
	 */
	public String createNew();

	/**
	 * Back point for message operations.
	 * @return The action result code.
	 */
	public String entryMessage();

	/**
	 * Saving operation for the new Message.
	 * @return The action result code.
	 */
	public String save();
	
	/**
	 * The name of the session attribute containing the current message.
	 */
	public static final String SESSION_PARAM_NAME_CURRENT_MESSAGE = "jpwebdynamicformCurrentMessage";
	
	public static final String SESSION_PARAM_NAME_HONEYPOT = "jpwebdynamicformHoneypotFieldName";
	
}