package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class MeasurementObject {

  private String name;
  private JsonElement measure;
  private String eventDate;
  private String swReceivedDate;
  private String entandoReceivedDate;
  
  public String getName() {
    return name;
  }

  public MeasurementObject setName(String name) {
    this.name = name;
    return this;
  }

  public JsonElement getMeasure() {
    return measure;
  }

  public MeasurementObject setMeasure(JsonElement measure) {
    this.measure = measure;
    return this;
  }

  public MeasurementObject setMeasure(String measure) {
    this.measure = new Gson().fromJson(measure, JsonElement.class);
    return this;
  }
  
  public String getEventDate() {
    return eventDate;
  }

  public void setEventDate(String eventDate) {
    this.eventDate = eventDate;
  }

  public String getSwReceivedDate() {
    return swReceivedDate;
  }

  public void setSwReceivedDate(String swReceivedDate) {
    this.swReceivedDate = swReceivedDate;
  }

  public String getEntandoReceivedDate() {
    return entandoReceivedDate;
  }

  public void setEntandoReceivedDate(String entandoReceivedDate) {
    this.entandoReceivedDate = entandoReceivedDate;
  }
}
