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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.apsadmin.content.IContentFinderAction;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;
import com.opensymphony.xwork2.Action;

/**
 * @author E.Santoboni
 */
public class TestContentFinderAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}
	
	public void testSearch_1() throws Throwable {
		try {
			this._helper.setWorkflowConfig();
			Map<String, String> params = new HashMap<String, String>();
			this.executeSearch("admin", params);
			IContentFinderAction action = (IContentFinderAction) this.getAction();
			//action = (IContentFinderAction) this.getAction();
			List<String> contents = action.getContents();
			assertEquals(24, contents.size());
			this.executeSearch("editorCoach", params);
			action = (IContentFinderAction) this.getAction();
			contents = action.getContents();
			String[] contentsId = { "ART112", "ART102", "ART104", "RAH101" };
			assertEquals(contentsId.length, contents.size());
			for (int i = 0; i < contentsId.length; i++) {
				String contentId = contentsId[i];
				assertTrue(contents.contains(contentId));
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.resetWorkflowConfig();
		}
	}
	
	public void testSearch_2() throws Throwable {
		try {
			this._helper.setWorkflowConfig();
			this._helper.setContentStates();
			Map<String, String> params = new HashMap<String, String>();
			
			this.executeSearch("admin", params);
			IContentFinderAction action = (IContentFinderAction) this.getAction();
			action = (IContentFinderAction) this.getAction();
			List<String> contents = action.getContents();
			assertEquals(24, contents.size());
			
			this.executeSearch("editorCoach", params);
			action = (IContentFinderAction) this.getAction();
			contents = action.getContents();
			String[] contentsId = { "ART102", "ART112" };
			assertEquals(contentsId.length, contents.size());
			for (int i = 0; i < contentsId.length; i++) {
				String contentId = contentsId[i];
				assertTrue(contents.contains(contentId));
			}
			
			this.executeSearch("supervisorCoach", params);
			action = (IContentFinderAction) this.getAction();
			contents = action.getContents();
			contentsId = new String[]{ "ART102", "ART111", "ART112", "RAH101" };
			assertEquals(contentsId.length, contents.size());
			for (int i = 0; i < contentsId.length; i++) {
				String contentId = contentsId[i];
				assertTrue(contents.contains(contentId));
			}
		} catch (Throwable t) {
			throw t;
		} finally {
			this._helper.resetWorkflowConfig();
			this._helper.resetContentStates();
		}
	}
	
	private void executeSearch(String currentUserName, Map<String, String> params) throws Throwable {
		this.initAction("/do/jacms/Content", "search");
		this.setUserOnSession(currentUserName);
		this.addParameters(params);
		String result = this.executeAction();
		assertEquals(Action.SUCCESS, result);
	}
	
	private void init() {
		ContentWorkflowManager workflowManager = (ContentWorkflowManager) this.getService(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_MANAGER);
		ConfigInterface configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
		this._helper = new WorkflowTestHelper(workflowManager, configManager, dataSource);
	}
	
	private WorkflowTestHelper _helper;
	
}