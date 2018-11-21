/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.entando.entando.plugins.jacms.web.contentmodel.ContentModelControllerUnitTest;
import org.entando.entando.plugins.jacms.web.page.PageConfigurationControllerIntegrationTest;
import org.entando.entando.plugins.jacms.web.page.PageConfigurationControllerWidgetsIntegrationTest;
import org.entando.entando.plugins.jacms.web.page.PageControllerIntegrationTest;
import org.entando.entando.plugins.jacms.web.contentmodel.ContentModelControllerIntegrationTest;

public class ControllersAllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(ControllersAllTests.class.getName());

        suite.addTest(new JUnit4TestAdapter(ContentModelControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(ContentModelControllerUnitTest.class));

        suite.addTest(new JUnit4TestAdapter(PageConfigurationControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(PageConfigurationControllerWidgetsIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(PageControllerIntegrationTest.class));

        return suite;
    }

}
