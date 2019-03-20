package org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;

public class KaaApplicationConfigDto extends DatasourcesConfigDto {

  private  int sequenceNumber; // da capire

  private String credentialsServiceName; // da capire

  private  String loggerId;

  public int getSequenceNumber() {
    return sequenceNumber;
  }

  public void setSequenceNumber(int sequenceNumber) {
    this.sequenceNumber = sequenceNumber;
  }

  public String getCredentialsServiceName() {
    return credentialsServiceName;
  }

  public void setCredentialsServiceName(String credentialsServiceName) {
    this.credentialsServiceName = credentialsServiceName;
  }

  public String getLoggerId() {
    return loggerId;
  }

  public void setLoggerId(String loggerId) {
    this.loggerId = loggerId;
  }
}
