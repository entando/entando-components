package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.google.gson.JsonObject;

public class MeasurementObject {

  private String name;
  private JsonObject measure;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public JsonObject getMeasure() {
    return measure;
  }

  public void setMeasure(JsonObject measure) {
    this.measure = measure;
  }
}
