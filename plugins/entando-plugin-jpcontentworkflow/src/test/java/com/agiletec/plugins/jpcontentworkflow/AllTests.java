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
package com.agiletec.plugins.jpcontentworkflow;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.TestContentManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.TestContentSearcherDAO;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.TestWorkflowNotifierDAO;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.TestWorkflowNotifierManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.parse.TestWorkflowNotifierDOM;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.TestContentWorkflowManager;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.TestContentAction;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.TestContentFinderAction;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.TestIntroNewContentAction;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.notifier.TestWorkflowNotifierConfigAction;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow.TestWorkflowAction;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow.TestWorkflowListAction;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.workflow.TestWorkflowStepAction;

/**
 * @author E.Santoboni
 */
public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jpcontentworkflow");
		
		suite.addTestSuite(TestWorkflowNotifierDOM.class);
		suite.addTestSuite(TestWorkflowNotifierDAO.class);
		suite.addTestSuite(TestWorkflowNotifierManager.class);
		
		suite.addTestSuite(TestContentSearcherDAO.class);
		suite.addTestSuite(TestContentManager.class);
		
		suite.addTestSuite(TestContentWorkflowManager.class);
		
		suite.addTestSuite(TestWorkflowNotifierConfigAction.class);
		
		suite.addTestSuite(TestWorkflowListAction.class);
		suite.addTestSuite(TestWorkflowAction.class);
		suite.addTestSuite(TestWorkflowStepAction.class);
		
		suite.addTestSuite(TestContentAction.class);
		suite.addTestSuite(TestContentFinderAction.class);
		suite.addTestSuite(TestIntroNewContentAction.class);
		
		return suite;
	}
	
}