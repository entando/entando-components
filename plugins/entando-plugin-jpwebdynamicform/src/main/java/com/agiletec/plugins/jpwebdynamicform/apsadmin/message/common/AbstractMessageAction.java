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