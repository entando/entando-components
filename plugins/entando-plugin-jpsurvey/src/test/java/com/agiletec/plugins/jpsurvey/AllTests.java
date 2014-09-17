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
package com.agiletec.plugins.jpsurvey;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.TestResultManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.TestVoterManager;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.TestSurveyManager;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.TestChoiceAction;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.TestQuestionAction;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.TestSurveyAction;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.TestSurveyDetailAction;
import com.agiletec.plugins.jpsurvey.apsadmin.survey.TestSurveyImageAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Plugin Survey");
		
		suite.addTestSuite(TestVoterManager.class);
		suite.addTestSuite(TestResultManager.class);
		suite.addTestSuite(TestSurveyManager.class);
		
		suite.addTestSuite(TestSurveyAction.class);
		suite.addTestSuite(TestSurveyDetailAction.class);
		suite.addTestSuite(TestQuestionAction.class);
		suite.addTestSuite(TestChoiceAction.class);
		suite.addTestSuite(TestSurveyImageAction.class);
		
		return suite;
	}
	
}