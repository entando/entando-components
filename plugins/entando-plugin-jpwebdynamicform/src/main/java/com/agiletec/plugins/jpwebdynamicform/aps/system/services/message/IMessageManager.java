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