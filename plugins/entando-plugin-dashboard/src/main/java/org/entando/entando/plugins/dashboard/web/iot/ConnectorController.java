package org.entando.entando.plugins.dashboard.web.iot;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "/plugins/dashboard")
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

    @RequestMapping(value = "/{dashboardId}/{datasourceCode}", method = RequestMethod.POST)
    public ResponseEntity<?> saveMeasurement(@PathVariable int dashboardId, @PathVariable String datasourceCode,
        @RequestBody JsonArray jsonElements) throws Exception {

        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);

        IDashboardDatasourceDto dto = connectorService.getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto,
            datasourceCode, dashboardDto.getServerDescription());

        connectorService.saveDeviceMeasurement(dto, jsonElements);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/server/{dashboardId}/datasource/{datasourceCode}/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<List<MeasurementObject>>> getMeasurement(@PathVariable int dashboardId,
        @PathVariable String datasourceCode, @RequestParam Long nMeasurements, @RequestParam Date startDate, @RequestParam Date endDate, RestListRequest requestList) throws Exception {

        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);
        IDashboardDatasourceDto dto = connectorService.getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto,
            datasourceCode, dashboardDto.getServerDescription());
        
        ResponseEntity<PagedRestResponse<List<MeasurementObject>>> measurements = null;
        try {

            PagedMetadata<List<MeasurementObject>> pagedMetadata = this.connectorService.getDeviceMeasurements(dto,nMeasurements, startDate,endDate, requestList);
            measurements = new ResponseEntity(new PagedRestResponse<>(pagedMetadata), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return measurements;
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
    
    @RequestMapping(value = "/{dashboardId}/{datasourceCode}/config")
    public ResponseEntity<?> getMeasurementConfig(@PathVariable int dashboardId, @PathVariable String datasourceCode) {
        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);

        IDashboardDatasourceDto dto = connectorService
            .getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto, datasourceCode,
                dashboardDto.getServerDescription());
        
        MeasurementConfig config = connectorService.getMeasurementsConfig(dto);
        
        return new ResponseEntity<>(config,HttpStatus.OK);
    }
    
}
