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