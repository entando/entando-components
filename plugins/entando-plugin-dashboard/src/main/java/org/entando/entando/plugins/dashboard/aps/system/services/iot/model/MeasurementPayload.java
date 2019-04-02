package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.MessagePayload;

import com.google.gson.JsonObject;

public class MeasurementPayload implements MessagePayload {

  private String templateId;
  private List<JsonObject> measurements = new ArrayList<>();

  public MeasurementPayload() {
  }

  public MeasurementPayload(JsonObject... args) {
    measurements.addAll(Arrays.asList(args));
  }

  public MeasurementPayload(List<JsonObject> measurements) {
	this.measurements = measurements;
}

public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public List<JsonObject> getMeasurements() {
    return measurements;
  }

  public void setMeasurements(
      List<JsonObject> measurements) {
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
