package org.entando.entando.plugins.jpsitewhereconnector.aps.iot;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.ServerType;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto.DashboardSitewhereDatasourceDto;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto.SitewhereApplicationConfigDto;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.service.SitewhereConnectorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

import static org.entando.entando.plugins.jpsitewhereconnector.aps.iot.TestUtils.SITEWHERE_ASSIGNMENT_ID;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.iot.TestUtils.SITEWHERE_HARDWARE_ID;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.iot.TestUtils.SITEWHERE_SERVER_TEST_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestSitewhereConnectorService {

  @InjectMocks
  SitewhereConnectorService sitewhereConnectorService;

  @Mock
  IMeasurementConfigService iMeasurementConfigService;
  
  DashboardSitewhereDatasourceDto dashboardDatasourceDto;
  
  @Before 
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    
    DashboardConfigDto dashboardConfigDto = new DashboardConfigDto();
    dashboardConfigDto.setDatasources(new ArrayList<>());
    dashboardConfigDto.setUsername("admin");
    dashboardConfigDto.setPassword("password");

    DatasourcesConfigDto datasourcesConfigDto = new SitewhereApplicationConfigDto();
    datasourcesConfigDto.setDatasource(ServerType.SITEWHERE.toString()); // TODO Sitewhere deve essere scritto nel dashboardConfig?

    dashboardDatasourceDto = new DashboardSitewhereDatasourceDto();
    dashboardDatasourceDto.setDashboardConfigDto(dashboardConfigDto);
    dashboardDatasourceDto.setDatasourcesConfigDto(datasourcesConfigDto);

    dashboardDatasourceDto
        .setDashboardUrl(StringUtils.join(SITEWHERE_SERVER_TEST_ADDRESS, "/sitewhere"));

    dashboardConfigDto.getDatasources().add(datasourcesConfigDto);
  }

  @Test
  public void testPingDevice() throws IOException {
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setDatasourceCode(SITEWHERE_HARDWARE_ID);
    dashboardDatasourceDto.getDashboardConfigDto().setToken(TestUtils.SITEWHERE_TENANT_TOKEN);
    assertTrue(sitewhereConnectorService.pingDevice(dashboardDatasourceDto));
  }
  
  @Test
  public void testGetAllDevices() {
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setDatasourceCode(SITEWHERE_HARDWARE_ID);
    dashboardDatasourceDto.getDashboardConfigDto().setToken(TestUtils.SITEWHERE_TENANT_TOKEN);
    assertNotNull(
        sitewhereConnectorService.getAllDevices(dashboardDatasourceDto.getDashboardConfigDto()));
  }
  
  @Test
  public void testSetMeasurementSchema() throws ApsSystemException {
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setDatasourceCode(SITEWHERE_HARDWARE_ID);
    dashboardDatasourceDto.getDashboardConfigDto().setToken(TestUtils.SITEWHERE_TENANT_TOKEN);
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setAssignmentId(SITEWHERE_ASSIGNMENT_ID);
//    assertEquals(sitewhereConnectorService.getSchema(dashboardDatasourceDto), new MeasurementConfig());
  }
  
  @Test
  public void testGetMeasurements() {
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setDatasourceCode(SITEWHERE_HARDWARE_ID);
    dashboardDatasourceDto.getDashboardConfigDto().setToken(TestUtils.SITEWHERE_TENANT_TOKEN);
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setAssignmentId(SITEWHERE_ASSIGNMENT_ID);
    JsonArray measurements = sitewhereConnectorService.getMeasurements(dashboardDatasourceDto, 2L,Instant.now(), Instant.now());
    assertTrue(measurements != null);
  }
  
}
