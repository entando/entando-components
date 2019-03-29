package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import java.util.Objects;

public class MeasurementType {

  String name;
  String type;

  public MeasurementType() {
    
  }
  
  public MeasurementType(String name, String type) {
    this.setName(name);
    this.setType(type);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasurementType that = (MeasurementType) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, type);
  }

  @Override
  public String toString() {
    return "MeasurementType{" +
        "name='" + name + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
