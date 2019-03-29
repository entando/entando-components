package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;


import com.google.gson.Gson;

import org.entando.entando.aps.system.init.model.servdb.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta una tipologia di dato proveniente da un datasource, erogabile sul portale
 *
 * inteso per essere un oggetto al di sopra dell'implementazione del singolo server
 */
public class MeasurementTemplate {

  private String id;

  private List<MeasurementType> fields = new ArrayList<>();

  public MeasurementTemplate() {

  }

  public void addField(String name, String type) {
    //se esiste gia "name", il vecchio dovrà essere sostituito
    if (fields.stream().anyMatch(measType -> measType.getName().equals(name))) {
      fields.remove(
          fields.stream().filter(measType -> measType.getName().equals(name)).findFirst().get());
    }
    MeasurementType measurementType = new MeasurementType(name, type);
    this.fields.add(measurementType);
  }

  public void removeField(String name) {
    if (fields.stream().anyMatch(measType -> measType.getName().equals(name))) {
      fields.remove(
          fields.stream().filter(measType -> measType.getName().equals(name)).findFirst().get());
    }
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<MeasurementType> getFields() {
    return fields;
  }

  public void setFields(
      List<MeasurementType> fields) {
    this.fields = fields;
  }
  
  public void setFields(String fields) {
    this.fields = Arrays.asList(new Gson().fromJson(fields,MeasurementType[].class));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MeasurementTemplate that = (MeasurementTemplate) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(fields, that.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fields);
  }
}
