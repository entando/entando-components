package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Objects;

public class MeasurementObject {

  private String measure;
  @JsonIgnore
  private String eventDate;
  @JsonIgnore
  private String swReceivedDate;
  @JsonIgnore
  private String entandoReceivedDate;

  public MeasurementObject() {
  }

//  public MeasurementObject(String name, String measure) {
//    this.measure = measure;
//  }
//
//  public String getName() {
//    return name;
//  }
//
//  public MeasurementObject setName(String name) {
//    this.name = name;
//    return this;
//  }

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

//  @Override
//  public boolean equals(Object o) {
//    if (this == o) {
//      return true;
//    }
//    if (o == null || getClass() != o.getClass()) {
//      return false;
//    }
//    MeasurementObject that = (MeasurementObject) o;
//    return //Objects.equals(name, that.name) &&
//        Objects.equals(measure, that.measure) &&
//        Objects.equals(eventDate, that.eventDate) &&
//        Objects.equals(swReceivedDate, that.swReceivedDate) &&
//        Objects.equals(entandoReceivedDate, that.entandoReceivedDate);
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hash(name, measure, eventDate, swReceivedDate, entandoReceivedDate);
//  }
}
