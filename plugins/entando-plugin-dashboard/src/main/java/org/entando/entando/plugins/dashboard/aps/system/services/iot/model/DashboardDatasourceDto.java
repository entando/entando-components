package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;

public class DashboardDatasourceDto implements IDashboardDatasourceDto{

  private DashboardConfigDto dashboardConfigDto;
  
  private DatasourcesConfigDto datasourcesConfigDto;

  public DashboardConfigDto getDashboardConfigDto() {
    return dashboardConfigDto;
  }

  public void setDashboardConfigDto(
      DashboardConfigDto dashboardConfigDto) {
    this.dashboardConfigDto = dashboardConfigDto;
  }

  public String getDashboardUrl() {
    if(this.getDashboardConfigDto() != null ) {
      return this.getDashboardConfigDto().getServerURI();
    }
    return null;
  }

  @Override
  public DatasourcesConfigDto getDatasourcesConfigDto() {
    return datasourcesConfigDto;
  }

  public void setDatasourcesConfigDto(
      DatasourcesConfigDto datasourcesConfigDto) {
    this.datasourcesConfigDto = datasourcesConfigDto;
  }

  public String getDatasourceUrl() {
    if(this.getDatasourcesConfigDto() != null) {
      return this.getDatasourcesConfigDto().getDatasourceURI();
    }
    return null;
  }

  @Override
  public ServerType getServerType() {
    return dashboardConfigDto.getType();
  }

  @Override
  public int getDashboardId() {
    return dashboardConfigDto.getId();
  }

  @Override
  public String getDatasourceCode() {
    return datasourcesConfigDto.getDatasourceCode();
  }
  
  @Override
  public boolean supports(String serverType) {
    return this.getServerType().equals(serverType);
  }
  	
}
