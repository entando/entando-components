package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;

import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigManager;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestDashboardConfigService {

  @InjectMocks
  DashboardConfigService dashboardConfigService;
  
  @Mock
  DashboardConfigManager dashboardConfigManager;
  
  @Mock
  DtoBuilder<DashboardConfig, DashboardConfigDto> dtoBuilder;
  
  DashboardConfigDto dashboard;
    
  @Before
  public void setUp() {
    dashboard = new DashboardConfigDto();
    dashboard.setActive(true);
    dashboard.setDebug(false);
    dashboard.setId(1111);
    dashboard.setPassword("pw");
    dashboard.setUsername("user");
    dashboard.setType("SITEWHERE");
    DatasourcesConfigDto datasourceGATE = new DatasourcesConfigDto();
    datasourceGATE.setType(DatasourceType.GATE);
    DatasourcesConfigDto datasourceGENERIC = new DatasourcesConfigDto();
    datasourceGENERIC.setType(DatasourceType.GENERIC);
    DatasourcesConfigDto datasourceGEODATA = new DatasourcesConfigDto();
    datasourceGEODATA.setType(DatasourceType.GEODATA);
    dashboard.setDatasources(new ArrayList(){{add(datasourceGATE); add(datasourceGENERIC); add(datasourceGEODATA);}});
  }
  
  @Test
  public void test_getDashboardConfigsFilterByDatasourceType() throws ApsSystemException {
    DashboardConfig dashboardEntity = new DashboardConfig();
    when(dashboardConfigManager.getDashboardConfig(anyInt())).thenReturn(dashboardEntity);
    when(dtoBuilder.convert((DashboardConfig) any())).thenReturn(dashboard);
    DashboardConfigDto result = dashboardConfigService
        .getDashboardConfig(1111, DatasourceType.GATE);
    assertEquals(1, result.getDatasources().size());
    result = dashboardConfigService
        .getDashboardConfig(1111, DatasourceType.GENERIC);
    assertEquals(0, result.getDatasources().size());
    assertEquals(0,result.getDatasources().size());
  }
  
  @Test
  public void test_getDashboardConfigs_filterGATE() throws ApsSystemException {
    SearcherDaoPaginatedResult dashboardConfigs = new SearcherDaoPaginatedResult();
    dashboardConfigs.setCount(2);
    DashboardConfigDto dashboardNoGATE = new DashboardConfigDto();
    dashboardConfigs.setList(new ArrayList(){{add(dashboard); add(dashboardNoGATE);}});
    when(dashboardConfigManager.getDashboardConfigs((List<FieldSearchFilter>) any())).thenReturn(dashboardConfigs);
    when(dtoBuilder.convert((List<DashboardConfig>) any())).thenReturn(new ArrayList(){{add(dashboard); add(dashboardNoGATE);}});
    PagedMetadata<DashboardConfigDto> result = dashboardConfigService
        .getDashboardConfigs(DatasourceType.GATE, new RestListRequest());
    assertEquals(result.getBody().size(),1);
  }
  
  @Test
  public void test_getDashboardConfigs_filterGENERIC() throws ApsSystemException {
    SearcherDaoPaginatedResult dashboardConfigs = new SearcherDaoPaginatedResult();
    dashboardConfigs.setCount(2);
    DashboardConfigDto dashboardNoGATE = new DashboardConfigDto();
    dashboardConfigs.setList(new ArrayList(){{add(dashboard); add(dashboardNoGATE);}});
    when(dashboardConfigManager.getDashboardConfigs((List<FieldSearchFilter>) any())).thenReturn(dashboardConfigs);
    when(dtoBuilder.convert((List<DashboardConfig>) any())).thenReturn(new ArrayList(){{add(dashboard); add(dashboardNoGATE);}});
    PagedMetadata<DashboardConfigDto> result = dashboardConfigService
        .getDashboardConfigs(DatasourceType.GENERIC, new RestListRequest());
    assertEquals(result.getBody().size(),1);
  }
  
  
}

