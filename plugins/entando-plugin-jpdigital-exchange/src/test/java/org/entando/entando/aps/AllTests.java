/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesManagerTest;
import org.entando.entando.aps.system.services.digitalexchange.DigitalExchangesServiceTest;
import org.entando.entando.aps.system.services.digitalexchange.category.DigitalExchangeCategoriesServiceTest;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangeOAuth2RestTemplateFactoryTest;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClientTest;
import org.entando.entando.aps.system.services.digitalexchange.component.DigitalExchangeComponentsServiceTest;
import org.entando.entando.aps.system.services.digitalexchange.job.ComponentDescriptorTest;
import org.entando.entando.aps.system.services.digitalexchange.job.DigitalExchangeJobExecutorTest;
import org.entando.entando.aps.system.services.digitalexchange.job.DigitalExchangeComponentJobsServiceTest;
import org.entando.entando.aps.system.services.pagemodel.DigitalExchangePageModelServiceTest;
import org.entando.entando.web.digitalexchange.DigitalExchangesControllerIntegrationTest;
import org.entando.entando.web.digitalexchange.DigitalExchangesControllerTest;
import org.entando.entando.web.digitalexchange.category.DigitalExchangeCategoriesControllerIntegrationTest;
import org.entando.entando.web.digitalexchange.category.DigitalExchangeCategoriesControllerTest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponentsControllerIntegrationTest;
import org.entando.entando.web.digitalexchange.component.DigitalExchangeComponentsControllerTest;
import org.entando.entando.web.digitalexchange.install.DigitalExchangeInstallResourceIntegrationTest;
import org.entando.entando.web.digitalexchange.job.DigitalExchangeJobResourceControllerIntegrationTest;
import org.entando.entando.web.pagemodel.DigitalExchangePageModelControllerIntegrationTest;
import org.entando.entando.web.pagemodel.DigitalExchangePageModelControllerTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for Digital Exchange Plugin");

        suite.addTest(new JUnit4TestAdapter(DigitalExchangesClientTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangesManagerTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeOAuth2RestTemplateFactoryTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeJobExecutorTest.class));
        suite.addTest(new JUnit4TestAdapter(ComponentDescriptorTest.class));

        addServicesTests(suite);
        addControllersTests(suite);

        return suite;
    }

    private static void addServicesTests(TestSuite suite) {

        suite.addTest(new JUnit4TestAdapter(DigitalExchangesServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeComponentsServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeCategoriesServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeComponentJobsServiceTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangePageModelServiceTest.class));
    }

    private static void addControllersTests(TestSuite suite) {

        suite.addTest(new JUnit4TestAdapter(DigitalExchangesControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangesControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(DigitalExchangeComponentsControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeComponentsControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(DigitalExchangeCategoriesControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangeCategoriesControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(DigitalExchangeInstallResourceIntegrationTest.class));

        suite.addTest(new JUnit4TestAdapter(DigitalExchangePageModelControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(DigitalExchangePageModelControllerTest.class));

        suite.addTest(new JUnit4TestAdapter(DigitalExchangeJobResourceControllerIntegrationTest.class));
    }
}
