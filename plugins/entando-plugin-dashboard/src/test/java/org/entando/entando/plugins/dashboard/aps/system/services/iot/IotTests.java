package org.entando.entando.plugins.dashboard.aps.system.services.iot;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.controller.TestConnectorController;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.controller.TestDashboardConfigController;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IotUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    IotUtilsTest.class,
    TestDashboardConfigController.class,
    TestConnectorController.class,
})
public class IotTests { }
