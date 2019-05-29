package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.exception.ApiResourceNotAvailableException;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.factory.ConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceStatus;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementMapping;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils.getDatasource;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
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

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  public static String datasourceCode = "datasourceCode";
  DashboardConfigDto dto = new DashboardConfigDto();
  
  ApiResourceNotAvailableException exc = null;
  
  @Before
  public void setUp() {
    exc = new ApiResourceNotAvailableException("408", "ConnectorService error dashboard id 0, datasource code datasourceCode, can't communicate to Api Service");
    MockitoAnnotations.initMocks(this);
    dto.setId(0);
    dto.setType("GENERIC");
    dto.getDatasources().add(new DatasourcesConfigDto());
    dto.getDatasources().get(0).setDatasourceCode(datasourceCode);
    dto.getDatasources().get(0).setStatus(DatasourceStatus.ONLINE);
    when(dashboardConfigService.getDashboardConfig(0)).thenReturn(dto);
    when(connectorFactory.getConnector(Mockito.any())).thenReturn(baseConnectorIot);
    when(baseConnectorIot.refreshMetadata(Mockito.any(),Mockito.any())).thenReturn(dto);
  }
  
  @Test
  public void testPingDevice_FirstNo_SecondOK() {
    when(baseConnectorIot.pingDevice(Mockito.any(),Mockito.any()))
        .thenThrow(exc).thenReturn(dto);
    DashboardConfigDto x = connectorService.pingDevice(dto, datasourceCode);
    assertEquals(x.getDatasources().get(0).getDatasourceCode(), dto.getDatasources().get(0).getDatasourceCode());
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.ONLINE.toString());
  }

  @Test
  public void testPingDevice_FirstNo_SecondNO() {
    when(baseConnectorIot.pingDevice(Mockito.any(),Mockito.any())).thenThrow(exc);
    thrown.expect(ApiResourceNotAvailableException.class);
    thrown.expectMessage("ConnectorService error dashboard id " + dto.getId()
        + ", datasource code " + datasourceCode + ", can't communicate to Api Service");
    connectorService.pingDevice(dto, datasourceCode);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.OFFLINE.toString());
  }
  
  @Test
  public void getDeviceMeasurements_FirstNO_SecondOK() {
    List<Map<String,Object>> measurements = new ArrayList<>();
    measurements.add(new HashMap() {{
      put("latitude",112.322);
      put("longitude",589.322);
    }});
    when(baseConnectorIot.getMeasurements(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenThrow(exc).thenReturn(measurements);
    List<Map<String, Object>> x = connectorService
        .getDeviceMeasurements(dto, datasourceCode, null, null, null, DatasourceType.GENERIC);
    assertEquals(x.get(0),measurements.get(0));
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.ONLINE.toString());
  }
  
  @Test
  public void getDeviceMeasurements_FirstNO_SecondNO() {
    List<Map<String,Object>> measurements = new ArrayList<>();
    measurements.add(new HashMap() {{
      put("latitude",112.322);
      put("longitude",589.322);
    }});
    when(baseConnectorIot.getMeasurements(Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any())).thenThrow(exc);
    thrown.expect(ApiResourceNotAvailableException.class);
    thrown.expectMessage("ConnectorService error dashboard id " + dto.getId()
        + ", datasource code " + datasourceCode + ", can't communicate to Api Service");
    connectorService
        .getDeviceMeasurements(dto, datasourceCode, null, null, null, DatasourceType.GENERIC);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.OFFLINE.toString());
  }
  
  @Test
  public void getMeasurementsConfig_FirstNO_SecondOK() {
    MeasurementConfig mockConfig = new MeasurementConfig();
    mockConfig.setMappings(new ArrayList(){{add(new MeasurementMapping("x", "x"));}});
    when(baseConnectorIot.getMeasurementConfig(Mockito.any(),Mockito.any(), Mockito.any())).thenThrow(exc).thenReturn(mockConfig);
    MeasurementConfig measurementConfig = connectorService
        .getMeasurementsConfig(dto, datasourceCode, DatasourceType.GENERIC);
    assertEquals(mockConfig,measurementConfig);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.ONLINE.toString());
  }
  
  @Test
  public void getMeasurementsConfig_FirstNO_SecondNO() {
    MeasurementConfig mockConfig = new MeasurementConfig();
    mockConfig.setMappings(new ArrayList(){{add(new MeasurementMapping("x", "x"));}});
    when(baseConnectorIot.getMeasurementConfig(Mockito.any(),Mockito.any(), Mockito.any())).thenThrow(exc);
    thrown.expect(ApiResourceNotAvailableException.class);
    thrown.expectMessage("ConnectorService error dashboard id " + dto.getId()
        + ", datasource code " + datasourceCode + ", can't communicate to Api Service");
    connectorService
        .getMeasurementsConfig(dto, datasourceCode, DatasourceType.GENERIC);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.OFFLINE.toString());
  }
  
  @Test
  public void getDeviceMeasurementSchema_FirstNO_SecondOK() {
    MeasurementTemplate measurementTemplate = new MeasurementTemplate();
    when(baseConnectorIot.getDeviceMeasurementSchema(dto,datasourceCode,DatasourceType.GENERIC)).thenThrow(exc).thenReturn(measurementTemplate);
    MeasurementTemplate x = connectorService
        .getDeviceMeasurementSchema(dto, datasourceCode, DatasourceType.GENERIC);
    assertEquals(x, measurementTemplate);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.ONLINE.toString());
  }
  
  @Test
  public void getDeviceMeasurementSchema_FirstNO_SecondNO() {
    MeasurementTemplate measurementTemplate = new MeasurementTemplate();
    when(baseConnectorIot.getDeviceMeasurementSchema(dto,datasourceCode,DatasourceType.GENERIC)).thenThrow(exc);
    thrown.expect(ApiResourceNotAvailableException.class);
    thrown.expectMessage("ConnectorService error dashboard id " + dto.getId()
        + ", datasource code " + datasourceCode + ", can't communicate to Api Service");
    connectorService
        .getDeviceMeasurementSchema(dto, datasourceCode, DatasourceType.GENERIC);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.OFFLINE.toString());
  }
  
  @Test
  public void refreshMetadataOK() {
    when(baseConnectorIot.refreshMetadata(dto,datasourceCode)).thenReturn(dto);
    connectorService.refreshMetadata(dto,datasourceCode);
    assertEquals(getDatasource(dto,datasourceCode).getStatus(), DatasourceStatus.ONLINE.toString());
  }
  
  @Test
  public void refreshMetadataExc() {
    when(baseConnectorIot.refreshMetadata(dto,datasourceCode)).thenThrow(exc);
    thrown.expect(ApiResourceNotAvailableException.class);
    thrown.expectMessage("ConnectorService error dashboard id " + dto.getId()
        + ", datasource code " + datasourceCode + ", can't communicate to Api Service");
    connectorService.refreshMetadata(dto,datasourceCode);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.OFFLINE.toString());
  }
  
  @Test
  public void getDeviceLocations_FirstNo_SecondOK() {
    DeviceLocations mockLocations = new DeviceLocations();
    mockLocations.setLatitude(1.23);
    mockLocations.setLongitude(4.56);
    when(baseConnectorIot.getDeviceLocations(dto,datasourceCode)).thenThrow(exc).thenReturn(mockLocations);
    DeviceLocations deviceLocations = connectorService.getDeviceLocations(dto,datasourceCode);
    assertEquals(deviceLocations,mockLocations);
    assertEquals(getDatasource(dto,datasourceCode).getStatus(), DatasourceStatus.ONLINE.toString());
  }
  
  @Test
  public void getDeviceLocations_FirstNo_SecondNO() {
    DeviceLocations mockLocations = new DeviceLocations();
    mockLocations.setLatitude(1.23);
    mockLocations.setLongitude(4.56);
    when(baseConnectorIot.getDeviceLocations(dto,datasourceCode)).thenThrow(exc);
    thrown.expect(ApiResourceNotAvailableException.class);
    thrown.expectMessage("ConnectorService error dashboard id " + dto.getId()
        + ", datasource code " + datasourceCode + ", can't communicate to Api Service");
    connectorService.getDeviceLocations(dto,datasourceCode);
    assertEquals(getDatasource(dto, datasourceCode).getStatus(), DatasourceStatus.OFFLINE.toString());
  }
  
  @Test
  public void isParcheggioAvailable_FirstNO_SecondOK() {
    boolean mockResult = true;
    when(baseConnectorIot.isParcheggioAvailable(dto,datasourceCode)).thenThrow(exc).thenReturn(mockResult);
    boolean result = connectorService.isParcheggioAvailable(dto, datasourceCode);
    assertEquals(result,mockResult);
    assertEquals(getDatasource(dto,datasourceCode).getStatus(), DatasourceStatus.ONLINE.toString());
  }

  @Test
  public void isParcheggioAvailable_FirstNO_SecondoNO() {
    when(baseConnectorIot.isParcheggioAvailable(dto,datasourceCode)).thenThrow(exc);
    thrown.expect(ApiResourceNotAvailableException.class);
    thrown.expectMessage("ConnectorService error dashboard id " + dto.getId()
        + ", datasource code " + datasourceCode + ", can't communicate to Api Service");
    connectorService.isParcheggioAvailable(dto, datasourceCode);
    assertEquals(getDatasource(dto,datasourceCode).getStatus(), DatasourceStatus.OFFLINE.toString());
  }
}
