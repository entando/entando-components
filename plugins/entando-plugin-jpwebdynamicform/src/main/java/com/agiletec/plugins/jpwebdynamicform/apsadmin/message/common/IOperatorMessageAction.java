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
 * Interface for Operator actions on a Message.
 * @author E.Mezzano
 */
public interface IOperatorMessageAction {
	
	/**
	 * Executes the operation of visualization of a message.
	 * @return The action result code.
	 */
	public String view();
	
	/**
	 * Executes the operation of enter in the answer form to a message.
	 * @return The action result code.
	 */
	public String newAnswer();
	
	/**
	 * Executes the operation of answering to a message.
	 * @return The action result code.
	 */
	public String answer();
	
	/**
	 * Executes the operation of request of deleting a message.
	 * @return The action result code.
	 */
	public String trash();
	
	/**
	 * Executes the operation of deleting a message.
	 * @return The action result code.
	 */
	public String delete();
	
}