package org.entando.entando.plugins.dashboard.aps.system.services.iot.controller;

import com.agiletec.aps.system.services.user.UserDetails;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementMapping;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.entando.entando.plugins.dashboard.web.dashboardconfig.DashboardConfigController;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestDashboardConfigController extends AbstractControllerTest {

  public static final String BASE_PATH = "/plugins/dashboard/dashboardConfigs/";
  @InjectMocks
  DashboardConfigController controller;
  
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
  public void testPingServerOK() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(connectorService.pingServer(Mockito.any())).thenReturn(true);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" +dashboardId+"/ping")
            .param("serverId", "1")
            .header("Authorization", "Bearer " + accessToken)
    );
    String payload = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload").getAsString();
    assertEquals(payload, "true");
    result.andExpect(status().isOk());
  }

  @Test
  public void testPingServer404() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(false);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" +dashboardId+"/ping")
            .param("serverId", "1")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().isNotFound());
  }
  
  @Test
  public void testPingServerFalse() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(connectorService.pingServer(Mockito.any())).thenReturn(false);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" +dashboardId+"/ping")
            .param("serverId", "1")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().isOk());
    String payload = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload").getAsString();
    assertEquals(payload, "false");
  }

  @Test
  public void testPingDatasourceOK() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.pingDevice(mockDto)).thenReturn(true);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/ping")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().isOk());
    String payload = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload").getAsString();
    assertEquals(payload, "true");
  }

  @Test
  public void testPingDatasource404Server() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(false);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/ping")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testPingDatasource404Device() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/ping")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testPingDatasourceFalse() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.pingDevice(Mockito.any())).thenReturn(false);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/ping")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is2xxSuccessful());
    String payload = new Gson()
        .fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload").getAsString();
    assertEquals(payload, "false");
  }

  @Test
  public void testGetMeasurementPreview() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    MeasurementTemplate measurementTemplate = new MeasurementTemplate();
    measurementTemplate.getFields().add(new MeasurementType("temp", "int"));
    measurementTemplate.getFields().add(new MeasurementType("time", "long"));

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.getDeviceMeasurementSchema(Mockito.any())).thenReturn(measurementTemplate);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/preview")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is2xxSuccessful());
    JsonElement jsonPayload = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload");
    MeasurementTemplate payload = IoTUtils.getObjectFromJson(jsonPayload,new TypeToken<MeasurementTemplate>(){}.getType(),MeasurementTemplate.class);
    assertEquals(payload, measurementTemplate);
  }

  @Test
  public void testGetMeasurementPreview404Server() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    MeasurementTemplate measurementTemplate = new MeasurementTemplate();

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(false);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.getDeviceMeasurementSchema(Mockito.any())).thenReturn(measurementTemplate);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/preview")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetMeasurementPreview404Device() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(null);

    MeasurementTemplate measurementTemplate = new MeasurementTemplate();

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.getDeviceMeasurementSchema(Mockito.any())).thenReturn(measurementTemplate);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/preview")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetMeasurementColumns() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    MeasurementConfig measurementConfig = new MeasurementConfig();
    measurementConfig.getMappings().add(new MeasurementMapping("temp", "temp"));
    measurementConfig.getMappings().add(new MeasurementMapping("time", "time"));

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.getMeasurementsConfig(Mockito.any())).thenReturn(measurementConfig);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/columns")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is2xxSuccessful());
    JsonElement jsonPayload = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload");
    MeasurementConfig payload = IoTUtils.getObjectFromJson(jsonPayload,new TypeToken<MeasurementConfig>(){}.getType(),MeasurementConfig.class);
    assertEquals(payload, measurementConfig);
  }

  @Test
  public void testGetMeasurementColumns404Server() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(new DatasourcesConfigDto());

    MeasurementConfig measurementConfig = new MeasurementConfig();

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(false);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.getMeasurementsConfig(Mockito.any())).thenReturn(measurementConfig);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/columns")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetMeasurementColumns404Device() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(null);

    MeasurementConfig measurementConfig = new MeasurementConfig();

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId,datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService.getMeasurementsConfig(Mockito.any())).thenReturn(measurementConfig);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/datasource/"+ datasourceCode +"/columns")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void testGetAllDevices() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(null);

    List<DatasourcesConfigDto> mockDatasources = new ArrayList<>();
    DatasourcesConfigDto datasource = new DatasourcesConfigDto();
    datasource.setDatasourceCode("1");
    DatasourcesConfigDto datasource2= new DatasourcesConfigDto();
    datasource.setDatasourceCode("2");
    DatasourcesConfigDto datasource3 = new DatasourcesConfigDto();
    datasource.setDatasourceCode("3");
    mockDatasources.add(datasource);
    mockDatasources.add(datasource2);
    mockDatasources.add(datasource3);
    
    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(connectorService.getAllDevices(Mockito.any())).thenReturn(mockDatasources);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/getAllDevices")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is2xxSuccessful());
    
    JsonElement jsonPayload = new Gson().fromJson(result.andReturn().getResponse().getContentAsString(), JsonObject.class)
        .get("payload");
    List<DatasourcesConfigDto> datasources = IoTUtils.getObjectFromJson(jsonPayload,new TypeToken<List<DatasourcesConfigDto>>(){}.getType(),ArrayList.class);
    assertEquals(datasources, mockDatasources);
  }
  @Test
  public void testGetAllDevicesNullResponse() throws Exception {
    UserDetails user = new OAuth2TestUtils.UserBuilder("admin", "adminadmin").grantedToRoleAdmin().build();
    String accessToken = mockOAuthInterceptor(user);
    int dashboardId = 1;
    String datasourceCode = "1";

    DashboardDatasourceDto mockDto = new DashboardDatasourceDto();
    mockDto.setDashboardConfigDto(new DashboardConfigDto());
    mockDto.setDatasourcesConfigDto(null);

    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(connectorService.getAllDevices(Mockito.any())).thenReturn(null);

    ResultActions result = mockMvc.perform(
        get(BASE_PATH + "server/" + dashboardId +"/getAllDevices")
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().is2xxSuccessful());
  }
  
}
