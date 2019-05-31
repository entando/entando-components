package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.google.gson.JsonObject;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.DeviceLocations;

public interface IGeodataConnectorIot {
  DeviceLocations getDeviceLocations(DashboardConfigDto dto, String datasourceCode);

  void setGeodataDeviceStatuses(DashboardConfigDto dto, String datasourceCode, JsonObject body);
}
