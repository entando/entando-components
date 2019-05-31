package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;

public interface IGateConnectorIot {

  boolean isParcheggioAvailable(DashboardConfigDto dto, String datasourceCode);

  DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode);
  
}
