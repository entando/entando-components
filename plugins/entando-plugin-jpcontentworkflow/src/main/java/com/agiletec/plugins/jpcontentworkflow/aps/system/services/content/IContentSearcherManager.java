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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.content;

import java.util.Collection;
import java.util.List;

import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public interface IContentSearcherManager {
	
	/**
	 * Carica una lista di identificativi di contenuti in base ai parametri immessi.
	 * @param workflowFilters L'insieme dei filtri sui contenuti del workflow su cui la ricerca deve essere effettuata.
	 * @param categories La categorie dei contenuti da cercare.
	 * @param filters L'insieme dei filtri sugli attibuti, su cui la ricerca deve essere effettuata.
	 * @param userGroupCodes I codici dei gruppi utenti dell'utente richiedente la lista. 
	 * Se la collezione è vuota o nulla, gli identificativi di contenuto erogati saranno 
	 * relativi al gruppo definito "ad accesso libero". Nel caso nella collezione sia presente 
	 * il codice del gruppo degli amministratori, non sarà applicato alcun filtro sul gruppo.
	 * @return La lista degli id dei contenuti cercati.
	 * @throws ApsSystemException in caso di errore nell'accesso al db.
	 */
	public List<String> loadContentsId(List<WorkflowSearchFilter> workflowFilters, String[] categories, 
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException;
	
}