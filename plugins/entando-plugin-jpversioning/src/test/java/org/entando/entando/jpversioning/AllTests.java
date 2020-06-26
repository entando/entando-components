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
package org.entando.entando.jpversioning;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.jpversioning.services.configuration.VersioningConfigurationServiceTest;
import org.entando.entando.jpversioning.services.content.ContentVersioningServiceTest;
import org.entando.entando.jpversioning.web.configuration.VersioningConfigurationControllerIntegrationTest;
import org.entando.entando.jpversioning.web.configuration.VersioningConfigurationControllerTest;
import org.entando.entando.jpversioning.web.content.ContentVersioningControllerIntegrationTest;
import org.entando.entando.jpversioning.web.content.ContentVersioningControllerTest;
import org.entando.entando.jpversioning.web.resource.ResourceVersioningControllerIntegrationTest;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.entando.entando.jpversioning");
		System.out.println("Test for org.entando.entando.jpversioning");

		suite.addTestSuite(ContentVersioningServiceTest.class);
		suite.addTestSuite(VersioningConfigurationServiceTest.class);
		suite.addTest(new JUnit4TestAdapter(ContentVersioningControllerIntegrationTest.class));
		suite.addTest(new JUnit4TestAdapter(ContentVersioningControllerTest.class));
		suite.addTest(new JUnit4TestAdapter(VersioningConfigurationControllerIntegrationTest.class));
		suite.addTest(new JUnit4TestAdapter(VersioningConfigurationControllerTest.class));
		suite.addTest(new JUnit4TestAdapter(ResourceVersioningControllerIntegrationTest.class));

		return suite;
	}
	
}