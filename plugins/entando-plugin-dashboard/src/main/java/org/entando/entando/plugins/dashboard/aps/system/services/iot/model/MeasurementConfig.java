package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class MeasurementConfig {

  private String measurementConfigId;

  private int dashboardId;

  private String datasourceCode;

  private String measurementTemplateId;

  private List<MeasurementMapping> mappings;

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

  public String getMappingKey(String sourceName) {
    for (MeasurementMapping mapping : this.getMappings()) {
      if(mapping.sourceName.equals(sourceName)) {
        return mapping.getDetinationName();
      }
    } 
    return sourceName;
  }
  
}
