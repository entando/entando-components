package org.entando.entando.plugins.jprestapi;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.plugins.jprestapi.aps.core.helper.TestJAXBHelper;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("REST API abstraction layer Tests");

        suite.addTestSuite(TestJAXBHelper.class);

        return suite;
    }
}
