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
package com.agiletec.plugins.jpmail.apsadmin.mail;

/**
 * Interface for the actions of editing senders of MailManager configuration.
 * @version 1.0
 * @author E.Mezzano
 *
 */
public interface IMailSenderConfigAction {
	
	/**
	 * Execute the action of addition of a mail sender.
	 * @return The action result code.
	 */
	public String newSender();
	
	/**
	 * Execute the action of editing a mail sender.
	 * @return The action result code.
	 */
	public String edit();
	
	/**
	 * Execute the action of request of removal of a sender from the IMailManager service configuration.
	 * @return The action result code.
	 */
	public String trash();
	
	/**
	 * Execute the action of removal of a sender from the IMailManager service configuration.
	 * @return The action result code.
	 */
	public String delete();
	
	/**
	 * Execute the action of saving a mail sender.
	 * @return The action result code.
	 */
	public String save();
	
}