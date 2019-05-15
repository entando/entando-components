package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.DATASOURCE_STATUS_OFFLINE;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.DATASOURCE_STATUS_ONLINE;

public enum DatasourceStatus {
  ONLINE{
    @Override
    public String toString() {
      return DATASOURCE_STATUS_ONLINE;
    }
  },
  OFFLINE{
    @Override
    public String toString() {
      return DATASOURCE_STATUS_OFFLINE;
    }
  },
  ;
}
