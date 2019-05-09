package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

public enum DatasourceStatus {
  ONLINE{
    @Override
    public String toString() {
      return "online";
    }
  },
  OFFLINE{
    @Override
    public String toString() {
      return "offline";
    }
  },
}
