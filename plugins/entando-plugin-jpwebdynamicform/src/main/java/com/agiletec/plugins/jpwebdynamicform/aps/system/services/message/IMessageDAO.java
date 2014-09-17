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

import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Answer;

/**
 * Interface for Data Access Object for Message Object.
 * @author E.Mezzano
 */
public interface IMessageDAO extends IEntityDAO {
	
	/**
	 * Remove a message and all its references.
	 * @param username The username of the author of the message.
	 * @throws ApsSystemException In case of error.
	 */
	public void deleteUserMessages(String username) throws ApsSystemException;
	
	/**
	 * Add an answer.
	 * @param answer The answer to add.
	 * @throws ApsSystemException In case of error.
	 */
	public void addAnswer(Answer answer) throws ApsSystemException;
	
	/**
	 * Returns the answers to the message of given identifier.
	 * @param messageId The identifier of the message.
	 * @return The list of answers to the desired message.
	 * @throws ApsSystemException In case of error.
	 */
	public List<Answer> loadAnswers(String messageId) throws ApsSystemException;
	
}