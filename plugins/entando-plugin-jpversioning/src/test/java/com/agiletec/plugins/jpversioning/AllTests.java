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
package com.agiletec.plugins.jpversioning;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TestTrashedResourceDAO;
import com.agiletec.plugins.jpversioning.aps.system.services.resource.TestTrashedResourceManager;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.TestVersioningDAO;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.TestVersioningManager;
import com.agiletec.plugins.jpversioning.apsadmin.resource.TestTrashedResourceAction;
import com.agiletec.plugins.jpversioning.apsadmin.versioning.TestVersionAction;
import com.agiletec.plugins.jpversioning.apsadmin.versioning.TestVersionFinderAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jpversioning");
		System.out.println("Test for jpversioning");
		
		suite.addTestSuite(TestVersioningDAO.class);
		suite.addTestSuite(TestVersioningManager.class);
		
		suite.addTestSuite(TestTrashedResourceDAO.class);
		suite.addTestSuite(TestTrashedResourceManager.class);
		
		suite.addTestSuite(TestTrashedResourceAction.class);
		
		suite.addTestSuite(TestVersionFinderAction.class);
		suite.addTestSuite(TestVersionAction.class);
		
		return suite;
	}
	
}