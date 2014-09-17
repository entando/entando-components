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

import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;

/**
 * Abstract class for access to a Message.
 * @author E.Mezzano
 */
public abstract class AbstractMessageAction extends BaseAction {
	
	/**
	 * Verify if the user is allowed to access to the given message.
	 * @param message The message for whom check the access.
	 * @return true if the access is allowed, false otherwise.
	 */
	protected abstract boolean isUserAllowed(Message message);
	
	/**
	 * Executes the operation of visualization of a message.
	 * @return The action result code.
	 */
	public String view() {
		try {
			Message message = this.getMessage();
			if (message == null || !this.isUserAllowed(message)) {
				this.addActionError(this.getText("Message.message.notFound"));
				return "messageNotFound";
			}
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "view");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Returns the desired message.
	 * @return The desired message.
	 */
	public Message getMessage() {
		if (this._message == null) {
			try {
				this._message = this.getMessageManager().getMessage(this.getId());
			} catch(Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getMessage");
				throw new RuntimeException("Error finding message", t);
			}
		}
		return _message;
	}
	
	/**
	 * Returns the answers to the current message.
	 * @return The answers to the current message.
	 */
	public List<Answer> getAnswers() {
		if (this._answers == null) {
			try {
				this._answers = this.getMessageManager().getAnswers(this.getId());
			} catch(Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getAnswers");
				throw new RuntimeException("Error finding answers to current message", t);
			}
		}
		return _answers;
	}
	
	/**
	 * Return the default system lang.
	 * @return The default lang.
	 */
	public Lang getDefaultLang() {
		return this.getLangManager().getDefaultLang();
	}
	
	/**
	 * Returns the message identifier.
	 * @return The message identifier.
	 */
	public String getId() {
		return _id;
	}
	/**
	 * Sets the message identifier.
	 * @param id The message identifier.
	 */
	public void setId(String id) {
		this._id = id;
	}
	
	/**
	 * Returns the MessageManager.
	 * @return The MessageManager.
	 */
	protected IMessageManager getMessageManager() {
		return _messageManager;
	}
	
	/**
	 * Sets the MessageManager. Must be setted with Spring bean injection.
	 * @param messageManager The MessageManager.
	 */
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}
	
	private String _id;
	
	private Message _message;
	private List<Answer> _answers;
	
	private IMessageManager _messageManager;
	
}