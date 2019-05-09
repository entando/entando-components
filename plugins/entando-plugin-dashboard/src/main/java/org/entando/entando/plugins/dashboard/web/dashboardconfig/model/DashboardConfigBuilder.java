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
    BeanUtils.copyProperties(dto, request);
    List<DatasourcesConfigRequest> dataReq = new ArrayList<>();
    for (DatasourcesConfigDto configDto: dto.getDatasources()) {
      dataReq.add(fromDtoToRequest(configDto));
    }
    request.setDatasources(dataReq);
    return request;
  }
  
  private static DatasourcesConfigRequest fromDtoToRequest(DatasourcesConfigDto dto) {
    DatasourcesConfigRequest request = new DatasourcesConfigRequest();
    BeanUtils.copyProperties(dto,request);
    return request;
  }

  public static DashboardConfigDto fromRequestToDto(DashboardConfigRequest req) {
    DashboardConfigDto dto = new DashboardConfigDto();
    BeanUtils.copyProperties(req, dto);
    List<DatasourcesConfigDto> dataDto = new ArrayList<>();
    for (DatasourcesConfigRequest configReq: req.getDatasources()) {
      dataDto.add(fromRequestToDto(configReq));
    }
    dto.setDatasources(dataDto);
    return dto;
  }

  private static DatasourcesConfigDto fromRequestToDto(DatasourcesConfigRequest req) {
    DatasourcesConfigDto dto = new DatasourcesConfigDto();
    BeanUtils.copyProperties(req,dto);
    return dto;
  }
  public static DashboardConfig fromRequestToEntity(DashboardConfigRequest req) {
    DashboardConfig entity = new DashboardConfig();
    BeanUtils.copyProperties(req, entity);
    List<DatasourcesConfigDto> dataDto = new ArrayList<>();
    for (DatasourcesConfigRequest configReq: req.getDatasources()) {
      dataDto.add(fromRequestToDto(configReq));
    }
    entity.setDatasources(dataDto);
    return entity;
  }

}
