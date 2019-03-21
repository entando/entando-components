package org.entando.entando.plugins.dashboard.web.iot;

import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "/plugins/dashboard/measurements")
public class ConnectorController {

    @Autowired
    IDashboardConfigService dashboardConfigService;

    @Autowired
    IConnectorService connectorService;

    protected IConnectorService getConnectorService() {
        return connectorService;
    }

    public void setConnectorService(IConnectorService connectorService) {
        this.connectorService = connectorService;
    }

    @RequestMapping(value = "/{dashboardId}/{datasourceId}", method = RequestMethod.POST)
    public ResponseEntity<?> saveMeasurement(@PathVariable int dashboardId, @PathVariable String datasourceCode,
                                             @RequestBody JsonArray jsonElements) throws Exception {

        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);

        IDashboardDatasourceDto dto = connectorService.getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto,
                datasourceCode, dashboardDto.getServerDescription());

        connectorService.saveDeviceMeasurement(dto, jsonElements);

//    return new ResponseEntity<>(measurement, HttpStatus.OK);
        return null;
    }

    @RequestMapping(value = "/{serverId}/datasource/{datasourceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<Map<String, Object>>> getMeasurement(@PathVariable String serverId,
                                                                                 @PathVariable String datasourceId, RestListRequest requestList) throws Exception {

        ResponseEntity<PagedRestResponse<Map<String, Object>>> listaDati = null;
        try {

            PagedMetadata<Map<String, Object>> pagedMetadata = this.connectorService.getMeasurements(requestList);
            listaDati = new ResponseEntity<>(new PagedRestResponse<>(pagedMetadata), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDati;
    }

    @RequestMapping(value = "/setTemplate/{dashboardId}/{datasourceCode}", method = RequestMethod.POST)
    public ResponseEntity<?> setMeasurementTemplate(@PathVariable int dashboardId, @PathVariable String datasourceCode)
            throws ApsSystemException {
        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);

        IDashboardDatasourceDto dto = connectorService.getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto,
                datasourceCode, dashboardDto.getServerDescription());

        connectorService.setDeviceMeasurementSchema(dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
