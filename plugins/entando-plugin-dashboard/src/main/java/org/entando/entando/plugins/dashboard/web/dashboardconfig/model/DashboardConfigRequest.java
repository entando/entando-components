
/*
 *
 *  * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *  *
 *  * This library is free software; you can redistribute it and/or modify it under
 *  * the terms of the GNU Lesser General Public License as published by the Free
 *  * Software Foundation; either version 2.1 of the License, or (at your option)
 *  * any later version.
 *  *
 *  * This library is distributed in the hope that it will be useful, but WITHOUT
 *  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 *  * details.
 *
 */

package org.entando.entando.plugins.dashboard.web.dashboardconfig.model;


import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class DashboardConfigRequest {

  @NotNull(message = "dashboardConfig.id.notBlank")
  private int id;

  @Size(max = 30, message = "string.size.invalid")
  @NotBlank(message = "dashboardConfig.serverDescription.notBlank")
  private String serverDescription;

  @Size(max = 255, message = "string.size.invalid")
  @NotBlank(message = "dashboardConfig.serverURI.notBlank")
  private String serverURI;

  private String username;

  private String password;

  private String token;

  private int timeConnection;

  private boolean active;

  private boolean debug;

  private String type;

  private List<DatasourcesConfigRequest> datasources;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isActive() {
    return active;
  }

  public boolean isDebug() {
    return debug;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getServerDescription() {
    return serverDescription;
  }

  public void setServerDescription(String serverDescription) {
    this.serverDescription = serverDescription;
  }

  public String getServerURI() {
    return serverURI;
  }

  public void setServerURI(String serverURI) {
    this.serverURI = serverURI;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public int getTimeConnection() {
    return timeConnection;
  }

  public void setTimeConnection(int timeConnection) {
    this.timeConnection = timeConnection;
  }

  public boolean getActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean getDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public List<DatasourcesConfigRequest> getDatasources() {
    return datasources;
  }

  public void setDatasources(final List<DatasourcesConfigRequest> datasources) {
    this.datasources = datasources;
  }
}
