package org.entando.entando.plugins.dashboard.web.iot.mock;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.DashboardConfigManager;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementObject;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementPayload;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.storage.IotMessage;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.agiletec.aps.system.exception.ApsSystemException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "/plugins/dashboard")
@Profile("mock")
public class ConnectorController {

	private static final Logger logger = LoggerFactory.getLogger(ConnectorController.class);

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
	public ResponseEntity<?> saveMeasurement(@PathVariable int serverId, @PathVariable String datasourceCode,
			@RequestBody String measure) {

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "/server/{serverId}/datasource/{datasourceCode}/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PagedRestResponse<IotMessage>> getMeasurement(@PathVariable int serverId,
			@PathVariable String datasourceCode, @RequestParam(value = "startDate", required = false) Instant startDate,
			@RequestParam(value = "endDate", required = false) Instant endDate, RestListRequest requestList)
			throws Exception {
		try {
			IotMessage iotMessage = new IotMessage();
			MeasurementPayload m = new MeasurementPayload();
			JsonObject obj = new JsonObject();

			String jsonString = "{\"timestamp\":\"2019-03-22T15:03:26\",\"temperature\":\"20\"}";

			JsonElement c = new com.google.gson.JsonParser().parse(jsonString);
			obj.add("measure", c);

			List<JsonObject> misur = new ArrayList<JsonObject>();
//			misur.add(obj);
//			m.setMeasurements(misur);
//			iotMessage.setContent(m);
//
//			List<IotMessage> lista = new ArrayList<IotMessage>();
//			lista.add(iotMessage);
//
//			PagedMetadata<IotMessage> pagedMetadata = new PagedMetadata<>(requestList, 1);
//			pagedMetadata.setBody(lista);
//
//			return new ResponseEntity(new PagedRestResponse(pagedMetadata), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity(HttpStatus.BAD_GATEWAY);
	}

	@RequestMapping(value = "/setTemplate/server/{serverId}/datasource/{datasourceCode}", method = RequestMethod.POST)
	public ResponseEntity<?> setMeasurementTemplate(@PathVariable int serverId, @PathVariable String datasourceCode)
			throws ApsSystemException {

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
