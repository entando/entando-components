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