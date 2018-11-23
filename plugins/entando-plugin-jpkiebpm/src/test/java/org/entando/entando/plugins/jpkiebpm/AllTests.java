package org.entando.entando.plugins.jpkiebpm;

import junit.framework.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.TestKieFormOverrideManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.TestBpmOverrides;
import org.entando.entando.plugins.jpkiebpm.aps.internalservlet.TestBpmCaseInstanceActionBase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven.TestPamProcessForm;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.TestBpmFormWidgetAction;
import org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.DataUXBuilderTest;
import org.entando.entando.plugins.jpkiebpm.web.config.*;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("KIE-BPM connector test");

//		suite.addTestSuite(TestKieFormManager.class);
        suite.addTestSuite(TestKieContainer.class);
        suite.addTestSuite(TestKieProcesses.class);
        suite.addTestSuite(TestKieProcesseInstance.class);
        suite.addTestSuite(TestKieTask.class);
        suite.addTestSuite(TestKieForm.class);
        suite.addTestSuite(TestPamProcessForm.class);
        suite.addTestSuite(TestKieProcessForm.class);
        suite.addTestSuite(TestPayload.class);
        suite.addTestSuite(TestBpmToFormHelper.class);
        suite.addTestSuite(TestFormToBpmHelper.class);
        suite.addTestSuite(TestJsonHelper.class);
        suite.addTestSuite(TestKieFormOverrideManager.class);
        suite.addTestSuite(TestBpmFormWidgetAction.class);
        suite.addTestSuite(TestBpmOverrides.class);
        suite.addTestSuite(TestBpmOverrides.class);
        suite.addTestSuite(TestJpkiebpmLabelsProperties.class);
        suite.addTestSuite(DataUXBuilderTest.class);
        suite.addTestSuite(TestBpmCaseInstanceActionBase.class);


        suite.addTest(new JUnit4TestAdapter(ConfigControllerIntegrationTest.class));
        suite.addTest(new JUnit4TestAdapter(ConfigControllerUnitTest.class));

        return suite;
    }

}
