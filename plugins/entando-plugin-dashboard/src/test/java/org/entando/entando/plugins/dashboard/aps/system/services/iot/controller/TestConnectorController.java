package org.entando.entando.plugins.dashboard.aps.system.services.iot.controller;

import com.agiletec.aps.system.services.user.UserDetails;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.web.iot.ConnectorController;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.utils.OAuth2TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestConnectorController extends AbstractControllerTest {

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

    
    List<MeasurementObject> measurementObjects = new ArrayList<>();
    MeasurementObject measurementObject = new MeasurementObject();
    measurementObject.setName("misuraX");
    measurementObject.setMeasure("valoreX");
    MeasurementObject measurementObject1 = new MeasurementObject();
    measurementObject1.setName("misuraY");
    measurementObject1.setMeasure("valoreY");
    measurementObjects.add(measurementObject);
    measurementObjects.add(measurementObject1);
    PagedMetadata<MeasurementObject> pagedMetadata = new PagedMetadata<>();
    pagedMetadata.setBody(measurementObjects);
    
    Instant startDate = Instant.ofEpochMilli(1553814852743L);
    Instant endDate = Instant.now();
    Mockito.when(dashboardConfigService.existsById(dashboardId)).thenReturn(true);
    Mockito.when(dashboardConfigService.getDashboardDatasourceDto(dashboardId, datasourceCode)).thenReturn(mockDto);
    Mockito.when(connectorService
        .getDeviceMeasurements(mockDto, nMeasurements , Date.from(startDate), Date.from(endDate),
            new RestListRequest())).thenReturn(pagedMetadata);
    ResultActions result = mockMvc.perform(
        get("/plugins/dashboard/server/" + dashboardId + "/datasource/" + datasourceCode + "/data")
            .param("nMeasurements", nMeasurements.toString())
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString())
            .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(status().isOk());
//    Mockito.verify(groupService, Mockito.times(1)).getGroups(restListReq);
  }


}
