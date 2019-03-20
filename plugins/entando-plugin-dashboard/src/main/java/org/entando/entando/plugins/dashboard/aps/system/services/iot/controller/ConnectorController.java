package org.entando.entando.plugins.dashboard.aps.system.services.iot.controller;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;

@Controller
@RestController("plugins/iotConnector")
public class ConnectorController {

  @Autowired
  IDashboardConfigService iDashboardConfigService;

  @Autowired
  IConnectorService iConnectorService;
  
  @RequestMapping(value = "measurements/{dashboardId}/{datasourceCode}", method = RequestMethod.POST)
  public ResponseEntity<?> saveMeasurement(@PathVariable int dashboardId,
      @PathVariable String datasourceCode, @RequestBody JsonArray jsonElements) throws Exception {

    DashboardConfigDto dashboardDto = iDashboardConfigService
        .getDashboardConfig(dashboardId);
    
	  IDashboardDatasourceDto dto = iConnectorService.getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto, datasourceCode, dashboardDto.getServerDescription());
    
    iConnectorService.saveDeviceMeasurement(dto, jsonElements);
    
//    return new ResponseEntity<>(measurement, HttpStatus.OK);
	  return null;
  }
}
