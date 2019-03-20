package org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.service;

import com.google.gson.JsonArray;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.sitewhere.dto.DashboardSitewhereDatasourceDto;

import java.time.Instant;

public interface ISitewhereConnectorService {

  JsonArray getMeasurements(DashboardSitewhereDatasourceDto dashboardSitewhereDatasourceDto,
      Long nMeasurements, Instant startDate, Instant endDate);
}
