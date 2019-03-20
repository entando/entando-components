package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Measurement {

  private String id;
  private int dashboardId;
  private String datasourceCode;
  private String templateId;
  private List<MeasurementObject> measurements;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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
}
