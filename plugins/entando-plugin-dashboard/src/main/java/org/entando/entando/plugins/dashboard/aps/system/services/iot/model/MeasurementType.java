package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

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
  public String toString() {
    return "MeasurementType{" +
        "name='" + name + '\'' +
        ", type='" + type + '\'' +
        '}';
  }
}
