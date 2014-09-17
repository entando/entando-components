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