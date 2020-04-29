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

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.plugins.jpseo.aps.system.services.content.ContentMappingIntegrationTest;
import org.entando.entando.plugins.jpseo.aps.system.services.metatag.MetatagCatalogueIntegrationTest;
import org.entando.entando.plugins.jpseo.apsadmin.content.ContentActionIntegrationTest;
import org.entando.entando.plugins.jpseo.apsadmin.portal.PageActionIntegrationTest;
import org.entando.entando.plugins.jpseo.apsadmin.portal.PageSettingsActionAspectTest;
import org.entando.entando.plugins.jpseo.apsadmin.portal.PageSettingsActionIntegrationTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Seo plugin");

        suite.addTestSuite(MetatagCatalogueIntegrationTest.class);

        suite.addTestSuite(ContentMappingIntegrationTest.class);
        suite.addTestSuite(ContentActionIntegrationTest.class);
        suite.addTestSuite(PageActionIntegrationTest.class);
        suite.addTestSuite(PageSettingsActionIntegrationTest.class);
        suite.addTest(new JUnit4TestAdapter(PageSettingsActionAspectTest.class));

        return suite;
    }

}
