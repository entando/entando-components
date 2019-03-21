package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

public class MeasurementColumn {

  String key;
  String value;

  public MeasurementColumn() {

  }

  public MeasurementColumn(String key, String value) {
    this.setKey(key);
    this.setValue(value);
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "MeasurementType{" +
        "key='" + key + '\'' +
        ", value='" + value + '\'' +
        '}';
  }
}
