package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class TestConnectorService {

  @InjectMocks
  ConnectorService connectorService;

  @InjectMocks
  ConnectorFactory connectorFactory;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

   @Test
  public void pingServerTest() throws IOException {
//    DashboardKaaDatasourceDto dashboardKaaDatasourceDto = new DashboardKaaDatasourceDto();
//    dashboardKaaDatasourceDto.setDashboardConfigDto(new DashboardConfigDto());
//    dashboardKaaDatasourceDto.setDashboardUrl(TestUtils.KAA_SERVER_TEST_ADDRESS);
//    assertTrue(connectorService.pingServer(dashboardKaaDatasourceDto.getDashboardConfigDto()));
  }
}
