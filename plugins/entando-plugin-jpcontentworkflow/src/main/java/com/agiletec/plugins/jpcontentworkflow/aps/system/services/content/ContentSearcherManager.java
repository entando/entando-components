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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public class ContentSearcherManager extends AbstractService implements IContentSearcherManager {

	private static final Logger _logger = LoggerFactory.getLogger(ContentSearcherManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public List<String> loadContentsId(List<WorkflowSearchFilter> workflowFilters, String[] categories, 
			EntitySearchFilter[] filters, Collection<String> userGroupCodes) throws ApsSystemException {
		try {
			return this.getContentSearcherDAO().loadContentsId(workflowFilters, categories, filters, userGroupCodes);
		} catch (Throwable t) {
			_logger.error("Error loading content identifiers filtered for workflow", t);
			throw new ApsSystemException("Error loading content identifiers filtered for workflow", t);
		}
	}
	
	protected IContentSearcherDAO getContentSearcherDAO() {
		return _contentSearcherDAO;
	}
	public void setContentSearcherDAO(IContentSearcherDAO contentSearcherDAO) {
		this._contentSearcherDAO = contentSearcherDAO;
	}
	
	private IContentSearcherDAO _contentSearcherDAO;
	
}