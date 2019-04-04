package org.entando.entando.plugins.dashboard.aps.system.services.iot.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.storage.IotMessageDto;
import org.entando.entando.plugins.dashboard.web.iot.ConnectorController;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.agiletec.aps.system.services.user.UserDetails;

public class TestConnectorController extends AbstractControllerTest {

  public static final String BASE_PATH = "/plugins/dashboard/";
  @InjectMocks
  ConnectorController controller;

  @Mock
  IDashboardConfigService dashboardConfigService;

  @Mock
  IConnectorService connectorService;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .addInterceptors(entandoOauth2Interceptor)
        .setHandlerExceptionResolvers(createHandlerExceptionResolver())
        .build();
  }

  @Test
  public void testSaveMeasurementOK() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin()
        .build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";
    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    String measurement = "{\"temperature\" : 2, \"timestamp\" : 123456789}";

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);
    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    ResultActions result = mockMvc.perform(
        post(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode)
            .contentType(MediaType.TEXT_PLAIN)
            .content(measurement)
    );
    result.andExpect(status().isOk());
    Mockito.verify(connectorService).saveDeviceMeasurement(Mockito.any(), captor.capture());
    assertEquals(captor.getValue(), measurement);
  }

  @Test
  public void testSaveMeasurement404Server() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin()
        .build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";
    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    String measurement = "{\"temperature\" : 2, \"timestamp\" : 123456789}";

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(false);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);

    ResultActions result = mockMvc.perform(
        post(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode)
            .contentType(MediaType.TEXT_PLAIN)
            .content(measurement)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testSaveMeasurement404Device() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin()
        .build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";
    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(null);

    String measurement = "{\"temperature\" : 2, \"timestamp\" : 123456789}";

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);

    ResultActions result = mockMvc.perform(
        post(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode)
            .contentType(MediaType.TEXT_PLAIN)
            .content(measurement)
    );
    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetMeasurement() throws Exception {
//    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin()
//        .build();
//    String accessToken = mockOAuthInterceptor(user);
//    int dashboardId = 1;
//    String datasourceCode = "1";
//    Long nMeasurements = 2L;
//    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
//    mockDto.setDashboardConfigDto(new DashboardConfigDto());
//    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());
//
//
//    List<MeasurementObject> measurementObjects = new ArrayList<>();
//    MeasurementObject measurementObject = new MeasurementObject();
//    measurementObject.setName("misuraX");
//    measurementObject.setMeasure("valoreX");
//    MeasurementObject measurementObject1 = new MeasurementObject();
//    measurementObject1.setName("misuraY");
//    measurementObject1.setMeasure("valoreY");
//    measurementObjects.add(measurementObject);
//    measurementObjects.add(measurementObject1);
//    PagedMetadata<MeasurementObject> pagedMetadata = new PagedMetadata<>();
//    pagedMetadata.setBody(measurementObjects);
//
//    Instant startDate = Instant.ofEpochMilli(1553814852743L);
//    Instant endDate = Instant.now();
//    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
//    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);
//    Mockito.when(connectorService
//        .getDeviceMeasurements(mockDto, nMeasurements , Date.from(startDate), Date.from(endDate),
//            new RestListRequest())).thenReturn(pagedMetadata);
//    ResultActions result = mockMvc.perform(
//        get(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode + "/data")
//            .param("nMeasurements", nMeasurements.toString())
//            .param("startDate", startDate.toString())
//            .param("endDate", endDate.toString())
//            .header("Authorization", "Bearer " + accessToken)
//    );
//
//    result.andExpect(status().isOk());
//
//    JsonElement jsonPayload = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
//        .get("payload");
//    List<MeasurementObject> payload = IoTUtils
//        .getObjectFromJson(jsonPayload,new TypeToken<List<MeasurementObject>>(){}.getType(),ArrayList.class);
//    assertEquals(payload, measurementObjects);
  }


  @Test
  public void testGetMeasurement404Server() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin()
        .build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";
    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());


    List<IotMessageDto> measurementObjects = new ArrayList<>();
    PagedMetadata<IotMessageDto> pagedMetadata = new PagedMetadata<>();
    pagedMetadata.setBody(measurementObjects);

    Instant startDate = Instant.ofEpochMilli(1553814852743L);
    Instant endDate = Instant.now();
    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(false);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.getDeviceMeasurements(mockDto, Date.from(startDate), Date.from(endDate),
            new RestListRequest())).thenReturn(measurement);
    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode + "/data")
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetMeasurement404Device() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin()
        .build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";
    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(null);


    List<IotMessageDto> measurementObjects = new ArrayList<>();
    PagedMetadata<IotMessageDto> pagedMetadata = new PagedMetadata<>();
    pagedMetadata.setBody(measurementObjects);

    Instant startDate = Instant.ofEpochMilli(1553814852743L);
    Instant endDate = Instant.now();
    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService
        .getDeviceMeasurements(mockDto, Date.from(startDate), Date.from(endDate),
            new RestListRequest())).thenReturn(pagedMetadata);
    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode + "/data")
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

}
