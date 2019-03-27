package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class MeasurementObject {

  private String name;
  private String measure;
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

  public String getMeasure() {
    return measure;
  }

  public void setMeasure(String measure) {
    this.measure = measure;
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
