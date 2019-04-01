package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import java.time.Instant;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "iot_messages")
public class IotMessage {

	@Id
	private String id;
	private Instant createdAt;
	private MessagePayload content;
	private int serverId;
	private String dashboardCode;

	public IotMessage() {

	}

	public IotMessage(int serverId, String dashboardCode, MessagePayload content) {
		this.serverId = serverId;
		this.dashboardCode = dashboardCode;
		this.content = content;
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
		return content;
	}
	public void setContent(MessagePayload content) {
		this.content = content;
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
				+ (content != null ? "content=" + content + ", " : "")
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
		IotMessage that = (IotMessage) o;
		return serverId == that.serverId &&
				Objects.equals(id, that.id) &&
				Objects.equals(createdAt, that.createdAt) &&
				Objects.equals(content, that.content) &&
				Objects.equals(dashboardCode, that.dashboardCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createdAt, content, serverId, dashboardCode);
	}
}
