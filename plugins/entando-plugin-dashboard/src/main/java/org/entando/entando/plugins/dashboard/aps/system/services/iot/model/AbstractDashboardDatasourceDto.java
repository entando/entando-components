package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;

public abstract class AbstractDashboardDatasourceDto implements IDashboardDatasourceDto{

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

  public String getDatasourceUrl() {
    if(this.getDatasourcesConfigDto() != null) {
      return this.getDatasourcesConfigDto().getDatasourceURI();
    }
    return null;
  }
  
  public void setDashboardUrl(String dashboardUrl){
    this.getDashboardConfigDto().setServerURI(dashboardUrl);
  }
  
  public void setDatasourceUrl(String datasourceUrl){
    this.getDatasourcesConfigDto().setDatasourceURI(datasourceUrl);
  }

  public DatasourcesConfigDto getDatasourcesConfigDto() {
    return datasourcesConfigDto;
  }

  public void setDatasourcesConfigDto(
      DatasourcesConfigDto datasourcesConfigDto) {
    this.datasourcesConfigDto = datasourcesConfigDto;
  }
}
