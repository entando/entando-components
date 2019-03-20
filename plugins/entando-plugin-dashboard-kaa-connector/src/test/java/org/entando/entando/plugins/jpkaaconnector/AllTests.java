package org.entando.entando.plugins.jpkaaconnector;

import org.entando.entando.plugins.jpkaaconnector.apsadmin.TestApsAdminSample;
import org.entando.entando.plugins.jpkaaconnector.aps.TestApsSample;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Change me with a suitable description");

		suite.addTestSuite(TestApsSample.class);
		suite.addTestSuite(TestApsAdminSample.class);
		
		return suite;
	}
	
}
