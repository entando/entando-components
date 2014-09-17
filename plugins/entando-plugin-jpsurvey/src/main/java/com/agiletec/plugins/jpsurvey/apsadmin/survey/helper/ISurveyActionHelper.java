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
