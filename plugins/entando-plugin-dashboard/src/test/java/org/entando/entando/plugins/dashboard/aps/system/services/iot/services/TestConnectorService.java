package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestConnectorService {

  @InjectMocks
  ConnectorService connectorService;

  @Mock
  ConnectorFactory connectorFactory;

  @Mock
  BaseConnectorIot baseConnectorIot;
  
  @Mock
  DashboardConfigService dashboardConfigService;
  
  public static String datasourceCode = "datasourceCode";
  DashboardConfigDto dashboardConfigDto = new DashboardConfigDto();
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    dashboardConfigDto.getDatasources().add(new DatasourcesConfigDto());
    dashboardConfigDto.getDatasources().get(0).setDatasourceCode(datasourceCode);
  }
  
  @Test
  public void testPingDevice_FirstNo_SecondOK() throws IOException, ApsSystemException {
    when(connectorFactory.getConnector(Mockito.any())).thenReturn(baseConnectorIot);
    when(baseConnectorIot.refreshMetadata(Mockito.any(),Mockito.any())).thenReturn(dashboardConfigDto);
    when(baseConnectorIot.pingDevice(Mockito.any(),Mockito.any())).thenThrow(new ApiResourceNotAvailableException("404", "")).thenReturn(dashboardConfigDto);
    DashboardConfigDto x = connectorService.pingDevice(dashboardConfigDto, datasourceCode);
    assertEquals(x.getDatasources().get(0).getDatasourceCode(), dashboardConfigDto.getDatasources().get(0).getDatasourceCode());
  }
}
