package org.entando.entando.plugins.jpsitewhereconnector.aps.iot;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.apache.commons.lang3.StringUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementMapping;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IMeasurementConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.MeasurementTemplateService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.ServerType;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto.DashboardSitewhereDatasourceDto;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto.SitewhereApplicationConfigDto;
import org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.service.SitewhereConnectorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.entando.entando.plugins.jpsitewhereconnector.aps.iot.TestUtils.SITEWHERE_ASSIGNMENT_ID;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.iot.TestUtils.SITEWHERE_HARDWARE_ID;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.iot.TestUtils.SITEWHERE_SERVER_TEST_ADDRESS;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.iot.TestUtils.SITEWHERE_TENANT_TOKEN;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.SitewhereConstants.SITEWHERE_SERVER_TYPE;
import static org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.SitewhereConstants.SITEWHERE_TENANT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class TestSitewhereConnectorService {

  @InjectMocks
  SitewhereConnectorService sitewhereConnectorService;

  @Mock
  IMeasurementConfigService iMeasurementConfigService;

  @Mock
  ConnectorFactory connectorFactory;

  @Mock
  MeasurementTemplateService measurementTemplateService;

  DashboardSitewhereDatasourceDto dashboardDatasourceDto;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    dashboardDatasourceDto = new DashboardSitewhereDatasourceDto();
    DashboardConfigDto dashboardConfigDto = new DashboardConfigDto();
    dashboardConfigDto.setDatasources(new ArrayList<>());
    dashboardConfigDto.setUsername("admin");
    dashboardConfigDto.setPassword("password");

    SitewhereApplicationConfigDto swDatasource = new SitewhereApplicationConfigDto();
    dashboardDatasourceDto.setDashboardConfigDto(dashboardConfigDto);
    
    dashboardDatasourceDto
        .setDashboardUrl(StringUtils.join(SITEWHERE_SERVER_TEST_ADDRESS, "/sitewhere"));


    dashboardConfigDto.setServerDescription(SITEWHERE_SERVER_TYPE);
    dashboardConfigDto.setToken(SITEWHERE_TENANT_TOKEN);

    swDatasource.setDatasourceCode(SITEWHERE_HARDWARE_ID);

    dashboardDatasourceDto.setDashboardConfigDto(dashboardConfigDto);
    dashboardDatasourceDto.setSitewhereDatasourceConfigDto(swDatasource);

    Mockito.when(connectorFactory.getDashboardDatasource(Mockito.any())).thenReturn(dashboardDatasourceDto);
    
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
    List<? extends AbstractDashboardDatasourceDto> devices = sitewhereConnectorService
        .getAllDevices(dashboardDatasourceDto.getDashboardConfigDto());
    assertNotNull(devices);
    assertTrue(devices.size() > 0);
  }

  @Test
  public void testSetMeasurementTemplate() throws ApsSystemException {
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setDatasourceCode(SITEWHERE_HARDWARE_ID);
    dashboardDatasourceDto.getDashboardConfigDto().setToken(TestUtils.SITEWHERE_TENANT_TOKEN);
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setAssignmentId(SITEWHERE_ASSIGNMENT_ID);

    ArgumentCaptor<MeasurementTemplate> captor = ArgumentCaptor.forClass(MeasurementTemplate.class);
    sitewhereConnectorService.saveMeasurementTemplate(dashboardDatasourceDto);
    Mockito.verify(measurementTemplateService).save(captor.capture());
    MeasurementTemplate template = captor.getValue();
    assertNotNull(template);
    assertEquals(template.getFields().size(), 2);

  }

  @Test
  public void testGetMeasurements() throws ApsSystemException {
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setDatasourceCode(SITEWHERE_HARDWARE_ID);
    dashboardDatasourceDto.getDashboardConfigDto().setToken(TestUtils.SITEWHERE_TENANT_TOKEN);
    dashboardDatasourceDto.getSitewhereDatasourceConfigDto().setAssignmentId(SITEWHERE_ASSIGNMENT_ID);
    MeasurementTemplate measurementTemplate = new MeasurementTemplate();
    measurementTemplate.setId("x");

    MeasurementConfig config = new MeasurementConfig();
    config.setMeasurementConfigId("xx");
    config.setDatasourceCode(dashboardDatasourceDto.getDatasourceCode());
    config.setDashboardId(dashboardDatasourceDto.getDashboardId());
    config.setMeasurementTemplateId(measurementTemplate.getId());

    MeasurementMapping mappingTemperatura = new MeasurementMapping();
    mappingTemperatura.setSourceName("temperature");
    mappingTemperatura.setDetinationName("Temperatura_nuovoNome");
    mappingTemperatura.setTransformerClass("java.lang.Double");
    config.addMapping(mappingTemperatura);

    MeasurementMapping mappingDistanza = new MeasurementMapping();
    mappingDistanza.setSourceName("distance");
    mappingDistanza.setDetinationName("Distanza_nuovoNome");
    mappingDistanza.setTransformerClass("java.lang.Double");
    config.addMapping(mappingDistanza);
    
    Mockito.when(measurementTemplateService.getByDashboardIdAndDatasourceCode(dashboardDatasourceDto.getDashboardId(), dashboardDatasourceDto.getDatasourceCode())).thenReturn(measurementTemplate);
    Mockito.when(iMeasurementConfigService.getByDashboardIdAndDatasourceCodeAndMeasurementTemplateId(dashboardDatasourceDto.getDashboardId(), dashboardDatasourceDto.getDatasourceCode(),measurementTemplate.getId()))
        .thenReturn(config);

    long nMeasurements = 1L;
    int nMeasurementsSentPerCall = 2;
    List<MeasurementObject> measurements = sitewhereConnectorService.getMeasurements(dashboardDatasourceDto,
        nMeasurements,null, null);
//    System.out.println(new Gson().toJson(measurements, ArrayList.class));
    assertTrue(measurements != null);
    assertEquals(measurements.size() ,nMeasurements * nMeasurementsSentPerCall);
    measurements.forEach(meas -> assertNotNull(meas.getEntandoReceivedDate()));
    measurements.forEach(meas -> assertNotNull(meas.getName()));
    measurements.forEach(meas -> assertNotNull(meas.getMeasure()));
  }
}
