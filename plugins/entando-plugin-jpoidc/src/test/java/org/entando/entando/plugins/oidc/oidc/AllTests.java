package org.entando.entando.plugins.oidc.oidc;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.entando.entando.plugins.oidc.oidc.aps.controller.DomainNameIdentityProviderExtractorTest;
import org.entando.entando.plugins.oidc.oidc.aps.controller.OidcHelperTest;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Change me with a suitable description");

		suite.addTest(new JUnit4TestAdapter(OidcHelperTest.class));
		suite.addTest(new JUnit4TestAdapter(DomainNameIdentityProviderExtractorTest.class));

		return suite;
	}

}
