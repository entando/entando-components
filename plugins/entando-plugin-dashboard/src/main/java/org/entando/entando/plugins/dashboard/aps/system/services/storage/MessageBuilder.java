package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementPayload;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBObject;

public class MessageBuilder {

	public static IotMessageDto getDto(IotMessage message) {
		Gson gson = new Gson();
		IotMessageDto dto = new IotMessageDto();
		dto.setCreatedAt(message.getCreatedAt());
		dto.setDashboardCode(message.getDashboardCode());
		dto.setServerId(message.getServerId());
		MeasurementPayload payload = gson.fromJson(message.getContent().toJson(), MeasurementPayload.class);
		//HashMap<String, Object> yourHashMap = new Gson().fromJson(yourJsonObject.toString(), HashMap.class);
		HashMap<String, Object> map = gson.fromJson(gson.toJson(payload.getMeasurement()), HashMap.class);
		payload.setMeasurement(map);
		dto.setContent(payload);
		return dto;
	}
	
	public static List<IotMessageDto> getDtos(List<IotMessage> messages) {
		List<IotMessageDto> dtos = new ArrayList<>();
		messages.forEach(m -> dtos.add(getDto(m)));
		return dtos;
	}
	

	public static IotMessage getEntity(IotMessageDto message) {
		Gson gson = new Gson();
		IotMessage entity = new IotMessage();
		entity.setCreatedAt(message.getCreatedAt());
		entity.setDashboardCode(message.getDashboardCode());
		entity.setServerId(message.getServerId());
		JsonObject json = gson.toJsonTree(message.getContent()).getAsJsonObject();
		entity.setContent(BasicDBObject.parse(gson.toJson(json)));
		return entity;
	}
	
	public static List<IotMessage> getEntities(List<IotMessageDto> dtos) {
		List<IotMessage> entities = new ArrayList<>();
		dtos.forEach(m -> entities.add(getEntity(m)));
		return entities;
	}
}
