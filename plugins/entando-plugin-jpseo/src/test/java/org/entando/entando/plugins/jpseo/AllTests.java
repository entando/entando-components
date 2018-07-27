/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo;


import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.TestMetatagCatalogue;

import org.entando.entando.plugins.jpseo.apsadmin.content.TestContentAction;
import org.entando.entando.plugins.jpseo.apsadmin.portal.TestPageAction;
import org.entando.entando.plugins.jpseo.apsadmin.portal.TestPageSettingsAction;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Seo plugin");
        
		suite.addTestSuite(TestMetatagCatalogue.class);
        
		suite.addTestSuite(TestContentAction.class);
		suite.addTestSuite(TestPageAction.class);
		suite.addTestSuite(TestPageSettingsAction.class);
        
		return suite;
	}

}
