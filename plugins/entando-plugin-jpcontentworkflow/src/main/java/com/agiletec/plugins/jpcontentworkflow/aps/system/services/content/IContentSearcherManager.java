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