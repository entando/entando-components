package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementPayload;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import com.google.gson.Gson;

public class TestMessagePayload {

	@Test
	public void testWriteFields() {
		MeasurementPayload measurementPayload = new MeasurementPayload();
		Map<String, Object> measurement = new HashMap<>();
		measurement.put("temperature", "2");
		measurement.put("timestamp", "123456789");
		measurementPayload.setMeasurement(measurement);

		//		PagedMetadata<MeasurementPayload> pagedMetadata = new PagedMetadata(restListRequest,
		//				pagedMeasurements);

		List<MeasurementPayload> payloads = new ArrayList<>();
		payloads.add(measurementPayload);
		payloads.add(measurementPayload);

		SearcherDaoPaginatedResult<MeasurementPayload> pagedMeasurements = new SearcherDaoPaginatedResult(
				payloads);
		PagedMetadata<MeasurementPayload> pagedMetadata = new PagedMetadata(new RestListRequest(),pagedMeasurements);
		pagedMetadata.setBody(payloads);

		PagedRestResponse<MeasurementPayload> pagedRestResponse = new PagedRestResponse<>(pagedMetadata);

		System.out.println(new Gson().toJson(new ResponseEntity(new PagedRestResponse(pagedMetadata), HttpStatus.OK)));
	}

}
