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
package com.agiletec.plugins.jpmail;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpmail.aps.system.services.mail.TestMailManager;
import com.agiletec.plugins.jpmail.aps.system.services.mail.parse.TestMailConfigDOM;
import com.agiletec.plugins.jpmail.aps.system.services.mail.util.TestEmailAddressValidator;
import com.agiletec.plugins.jpmail.apsadmin.mail.TestMailSenderConfigAction;
import com.agiletec.plugins.jpmail.apsadmin.mail.TestSmtpConfigAction;

/**
 * Launch class for all the jpmail tests.
 * @version 1.0
 */
public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Entando Plugin Mail");
		
		suite.addTestSuite(TestEmailAddressValidator.class);
		suite.addTestSuite(TestMailConfigDOM.class);
		suite.addTestSuite(TestMailManager.class);
		
		suite.addTestSuite(TestSmtpConfigAction.class);
		suite.addTestSuite(TestMailSenderConfigAction.class);
		
		return suite;
	}
	
}