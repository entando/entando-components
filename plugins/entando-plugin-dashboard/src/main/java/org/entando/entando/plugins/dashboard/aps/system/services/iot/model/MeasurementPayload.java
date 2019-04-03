package org.entando.entando.plugins.dashboard.aps.system.services.iot.model;

import java.util.Objects;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.MessagePayload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MeasurementPayload implements MessagePayload {

	private String templateId;
	private JsonObject measurement = new JsonObject();

	public MeasurementPayload() {
	}

	public MeasurementPayload(JsonObject measurement) {
		this.measurement = measurement;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public JsonObject getMeasurements() {
		return measurement;
	}

	public void setMeasurement(JsonObject measurement) {
		this.measurement = measurement;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MeasurementPayload payload = (MeasurementPayload) o;
		return Objects.equals(templateId, payload.templateId) &&
				Objects.equals(measurement, payload.measurement);
	}

	@Override
	public int hashCode() {
		return Objects.hash(templateId, measurement);
	}

	@Override
	public String getJson() {
		return new Gson().toJson(this);
	}
}
