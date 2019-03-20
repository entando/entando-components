package org.entando.entando.plugins.jpkaaconnector.aps.iot;

import com.agiletec.aps.system.exception.ApsSystemException;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto.DashboardKaaDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto.KaaApplicationConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.service.KaaConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.MeasurementTemplateService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.ServerType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.entando.entando.plugins.jpkaaconnector.aps.iot.TestUtils.KAA_APPLICATION_TOKEN;
import static org.entando.entando.plugins.jpkaaconnector.aps.iot.TestUtils.KAA_CTL_SCHEMA_ID;
import static org.entando.entando.plugins.jpkaaconnector.aps.iot.TestUtils.KAA_SERVER_TEST_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class TestKaaConnectorService {

  @InjectMocks
  KaaConnectorService kaaConnectorService;

  @Mock
  IMeasurementConfigService measurementConfigService;

  @Mock
  MeasurementTemplateService measurementTemplateService;

  DashboardKaaDatasourceDto dashboardDatasourceDto;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    DashboardConfigDto dashboardConfigDto = new DashboardConfigDto();
    dashboardConfigDto.setDatasources(new ArrayList<>());
    dashboardConfigDto.setUsername("devuser");
    dashboardConfigDto.setPassword("devuser123");

    DatasourcesConfigDto datasourcesConfigDto = new KaaApplicationConfigDto();
    datasourcesConfigDto.setDatasource(ServerType.KAA.toString()); // TODO Kaa deve essere scritto nel dashboardConfig?

    dashboardDatasourceDto = new DashboardKaaDatasourceDto();
    dashboardDatasourceDto.setDashboardConfigDto(dashboardConfigDto);
    dashboardDatasourceDto.setDatasourcesConfigDto(datasourcesConfigDto);

    dashboardDatasourceDto
        .setDashboardUrl(StringUtils.join(KAA_SERVER_TEST_ADDRESS, "/kaaAdmin"));

    dashboardConfigDto.getDatasources().add(datasourcesConfigDto);

  }

  @Test
  public void testGetDevices() {
    List<DashboardKaaDatasourceDto> devices = (List<DashboardKaaDatasourceDto>) kaaConnectorService
        .getAllDevices(dashboardDatasourceDto.getDashboardConfigDto());
    assertNotEquals(devices, null);
    devices.forEach(device -> assertNotEquals(device, null));
    assertEquals(devices.size(), 17);
  }

  @Test
  public void testPingDevice() {
    dashboardDatasourceDto.getKaaDatasourceConfigDto().setDatasourceCode(KAA_APPLICATION_TOKEN);
    assertEquals(kaaConnectorService.pingDevice(dashboardDatasourceDto), true);
  }

  @Test
  public void setDeviceMeasurementSchema() throws ApsSystemException {
    dashboardDatasourceDto.getKaaDatasourceConfigDto().setDatasourceCode(KAA_APPLICATION_TOKEN);
    dashboardDatasourceDto.getKaaDatasourceConfigDto().setLoggerId(KAA_CTL_SCHEMA_ID);


    doAnswer((Answer) invocation -> {
      MeasurementConfig arg0 = invocation.getArgument(0);
      System.out.println(arg0.getMappings());
      return null;
    }).when(measurementTemplateService).save(any());
    
    kaaConnectorService.saveMeasurementTemplate(dashboardDatasourceDto);

    
  }

  @Test
  public void getDeviceLogAppenders() {
    dashboardDatasourceDto.getKaaDatasourceConfigDto().setDatasourceCode(KAA_APPLICATION_TOKEN);
    assertTrue(kaaConnectorService.getLogAppenders(dashboardDatasourceDto).size() > 0);
  }

}
