package org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;

public class SitewhereApplicationConfigDto extends DatasourcesConfigDto {

  private String assignmentId;

  private String specificationId;

  public String getAssignmentId() {
    return assignmentId;
  }

  public void setAssignmentId(String assignmentId) {
    this.assignmentId = assignmentId;
  }

  public String getSpecificationId() {
    return specificationId;
  }

  public void setSpecificationId(String specificationId) {
    this.specificationId = specificationId;
  }
}
