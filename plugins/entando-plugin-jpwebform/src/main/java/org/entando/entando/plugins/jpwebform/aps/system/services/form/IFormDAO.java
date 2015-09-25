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

import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Answer;

/**
 * Interface for Data Access Object of Form Message Object and versioned types.
 * @author E.Santoboni
 */
public interface IFormDAO extends IEntityDAO {
	
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
	
	//public void saveTypeVersion(String typeCode, Integer versionId, String modelXml, String stepXml);
	
	//public String loadTypeVersion(String typeCode, Integer versionId);
	
}