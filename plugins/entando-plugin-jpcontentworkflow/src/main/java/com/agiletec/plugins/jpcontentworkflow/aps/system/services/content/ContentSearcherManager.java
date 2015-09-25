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