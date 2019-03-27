package org.entando.entando.plugins.dashboard.web.iot;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
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

//        IDashboardDatasourceDto dto = connectorService.getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto,
//            datasourceCode, dashboardDto.getType());
//FIXME
        connectorService.saveDeviceMeasurement(null, jsonElements);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/server/{dashboardId}/datasource/{datasourceCode}/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<MeasurementObject>> getMeasurement(@PathVariable int dashboardId,
        @PathVariable String datasourceCode, @RequestParam (value = "nMeasurements", required = false) long nMeasurements, 
        @RequestParam (value = "startDate", required = false)Date startDate , 
        @RequestParam (value = "endDate", required = false)Date endDate, 
        RestListRequest requestList) throws Exception {

        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);
        DashboardDatasourceDto dto = new DashboardDatasourceDto();
        dto.setDashboardConfigDto(dashboardDto);
        for (DatasourcesConfigDto datasource : dashboardDto.getDatasources()) {
            if(datasource.getDatasourceCode().equals(datasourceCode)) {
                dto.setDatasourcesConfigDto(datasource);
                break;
            }
        }
        
        try {
            PagedMetadata<MeasurementObject> pagedMetadata = this.connectorService
                .getDeviceMeasurements(dto, nMeasurements, startDate, endDate, requestList);
            return new ResponseEntity(new PagedRestResponse(pagedMetadata), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/setTemplate/{dashboardId}/{datasourceCode}", method = RequestMethod.POST)
    public ResponseEntity<?> setMeasurementTemplate(@PathVariable int dashboardId, @PathVariable String datasourceCode)
        throws ApsSystemException {
        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);

//        IDashboardDatasourceDto dto = connectorService.getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto,
//            datasourceCode, dashboardDto.getType());
//FIXME
//        connectorService.setDeviceMeasurementSchema(dto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/getTemplate/{dashboardId}/{datasourceCode}", method = RequestMethod.POST)
    public ResponseEntity<SimpleRestResponse<MeasurementTemplate>> getMeasurementTemplate(@PathVariable int dashboardId, @PathVariable String datasourceCode)
        throws ApsSystemException {

        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);
        DashboardDatasourceDto dto = new DashboardDatasourceDto();
        dto.setDashboardConfigDto(dashboardDto);
        for (DatasourcesConfigDto datasource : dashboardDto.getDatasources()) {
            if(datasource.getDatasourceCode().equals(datasourceCode)) {
                dto.setDatasourcesConfigDto(datasource);
                break;
            }
        }
        MeasurementTemplate template = connectorService
            .getDeviceMeasurementSchema(dto);

        return new ResponseEntity<>(new SimpleRestResponse(template), HttpStatus.OK);
    }


    @RequestMapping(value = "/{dashboardId}/{datasourceCode}/config")
    public ResponseEntity<?> getMeasurementConfig(@PathVariable int dashboardId, @PathVariable String datasourceCode) {
        DashboardConfigDto dashboardDto = dashboardConfigService.getDashboardConfig(dashboardId);

//        IDashboardDatasourceDto dto = connectorService
//            .getDashboardDatasourceDtobyIdAndCodeAndServerType(dashboardDto, datasourceCode,
//                dashboardDto.getType());
        //FIXME
        
//        MeasurementConfig config = connectorService.getMeasurementsConfig(dto);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
}
