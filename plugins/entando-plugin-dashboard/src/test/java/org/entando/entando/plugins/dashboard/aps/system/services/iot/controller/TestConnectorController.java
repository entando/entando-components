package org.entando.entando.plugins.dashboard.aps.system.services.iot.controller;

import com.agiletec.aps.system.services.user.UserDetails;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.TestUtils;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.storage.IotMessageDto;
import org.entando.entando.plugins.dashboard.web.iot.ConnectorController;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        .setHandlerExceptionResolvers(TestUtils.createHandlerExceptionResolver())
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

    JsonObject response = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class);

    assertEquals(response.get("errors").getAsJsonArray().get(0).getAsJsonObject()
        .get("message").getAsString(),"a Datasource with " + datasourceCode + " code could not be found");
  }

  @Test
  public void testGetMeasurement() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin()
        .build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";
    Long nMeasurements = 2L;
    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());


    List<Map<String,Object>> measurements = new ArrayList<>();
    measurements.add(new HashMap<String, Object>(){{
      put("temperature", 30);
      put("timestamp", 123456789);
    }});

    measurements.add(new HashMap<String, Object>(){{
      put("temperature", 25);
      put("timestamp", 987654321);
    }});

    Instant startDate = Instant.ofEpochMilli(1553814852743L);
    Instant endDate = Instant.now();
    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService
        .getDeviceMeasurements(Mockito.any(), Mockito.any(), Mockito.any(),
            Mockito.any())).thenReturn(measurements);
    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode + "/data")
            .param("nMeasurements", nMeasurements.toString())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().isOk());

    JsonArray jsonMeasurements = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload").getAsJsonArray();

    List<Map<String, Object>> resultList = new ArrayList();

    for (JsonElement elem:jsonMeasurements) {
      Map<String, Object> map = new HashMap();
      for (String e : elem.getAsJsonObject().keySet()) {
        map.put(e, elem.getAsJsonObject().get(e).getAsString());
      }
      resultList.add(map);
    }

    for (int i = 0; i< resultList.size(); i++) {
      assertEquals(resultList.get(i).keySet(), measurements.get(i).keySet());
    }
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
//    Mockito.when(connectorService.getDeviceMeasurements(mockDto, Date.from(startDate), Date.from(endDate),
//            new RestListRequest())).thenReturn(measurement);
    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode + "/data")
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
    JsonObject response = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class);

    assertEquals(response.get("errors").getAsJsonArray().get(0).getAsJsonObject()
        .get("message").getAsString(),"a Server with " + dashboardId + " code could not be found");
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
//    Mockito.when(connectorService
//        .getDeviceMeasurements(mockDto, Date.from(startDate), Date.from(endDate),
//            new RestListRequest())).thenReturn(pagedMetadata);
    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId + "/datasource/" + datasourceCode + "/data")
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());

    JsonObject response = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class);

    assertEquals(response.get("errors").getAsJsonArray().get(0).getAsJsonObject()
        .get("message").getAsString(),"a Datasource with " + datasourceCode + " code could not be found");
  }

}
