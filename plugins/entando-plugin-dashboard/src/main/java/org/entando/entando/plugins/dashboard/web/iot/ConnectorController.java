package org.entando.entando.plugins.dashboard.web.iot;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementPayload;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTUtils;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.agiletec.aps.system.exception.ApsSystemException;

@RestController
@RequestMapping(value = "/plugins/dashboard")
@Profile("!mock")
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

  @RequestMapping(value = "/server/{serverId}/datasource/{datasourceCode}", method = RequestMethod.POST)
  public ResponseEntity<?> saveMeasurement(@PathVariable int serverId,
      @PathVariable String datasourceCode,
      @RequestBody String measure) {

    DashboardConfigDto dto = IoTUtils.checkServerAndDatasource(serverId, datasourceCode, dashboardConfigService);
    connectorService.saveDeviceMeasurement(dto, datasourceCode, measure);
    return new ResponseEntity(HttpStatus.OK);
  }
  
	@RequestMapping(value = "/server/{serverId}/datasource/{datasourceCode}/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedRestResponse<MeasurementPayload>> getMeasurement(
			@PathVariable int serverId,
			@PathVariable String datasourceCode,
			@RequestParam(value = "startDate", required = false) Instant startDate,
			@RequestParam(value = "endDate", required = false) Instant endDate,
			RestListRequest requestList) {
    DashboardConfigDto dto = IoTUtils.checkServerAndDatasource(serverId, datasourceCode, dashboardConfigService);
		Date start = null;
		Date end = null;
		if(startDate != null) {
			start = Date.from(startDate);
		}
		if(endDate != null) {
			end = Date.from(endDate);
		}

		try {
			List<Map<String, Object>> payloads = this.connectorService
					.getDeviceMeasurements(dto, datasourceCode, start, end, requestList);

			SearcherDaoPaginatedResult<Map<String, Object>> pagedMeasurements = new SearcherDaoPaginatedResult(
					payloads);
			PagedMetadata<Map<String, Object>> pagedMetadata = new PagedMetadata(requestList,pagedMeasurements);
			pagedMetadata.setBody(payloads);

			return new ResponseEntity(new PagedRestResponse(pagedMetadata), HttpStatus.OK);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

  @RequestMapping(value = "/setTemplate/server/{serverId}/datasource/{datasourceCode}", method = RequestMethod.POST)
  public ResponseEntity<?> setMeasurementTemplate(@PathVariable int serverId,
      @PathVariable String datasourceCode)
      throws ApsSystemException {
    DashboardConfigDto dto = IoTUtils.checkServerAndDatasource(serverId, datasourceCode, dashboardConfigService);
//        connectorService.setDeviceMeasurementSchema(dto);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  
}
