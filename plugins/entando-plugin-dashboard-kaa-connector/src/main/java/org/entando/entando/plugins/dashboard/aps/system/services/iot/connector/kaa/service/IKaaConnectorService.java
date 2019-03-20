package org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.service;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto.DashboardKaaDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.dto.LogAppender;

import java.util.List;

public interface IKaaConnectorService {

  List<LogAppender> getLogAppenders(DashboardKaaDatasourceDto dashboardDatasourceDto);

}
