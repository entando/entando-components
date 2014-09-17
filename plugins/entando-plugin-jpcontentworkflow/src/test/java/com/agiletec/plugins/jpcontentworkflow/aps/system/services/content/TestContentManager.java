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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.agiletec.plugins.jpcontentworkflow.aps.ApsPluginBaseTestCase;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.IContentSearcherManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;

/**
 * @author E.Santoboni
 */
public class TestContentManager extends ApsPluginBaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }
	
	public void testLoadContentsId() throws ApsSystemException {
		List<WorkflowSearchFilter> workflowFilters = new ArrayList<WorkflowSearchFilter>();
		
		Collection<String> userGroupCodes = new ArrayList<String>();
		userGroupCodes.add(Group.FREE_GROUP_NAME);
		List<String> contentsId = this._contentSearcherManager.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(0, contentsId.size());
		
		WorkflowSearchFilter f1 = new WorkflowSearchFilter();
		f1.setTypeCode("ART");
		f1.addAllowedStep(Content.STATUS_READY);
		workflowFilters.add(f1);
		contentsId = this._contentSearcherManager.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(1, contentsId.size());
		
		WorkflowSearchFilter f2 = new WorkflowSearchFilter();
		f2.setTypeCode("RAH");
		f2.addAllowedStep(Content.STATUS_DRAFT);
		workflowFilters.add(f2);
		contentsId = this._contentSearcherManager.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(2, contentsId.size());
		
		WorkflowSearchFilter f3 = new WorkflowSearchFilter();
		f3.setTypeCode("EVN");
		f3.addAllowedStep(Content.STATUS_READY);
		workflowFilters.add(f3);
		contentsId = this._contentSearcherManager.loadContentsId(workflowFilters, null, null, userGroupCodes);
		assertEquals(2, contentsId.size());
	}
	
    private void init() throws Exception {
    	try {
    		this._contentSearcherManager = (IContentSearcherManager) this.getService(JpcontentworkflowSystemConstants.CONTENT_SEARCHER_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }
    
    private IContentSearcherManager _contentSearcherManager = null;
	
}