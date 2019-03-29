package org.entando.entando.plugins.jpinfinispan;

import org.entando.entando.plugins.jpinfinispan.aps.TestApsSample;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.aps.system.services.cache.CacheIntegrationTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for Infinispan connector");

		suite.addTestSuite(TestApsSample.class);
		suite.addTestSuite(CacheIntegrationTest.class);

		return suite;
	}

}
