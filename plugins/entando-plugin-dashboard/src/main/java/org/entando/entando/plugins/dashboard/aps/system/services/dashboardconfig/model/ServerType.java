package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ServerType {
	
	@JsonProperty("code")
	private String code;
	

	@JsonProperty("description")
	private String description;

	public ServerType() {
	}

  public ServerType(String code) {
		this.code = code;
	}

  public ServerType(String code, String description) {
    this.code = code;
    this.description = description;
  }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServerType that = (ServerType) o;
    return Objects.equals(code, that.code) &&
        Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, description);
  }
}
