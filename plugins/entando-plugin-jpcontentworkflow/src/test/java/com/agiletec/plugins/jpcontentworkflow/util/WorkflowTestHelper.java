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
package com.agiletec.plugins.jpcontentworkflow.util;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.ContentWorkflowManager;

/**
 * @author E.Santoboni
 */
public class WorkflowTestHelper {
	
	public WorkflowTestHelper(ContentWorkflowManager workflowManager, ConfigInterface configManager) {
		this(workflowManager, configManager, null);
	}
	
	public WorkflowTestHelper(ContentWorkflowManager workflowManager, ConfigInterface configManager, DataSource dataSource) {
		this._workflowManager = workflowManager;
		this._configManager = configManager;
		this._dataSource = dataSource;
	}
	
	public void setWorkflowConfig() throws Exception {
		this.updateConfig(SET_CONFIG);
	}
	
	public void resetWorkflowConfig() throws Exception {
		this.updateConfig(RESET_CONFIG);
		this.updateConfig(RESET_CONFIG);
	}
	
	public void setContentStates() throws Exception {
		this.executeQuery(SET_STATES_1);
		this.executeQuery(SET_STATES_2);
		this.executeQuery(SET_STATES_3);
		this.executeQuery(SET_STATES_4);
	}
	
	public void resetContentStates() throws Exception {
		this.executeQuery(RESET_STATES_1);
		this.executeQuery(RESET_STATES_2);
	}
	
	private void executeQuery(String query) throws Exception {
		Connection conn = this._dataSource.getConnection();
		Statement stat = conn.createStatement();
		stat.execute(query);
		stat.close();
		conn.close();
	}
	
	private void updateConfig(String config) throws Exception {
		this._configManager.updateConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM, config);
		this._workflowManager.init();
	}
    
	private static final String SET_STATES_1 = 
		"UPDATE contents SET status = 'step1' WHERE contentid = 'ART104'";
	private static final String SET_STATES_2 = 
		"UPDATE contents SET status = 'step3' WHERE contentid = 'ART111'";
	private static final String SET_STATES_3 = 
		"UPDATE contents SET status = 'step2' WHERE contentid = 'ART102'";
	private static final String SET_STATES_4 = 
		"UPDATE contents SET status = '" + Content.STATUS_READY + "' WHERE contentid = 'RAH101' ";
    
	private static final String RESET_STATES_1 = 
		"UPDATE contents SET status = '" + Content.STATUS_DRAFT + "' WHERE " +
				"contentid = 'ART102' OR contentid = 'ART104' OR contentid = 'RAH101'";
	
	private static final String RESET_STATES_2 = 
		"UPDATE contents SET status = '" + Content.STATUS_READY + "' WHERE " +
				"contentid = 'ART111'";
	
	private static final String SET_CONFIG = 
		"<contenttypes>" +
			"<contenttype typecode=\"ART\">" +
				"<step code=\"step1\" descr=\"Step 1\" role=\"pageManager\" />" +
				"<step code=\"step2\" descr=\"Step 2\" />" +
				"<step code=\"step3\" descr=\"Step 3\" role=\"supervisor\" />" +
			"</contenttype>" +
			"<contenttype typecode=\"EVN\" role=\"pageManager\" />" +
		"</contenttypes>";
	
	private static final String RESET_CONFIG = 
		"<contenttypes />";
	
	private ContentWorkflowManager _workflowManager;
	private ConfigInterface _configManager;
    private DataSource _dataSource;
	
}