package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.MessagePayload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MeasurementPayload implements MessagePayload {

  private String templateId;
  private List<MeasurementObject> measurements = new ArrayList<>();

  public MeasurementPayload() {
  }

  public MeasurementPayload(MeasurementObject... args) {
    measurements.addAll(Arrays.asList(args));
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasurementPayload payload = (MeasurementPayload) o;
    return Objects.equals(templateId, payload.templateId) &&
        Objects.equals(measurements, payload.measurements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, measurements);
  }
}
