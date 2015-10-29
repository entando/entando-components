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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowTestHelper;
import com.opensymphony.xwork2.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

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
			ContentFinderAction action = (ContentFinderAction) this.getAction();
			//action = (IContentFinderAction) this.getAction();
			List<String> contents = action.getContents();
			assertEquals(24, contents.size());
			this.executeSearch("editorCoach", params);
			action = (ContentFinderAction) this.getAction();
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
			ContentFinderAction action = (ContentFinderAction) this.getAction();
			action = (ContentFinderAction) this.getAction();
			List<String> contents = action.getContents();
			assertEquals(24, contents.size());
			
			this.executeSearch("editorCoach", params);
			action = (ContentFinderAction) this.getAction();
			contents = action.getContents();
			String[] contentsId = { "ART102", "ART112" };
			assertEquals(contentsId.length, contents.size());
			for (int i = 0; i < contentsId.length; i++) {
				String contentId = contentsId[i];
				assertTrue(contents.contains(contentId));
			}
			
			this.executeSearch("supervisorCoach", params);
			action = (ContentFinderAction) this.getAction();
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