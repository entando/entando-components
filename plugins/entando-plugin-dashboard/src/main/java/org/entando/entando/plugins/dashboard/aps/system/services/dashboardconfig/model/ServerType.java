package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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
	
}
