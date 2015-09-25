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
package com.agiletec.plugins.jpsurvey.apsadmin.survey;

import com.agiletec.plugins.jpsurvey.apsadmin.ApsAdminPluginBaseTestCase;

import com.agiletec.apsadmin.system.BaseAction;
import com.opensymphony.xwork2.Action;

public class TestSurveyDetailAction extends ApsAdminPluginBaseTestCase {
	
	public void testView() throws Throwable {	
		try {
			this.setUserOnSession("admin");
			this.initAction("/do/jpsurvey/Survey", "view");
			this.addParameter("surveyId", String.valueOf(1));
			String result = this.executeAction();
			assertEquals(BaseAction.FAILURE, result);
			
			result = this.executeView("admin", 1, false);
			assertEquals("listSurveys", result);
			
			result = this.executeView("admin", 2, true);
			assertEquals("listSurveys", result);
			
			result = this.executeView("admin", 1, true);
			assertEquals(Action.SUCCESS, result);
			
			result = this.executeView("admin", 2, false);
			assertEquals(Action.SUCCESS, result);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private String executeView(String username, int surveyId, boolean questionnaire) throws Throwable {
		this.setUserOnSession("admin");
		this.initAction("/do/jpsurvey/Survey", "view");
		this.addParameter("surveyId", String.valueOf(surveyId));
		this.addParameter("questionnaire", String.valueOf(questionnaire));
		String result = this.executeAction();
		return result;
	}
	
}