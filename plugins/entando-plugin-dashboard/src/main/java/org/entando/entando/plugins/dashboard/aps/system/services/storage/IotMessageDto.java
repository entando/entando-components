package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.Id;

public class IotMessageDto {

	@Id
	private String id;
	private Instant createdAt;
	private MessagePayload measurement;
	private int serverId;
	private String dashboardCode;

	public IotMessageDto() {

	}

	public IotMessageDto(int serverId, String dashboardCode, MessagePayload content) {
		this.serverId = serverId;
		this.dashboardCode = dashboardCode;
		this.measurement = content;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public MessagePayload getContent() {
		return measurement;
	}
	public void setContent(MessagePayload content) {
		this.measurement = content;
	}

	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}


	public String getDashboardCode() {
		return dashboardCode;
	}

	public void setDashboardCode(String dashboardCode) {
		this.dashboardCode = dashboardCode;
	}
	@Override
	public String toString() {
		return "Message [" + (id != null ? "id=" + id + ", " : "")
				+ (createdAt != null ? "createdAt=" + createdAt + ", " : "")
				+ (measurement != null ? "content=" + measurement + ", " : "")
				+ ("serverId=" + serverId)
				+ (dashboardCode != null ? "dashboardCode=" + dashboardCode : "") + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		IotMessageDto that = (IotMessageDto) o;
		return serverId == that.serverId &&
				Objects.equals(id, that.id) &&
				Objects.equals(createdAt, that.createdAt) &&
				Objects.equals(measurement, that.measurement) &&
				Objects.equals(dashboardCode, that.dashboardCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createdAt, measurement, serverId, dashboardCode);
	}
	
}
