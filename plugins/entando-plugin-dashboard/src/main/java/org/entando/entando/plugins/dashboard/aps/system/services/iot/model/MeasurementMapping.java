package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class MeasurementMapping {
  
  String sourceName;
  @JsonIgnore
  String sourceType;
  String detinationName;
  @JsonIgnore
  String transformerClass;

  public String getSourceName() {
    return sourceName;
  }

  public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
  }

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public String getDetinationName() {
    return detinationName;
  }

  public void setDetinationName(String detinationName) {
    this.detinationName = detinationName;
  }

  public String getTransformerClass() {
    return transformerClass;
  }

  public void setTransformerClass(String transformerClass) {
    this.transformerClass = transformerClass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasurementMapping that = (MeasurementMapping) o;
    return Objects.equals(sourceName, that.sourceName) &&
        Objects.equals(sourceType, that.sourceType) &&
        Objects.equals(detinationName, that.detinationName) &&
        Objects.equals(transformerClass, that.transformerClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sourceName, sourceType, detinationName, transformerClass);
  }
}
