package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.DATASOURCE_TYPE_GATE;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.DATASOURCE_TYPE_GENERIC;
import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.DATASOURCE_TYPE_GEODATA;

public enum DatasourceType {
  GENERIC{
    @Override
    public String toString() {
      return DATASOURCE_TYPE_GENERIC;
    }
  },
  GEODATA{
    @Override
    public String toString() {
      return DATASOURCE_TYPE_GEODATA;
    }
  },
  GATE{
    @Override
    public String toString() {
      return DATASOURCE_TYPE_GATE;
    }
  },
  ;
}
