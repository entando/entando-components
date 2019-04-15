package org.entando.entando.plugins.dashboard.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.web.iot.ConnectorController;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class ConnectorControllerTest extends AbstractControllerTest {

  @Mock
  private IDashboardConfigService dashboardConfigService;

  @Mock
  private IConnectorService connectorService;

  @Mock
  private HttpSession httpSession;

  @InjectMocks
  private ConnectorController controller;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .addInterceptors(entandoOauth2Interceptor)
        .setHandlerExceptionResolvers(createHandlerExceptionResolver())
        .build();
  }

  @Test
  public void when_GetMeasurement_WhenServerNotExist_thanError() throws Exception {
    when(this.dashboardConfigService.existsById(Mockito.anyInt())).thenReturn(false);

    String accessToken = "token";
    ResultActions result =mockMvc.perform(get("/plugins/dashboard/server/2/datasource/3/data")
        .header("Authorization", "Bearer " + accessToken));
    result.andExpect(status().is4xxClientError());
  }

  @Test
  public void when_GetMeasurement_WhenServerExistAndDashboardCodeNotExist_thanError() throws Exception {

    when(this.dashboardConfigService.existsById(Mockito.anyInt())).thenReturn(true);
    when(dashboardConfigService.getDashboardConfig(Mockito.anyInt())).thenReturn(new DashboardConfigDto());

    String accessToken = "token";
    ResultActions result =mockMvc.perform(get("/plugins/dashboard/server/2/datasource/3/data")
        .header("Authorization", "Bearer " + accessToken));
    result.andExpect(status().is4xxClientError());

  }

  @Test
  public void should_GetExistingMeasurement_When_ServerExist() throws Exception {

    Map<String, Object> mappa1 = new HashMap<String, Object>();
    mappa1.put("temperature", 2.3);
    mappa1.put("timestamp", "2019-03-04 12:13:14");

    Map<String, Object> mappa2 = new HashMap<String, Object>();
    mappa2.put("temperature", 12.3);
    mappa2.put("timestamp", "2019-03-04 13:13:14");


    List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();
    lista.add(mappa1);
    lista.add(mappa2);

    RestListRequest requestList = new RestListRequest();
    PagedMetadata<Map<String, Object>> pagedMetadata = new PagedMetadata<>(requestList, 1);
    pagedMetadata.setBody(lista);
    DashboardConfigDto dto = new DashboardConfigDto();
    String datasourceCode = "";
    when(this.dashboardConfigService.existsById(Mockito.anyInt())).thenReturn(true);
    when(connectorService.getDeviceMeasurements(dto, datasourceCode, null,null, requestList)).thenReturn(lista);

    String accessToken = "token";
    ResultActions result =mockMvc.perform(get("/plugins/dashboard/server/2/datasource/3/data")
        .header("Authorization", "Bearer " + accessToken));
    result.andExpect(status().is2xxSuccessful());

    System.out.println("respt " + result.andReturn().getResponse().getContentAsString());

    //TODO check 
  }





}
