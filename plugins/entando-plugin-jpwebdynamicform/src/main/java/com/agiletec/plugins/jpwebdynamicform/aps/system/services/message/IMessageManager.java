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
package com.agiletec.plugins.jpwebdynamicform.aps.system.services.message;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.MessageTypeNotifierConfig;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;

/**
 * Interface for Manager for Message Object. 
 * @author E.Mezzano
 */
public interface IMessageManager extends IEntityManager {
	
	/**
	 * Returns the notifier's configuration for a given type of message.
	 * @param type The type of the message.
	 * @return The notifier's configuration for a given type of message.
	 */
	public MessageTypeNotifierConfig getNotifierConfig(String typeCode);
	
	/**
	 * Save the given notifier's configuration for a particular type of message.
	 * @param config The notifier's configuration for a particular type of message.
	 */
	public void saveNotifierConfig(MessageTypeNotifierConfig config) throws ApsSystemException;
	
	/**
	 * Create a new message instance of requested type.
	 *  The new message is instantiated cloning the corresponding prototype.
	 * @param typeCode The code of the requested type of message, as defined in configuration.
	 * @return The requested message (empty).
	 */
	public Message createMessageType(String typeCode);
	
	/**
	 * Returns a list of message types in small form.
	 * @return The list of message types (as {@link SmallMessageType}).
	 */
	public List<SmallMessageType> getSmallMessageTypes();
	
	/**
	 * Returns the map of message prototypes in small form ({@link SmallMessageType}), indexed by the type code.
	 * @return The map of message prototypes in small form ({@link SmallMessageType}).
	 */
	public Map<String, SmallMessageType> getSmallMessageTypesMap();
	
	/**
	 * Returns the name of the message attribute containing the mail address.
	 * @param typeCode The code of the requested type of message, as defined in configuration.
	 * @return The name of the message attribute containing the mail address.
	 */
	public String getMailAttributeName(String typeCode);
	
	/**
	 * Returns a list of Message identifiers for the given type code and the filters.
	 * @param filters The entity search filters. Could be null or empty.
	 * @return A list of messages identifiers.
	 * @throws ApsSystemException In case of error.
	 */
	public List<String> loadMessagesId(EntitySearchFilter[] filters) throws ApsSystemException;
	
	/**
	 * Returns a list of Message identifiers for the given type code and the filters.
	 * @param filters The entity search filters. Could be null or empty.
	 * @param answered If true filters only the responded record, otherwise only the not responded records.
	 * @return A list of messages identifiers.
	 * @throws ApsSystemException In case of error.
	 */
	public List<String> loadMessagesId(EntitySearchFilter[] filters, boolean answered) throws ApsSystemException;
	
	/**
	 * Returns the message of given id.
	 * @param id The identifier of the requested message.
	 * @return The requested message, null if not found.
	 * @throws ApsSystemException In case of error.
	 */
	public Message getMessage(String id) throws ApsSystemException;
	
	/**
	 * Add a Message.
	 * @param message The Message to add.
	 * @throws ApsSystemException In case of Exception adding the Message.
	 */
	public void addMessage(Message message) throws ApsSystemException;
	
	/**
	 * Send a Message.
	 * @param message The Message to send.
	 * @throws ApsSystemException In case of Exception adding the Message.
	 */
	public void sendMessage(Message message) throws ApsSystemException;
	
	/**
	 * Remove a message and all its references.
	 * @param messageId The identifier of the message.
	 * @throws ApsSystemException In case of error.
	 */
	public void deleteMessage(String messageId) throws ApsSystemException;
	
	/**
	 * Add an answer.
	 * @param answer The answer to add.
	 * @return true if the answer has been sent, false otherwise.
	 * @throws ApsSystemException In case of error.
	 */
	public boolean sendAnswer(Answer answer) throws ApsSystemException;
	
	/**
	 * Returns the answers to the message of given identifier.
	 * @param messageId The identifier of the message.
	 * @return The list of answers to the desired message.
	 * @throws ApsSystemException In case of error.
	 */
	public List<Answer> getAnswers(String messageId) throws ApsSystemException;
	
	public static final String USERNAME_FILTER_KEY = "username";
	public static final String CREATION_DATE_FILTER_KEY = "created";
	
}