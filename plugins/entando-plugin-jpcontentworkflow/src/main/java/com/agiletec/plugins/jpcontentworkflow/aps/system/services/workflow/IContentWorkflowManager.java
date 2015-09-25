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