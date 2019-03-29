package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.MessagePayload;

import java.util.ArrayList;
import java.util.List;

public class MeasurementPayload implements MessagePayload {

  private String templateId;
  private List<MeasurementObject> measurements = new ArrayList<>();

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public List<MeasurementObject> getMeasurements() {
    return measurements;
  }

  public void setMeasurements(
      List<MeasurementObject> measurements) {
    this.measurements = measurements;
  }
}
