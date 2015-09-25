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
package com.agiletec.plugins.jpsurvey.apsadmin.survey.helper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import com.agiletec.aps.cms.resource.model.ResourceInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;

public interface ISurveyActionHelper {

	public Map getReferencingObjects(ResourceInterface resource, HttpServletRequest request) throws ApsSystemException;

	public List<Group> getAllowedGroups(UserDetails currentUser);

	/**
	 * Returns the list of IDs given the following criteria: 
	 * 
	 * @param insertedText Testo immesso per la ricerca.
	 * @param categoryCode Il codice della categoria specificata della risorsa.
	 * @return La lista di identificativi delle risorse cercate.
	 * @throws Throwable In caso di errore.
	 */
	public List<String> searchResources(String insertedText, String categoryCode) throws Throwable;

}
