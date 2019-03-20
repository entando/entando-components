package org.entando.entando.plugins.dashboard.aps.system.services.iot.controller;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
@RestController("plugins/iotConnector")
public class ConnectorController {

  @Autowired
  IDashboardConfigService iDashboardConfigService;

  @Autowired
  IConnectorService iConnectorService;
  
  @RequestMapping(value = "measurements/{dashboardId}/{datasourceCode}")
  public ResponseEntity<?> saveMeasurement(@PathVariable int dashboardId,
      @PathVariable String datasourceCode, @RequestBody JsonArray jsonElements) throws Exception {
	  
	  //TODO marco
    
//	  IDashboardDatasourceDto dto = iDashboardConfigService.getDashboardDataSourceDtobyIdAndCode(dashboardId, datasourceCode);
    
//    JsonObject measurement = iConnectorService.saveDeviceMeasurement(dto, jsonElements);
    
//    return new ResponseEntity<>(measurement, HttpStatus.OK);
	  return null;
  }
}
