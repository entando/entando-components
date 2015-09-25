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
package com.agiletec.plugins.jpwebdynamicform;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message.TestUserNewMessageAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.TestMessageDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.TestMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.TestMessageManagerWithRoles;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.TestMessageSearcherDAO;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.parse.TestMessageNotifierConfigDOM;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.renderer.TestBaseMessageRenderer;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.TestMessageFinderAction;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.TestOperatorMessageAction;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.config.TestNotifierConfigAction;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Entando Plugin jpwebdynamicform");

		suite.addTestSuite(TestBaseMessageRenderer.class);

		suite.addTestSuite(TestMessageDAO.class);
		suite.addTestSuite(TestMessageSearcherDAO.class);
		suite.addTestSuite(TestMessageManager.class);
		suite.addTestSuite(TestMessageManagerWithRoles.class);
		suite.addTestSuite(TestMessageNotifierConfigDOM.class);

		suite.addTestSuite(TestUserNewMessageAction.class);

		suite.addTestSuite(TestNotifierConfigAction.class);
		suite.addTestSuite(TestMessageFinderAction.class);
		suite.addTestSuite(TestOperatorMessageAction.class);

		return suite;
	}

}