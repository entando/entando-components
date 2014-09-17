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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public interface IContentWorkflowManager {
	
	/**
	 * Restituisce il nome del ruolo per un dato tipo di contenuto.
	 * @param typeCode Il codice del tipo di contenuto.
	 * @return Il nome del ruolo per il tipo di contenuto richiesto.
	 */
	public String getRole(String typeCode);
	
	/**
	 * Aggiorna il ruolo per un dato tipo di contenuto.
	 * @param typeCode Il codice del tipo di contenuto.
	 * @param role Il nome del ruolo asociato al tipo di contenuto.
	 * @throws ApsSystemException In caso di errori in fase di aggiornamento.
	 */
	public void updateRole(String typeCode, String role) throws ApsSystemException;
	
	/**
	 * Restituisce la lista degli step per un dato tipo di contenuto.
	 * @param typeCode Il codice del tipo di contenuto.
	 * @return La lista degli step per il tipo di contenuto richiesto.
	 */
	public List<Step> getSteps(String typeCode);
	
	/**
	 * Aggiorna la lista degli step di un dato tipo di contenuto.
	 * @param workflow Il workflow aggiornato.
	 * @throws ApsSystemException In caso di errori in fase di aggiornamento.
	 */
	public void updateSteps(String typeCode, List<Step> steps) throws ApsSystemException;
	
	/**
	 * Restituisce il workflow per un dato tipo di contenuto.
	 * @param typeCode Il codice del tipo di contenuto.
	 * @return Il workflow per il tipo di contenuto richiesto.
	 */
	public Workflow getWorkflow(String typeCode);
	
	/**
	 * Restituisce gli step utilizzati per i contenuti del tipo dato.
	 * @param typeCode Il codice del tipo di contenuto.
	 * @return La lista degli step per cui sono presenti contenuti del tipo dato.
	 */
	public List<String> searchUsedSteps(String typeCode);
	
	public List<WorkflowSearchFilter> getWorkflowSearchFilters(UserDetails currentUser) throws ApsSystemException;
	
	public List<SmallContentType> getManagingContentTypes(UserDetails currentUser) throws ApsSystemException;
	
}