package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MeasurementConfig {

  private String measurementConfigId;

  private int dashboardId;

  private String datasourceCode;

  private String measurementTemplateId;

  private List<MeasurementMapping> mappings = new ArrayList<>();

  public String getMeasurementConfigId() {
    return measurementConfigId;
  }

  public void setMeasurementConfigId(String measurementConfigId) {
    this.measurementConfigId = measurementConfigId;
  }

  public int getDashboardId() {
    return dashboardId;
  }

  public void setDashboardId(int dashboardId) {
    this.dashboardId = dashboardId;
  }

  public String getDatasourceCode() {
    return datasourceCode;
  }

  public void setDatasourceCode(String datasourceCode) {
    this.datasourceCode = datasourceCode;
  }

  public String getMeasurementTemplateId() {
    return measurementTemplateId;
  }

  public void setMeasurementTemplateId(String measurementTemplateId) {
    this.measurementTemplateId = measurementTemplateId;
  }

  public List<MeasurementMapping> getMappings() {
    return mappings;
  }

  public void setMappings(
      List<MeasurementMapping> mappings) {
    this.mappings = mappings;
  }

  public void setMappings(String json) {
    this.mappings = Arrays.asList(new Gson().fromJson(json, MeasurementMapping[].class));
  }

  public void addMapping(MeasurementMapping mapping) {
    this.getMappings().add(mapping);
  }
  
  
  public String getMappingKey(String sourceName) {
    for (MeasurementMapping mapping : this.getMappings()) {
      if(mapping.sourceName.equals(sourceName)) {
        return mapping.getDestinationName();
      }
    }
    return sourceName;
  }

  public Optional<MeasurementMapping> getMappingforSourceName(String sourceName) {
    return this.getMappings().stream().filter(mapping -> mapping.getSourceName().equals(sourceName))
        .findFirst();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasurementConfig config = (MeasurementConfig) o;
    return dashboardId == config.dashboardId &&
        Objects.equals(measurementConfigId, config.measurementConfigId) &&
        Objects.equals(datasourceCode, config.datasourceCode) &&
        Objects.equals(measurementTemplateId, config.measurementTemplateId) &&
        Objects.equals(mappings, config.mappings);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(measurementConfigId, dashboardId, datasourceCode, measurementTemplateId, mappings);
  }
}
