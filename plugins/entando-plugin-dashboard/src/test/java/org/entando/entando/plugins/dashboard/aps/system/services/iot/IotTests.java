package org.entando.entando.plugins.dashboard.aps.system.services.iot;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.TestConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.TestMeasurementConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IotUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({

    TestConnectorService.class,
    IotUtilsTest.class,
    TestMeasurementConfigService.class

})
public class IotTests { }
