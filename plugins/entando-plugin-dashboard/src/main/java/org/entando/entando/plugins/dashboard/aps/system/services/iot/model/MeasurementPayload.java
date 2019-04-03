package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import java.util.HashMap;
import java.util.Map;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.MessagePayload;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;

public class MeasurementPayload implements MessagePayload {

	private Map<String, Object> measurement = new HashMap();

	public MeasurementPayload() {
	}

	public MeasurementPayload(Map<String, Object>  measurement) {
		this.measurement = measurement;
	}

	public Map<String, Object> getMeasurement() {
		return measurement;
	}

	public void setMeasurement(Map<String, Object> measurement) {
		this.measurement = measurement;
	}

	@JsonIgnore
	@Override
	public String getJson() {
		return new Gson().toJson(this);
	}
}
