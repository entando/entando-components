/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebdynamicform;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message.TestUserNewMessageAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.TestMessageDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.TestMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.TestMessageSearcherDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.parse.TestMessageNotifierConfigDOM;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.renderer.TestBaseMessageRenderer;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.TestMessageFinderAction;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.TestOperatorMessageAction;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.config.TestNotifierConfigAction;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS Plugin jpwebdynamicform");

		suite.addTestSuite(TestBaseMessageRenderer.class);

		suite.addTestSuite(TestMessageDAO.class);
		suite.addTestSuite(TestMessageSearcherDAO.class);
		suite.addTestSuite(TestMessageManager.class);
		suite.addTestSuite(TestMessageNotifierConfigDOM.class);

		suite.addTestSuite(TestUserNewMessageAction.class);

		suite.addTestSuite(TestNotifierConfigAction.class);
		suite.addTestSuite(TestMessageFinderAction.class);
		suite.addTestSuite(TestOperatorMessageAction.class);

		return suite;
	}

}