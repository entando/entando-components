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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import com.agiletec.plugins.jpcontentworkflow.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.ContentSearcherDAO;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.IContentSearcherDAO;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public class TestContentSearcherDAO extends ApsPluginBaseTestCase {
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testLoadContentsId() {
		List<WorkflowSearchFilter> workflowFilters = new ArrayList<WorkflowSearchFilter>();
		
		Collection<String> userGroupCodes = new ArrayList<String>();
		userGroupCodes.add(Group.FREE_GROUP_NAME);
		List<String> contentsId = this._searcherDAO.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(0, contentsId.size());
		
		WorkflowSearchFilter f1 = new WorkflowSearchFilter();
		f1.setTypeCode("ART");
		f1.addAllowedStep(Content.STATUS_READY);
		workflowFilters.add(f1);
		contentsId = this._searcherDAO.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(1, contentsId.size());
		
		WorkflowSearchFilter f2 = new WorkflowSearchFilter();
		f2.setTypeCode("RAH");
		f2.addAllowedStep(Content.STATUS_DRAFT);
		workflowFilters.add(f2);
		contentsId = this._searcherDAO.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(2, contentsId.size());
		
		WorkflowSearchFilter f3 = new WorkflowSearchFilter();
		f3.setTypeCode("EVN");
		f3.addAllowedStep(Content.STATUS_READY);
		workflowFilters.add(f3);
		contentsId = this._searcherDAO.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(2, contentsId.size());
	}
	
	private void init() {
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		ContentSearcherDAO searcherDAO = new ContentSearcherDAO();
		searcherDAO.setDataSource(dataSource);
		this._searcherDAO = searcherDAO;
	}
	
	private IContentSearcherDAO _searcherDAO;
	
}