package org.entando.entando.plugins.dashboard.aps.system.services.iot.dto;

public class DeviceLocations {

  private Double latitude;
  private Double longitude;

  public DeviceLocations() {
  }

  public DeviceLocations(Double latitude, Double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
  
}
