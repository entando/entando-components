package org.entando.entando.plugins.jpkiebpm;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.TestKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.TestKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.TestBpmToFormHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.TestFormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.TestJsonHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieContainer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieProcessForm;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieProcesseInstance;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieProcesses;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestKieTask;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.TestPayload;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.TestBpmOverrides;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("KIE-BPM connector test");

		suite.addTestSuite(TestKieFormManager.class);
		suite.addTestSuite(TestKieContainer.class);
		suite.addTestSuite(TestKieProcesses.class);
		suite.addTestSuite(TestKieProcesseInstance.class);
		suite.addTestSuite(TestKieTask.class);
		suite.addTestSuite(TestKieForm.class);
		suite.addTestSuite(TestKieProcessForm.class);
		suite.addTestSuite(TestPayload.class);
		suite.addTestSuite(TestBpmToFormHelper.class);
		suite.addTestSuite(TestFormToBpmHelper.class);
		suite.addTestSuite(TestJsonHelper.class);
		suite.addTestSuite(TestKieFormOverrideManager.class);
		suite.addTestSuite(TestBpmOverrides.class);

		return suite;
	}

}
