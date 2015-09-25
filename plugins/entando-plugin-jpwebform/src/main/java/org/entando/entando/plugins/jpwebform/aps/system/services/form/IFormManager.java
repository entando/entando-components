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
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.*;

/**
 * Interface for Manager for Form Message Object. 
 * @author E.Santoboni
 */
public interface IFormManager extends IEntityManager {

	/**
	 * Returns the notifier's configuration for a given type of message.
	 * @param typeCode The type of the message.
	 * @return The notifier's configuration for a given type of message.
	 */
	public MessageTypeNotifierConfig getNotifierConfig(String typeCode);

	/**
	 * Save the given notifier's configuration for a particular type of message.
	 * @param config The notifier's configuration for a particular type of message.
	 */
	public void saveNotifierConfig(MessageTypeNotifierConfig config) throws ApsSystemException;

	public List<SmallMessageType> getSmallMessageTypes();

	public Map<String, SmallMessageType> getSmallMessageTypesMap();

	public String getMailAttributeName(String typeCode);

	public List<String> loadMessagesId(EntitySearchFilter[] filters) throws ApsSystemException;

	public List<String> loadMessagesId(EntitySearchFilter[] filters, boolean answered) throws ApsSystemException;

	public Message getMessage(String id) throws ApsSystemException;

	public Message getMessage(String id, TypeVersionGuiConfig config) throws ApsSystemException;

	public void addUpdateMessage(Message message) throws ApsSystemException;

	public void sendMessage(Message message) throws ApsSystemException;

	public void sendMessage(Message message, String customEmail) throws ApsSystemException;

	public void deleteMessage(String messageId) throws ApsSystemException;

	public Message resumeMessageByUserAndType(String user, String typeCode, Integer status) throws ApsSystemException;

	public void deleteTypeVersion(String typeCode) throws ApsSystemException;

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

	/**
	 * Create a new message instance of requested type.
	 *  The new message is instantiated cloning the corresponding prototype.
	 * @param typeCode The code of the requested type of message, as defined in configuration.
	 * @return The requested message (empty).
	 */
	public Message createMessageType(String formTypeCode);
        
	public StepsConfig getStepsConfig(String formTypeCode);

	public void saveStepsConfig(StepsConfig config) throws ApsSystemException;

	public void deleteStepsConfig(String formTypeCode) throws ApsSystemException;

	public void generateNewVersionType(String formTypeCode) throws ApsSystemException;
	
	public Boolean validateOgnl(String expression, Message entity, String currentUser) throws ApsSystemException;
	
	public OGNL_MESSAGES verifyOgnlExpression(String expression, Message entity, String currentUser) throws ApsSystemException;
	
	public TypeVersionGuiConfig getTypeVersionGui(String formTypeCode) throws ApsSystemException;
	public TypeVersionGuiConfig getTypeVersionGui(String formTypeCode, Integer version) throws ApsSystemException;

	public StepsConfig getPublicSteps(String formTypeCode) throws ApsSystemException;
	public StepsConfig getPublicSteps(String formTypeCode, Integer version) throws ApsSystemException;

	public StepGuiConfig generateWorkStepUserGui(String formTypeCode, String stepCode, boolean save) throws ApsSystemException;

	public StepGuiConfig getWorkGuiConfig(String typeCode, String stepCode) throws ApsSystemException;

	public void saveWorkGuiConfig(StepGuiConfig config) throws ApsSystemException;

	public void deleteWorkGuiConfig(String typeCode, String stepCode) throws ApsSystemException;

	public void deleteAllWorkGuiConfig(String typeCode) throws ApsSystemException;
	
	public boolean checkStepGui(String entityTypeCode);

	public static final String FILTER_KEY_USERNAME = "username";
	public static final String FILTER_KEY_CREATION_DATE = "creationdate";
	public static final String FILTER_KEY_SEND_DATE = "senddate";
	public static final String FILTER_KEY_VERSION_TYPE = "versiontype";
	public static final String FILTER_KEY_MESSAGE_TYPE = "messagetype";
	public static final String FILTER_KEY_DONE = "done";
	public enum OGNL_MESSAGES {SUCCESS, METHOD_ERROR, PROPERTY_ERROR, GENERIC_ERROR};
	
}
