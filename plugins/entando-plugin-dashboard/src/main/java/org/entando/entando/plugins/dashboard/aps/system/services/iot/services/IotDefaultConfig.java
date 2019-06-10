package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

public class IotDefaultConfig implements Cloneable{

  private Long dashboardId;
  private String datasourceCode;

  public Long getDashboardId() {
    return dashboardId;
  }

  public void setDashboardId(Long dashboardId) {
    this.dashboardId = dashboardId;
  }

  public String getDatasourceCode() {
    return datasourceCode;
  }

  public void setDatasourceCode(String datasourceCode) {
    this.datasourceCode = datasourceCode;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    IotDefaultConfig config = new IotDefaultConfig();
    config.setDashboardId(this.getDashboardId());
    config.setDatasourceCode(this.getDatasourceCode());
    return config;
  }
}
