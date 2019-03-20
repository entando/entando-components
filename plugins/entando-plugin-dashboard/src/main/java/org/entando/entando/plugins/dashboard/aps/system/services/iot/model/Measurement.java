package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.google.gson.JsonArray;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Measurement {

  Map<String, JsonArray> measurement;

  public Map<String, JsonArray> getMeasurement() {
    if(measurement == null) {
      measurement = new HashMap<>();
    }
    return measurement;
  }

  public void setMeasurement(
      Map<String, JsonArray> measurement) {
    if(measurement == null) {
      measurement = new HashMap<>();
    }
    this.measurement = measurement;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Measurement that = (Measurement) o;
    return Objects.equals(measurement, that.measurement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(measurement);
  }
}
