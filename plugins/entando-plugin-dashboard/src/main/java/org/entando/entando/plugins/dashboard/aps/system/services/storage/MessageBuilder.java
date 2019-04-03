package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementPayload;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

public class MessageBuilder {

	public static IotMessageDto getDto(IotMessage message) {
		IotMessageDto dto = new IotMessageDto();
		dto.setCreatedAt(message.getCreatedAt());
		dto.setDashboardCode(message.getDashboardCode());
		dto.setServerId(message.getServerId());
		dto.setContent(new Gson().fromJson(message.getContent().toJson(), MeasurementPayload.class));
		return dto;
	}
	
	public static List<IotMessageDto> getDtos(List<IotMessage> messages) {
		List<IotMessageDto> dtos = new ArrayList<>();
		messages.forEach(m -> dtos.add(getDto(m)));
		return dtos;
	}
	

	public static IotMessage getEntity(IotMessageDto message) {
		IotMessage entity = new IotMessage();
		entity.setCreatedAt(message.getCreatedAt());
		entity.setDashboardCode(message.getDashboardCode());
		entity.setServerId(message.getServerId());
		entity.setContent(BasicDBObject.parse(message.getContent().getJson()));
		return entity;
	}
	
	public static List<IotMessage> getEntities(List<IotMessageDto> dtos) {
		List<IotMessage> entities = new ArrayList<>();
		dtos.forEach(m -> entities.add(getEntity(m)));
		return entities;
	}
}
