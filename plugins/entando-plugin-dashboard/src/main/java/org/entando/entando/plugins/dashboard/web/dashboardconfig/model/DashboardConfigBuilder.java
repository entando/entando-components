package org.entando.entando.plugins.dashboard.web.dashboardconfig.model;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class DashboardConfigBuilder {

  public static DashboardConfigRequest fromDtoToRequest(DashboardConfigDto dto) {
    DashboardConfigRequest request = new DashboardConfigRequest();
    List<DatasourcesConfigRequest> datasourcesRequest = new ArrayList<>();
    List<DatasourcesConfigDto> datasourcesConfigDto = dto.getDatasources();
    BeanUtils.copyProperties(dto, request);
    request.setDatasources(new ArrayList<>());
    for (DatasourcesConfigDto configDto: dto.getDatasources()) {
      request.getDatasources().add(fromDtoToRequest(configDto));
    }
    return request;
  }
  
  public static DatasourcesConfigRequest fromDtoToRequest(DatasourcesConfigDto dto) {
    DatasourcesConfigRequest request = new DatasourcesConfigRequest();
    BeanUtils.copyProperties(dto,request);
    return request;
  } 
  
}
