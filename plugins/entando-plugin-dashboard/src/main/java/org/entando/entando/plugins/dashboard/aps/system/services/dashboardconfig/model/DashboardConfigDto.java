
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

package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model;


import java.util.ArrayList;
import java.util.List;

public class DashboardConfigDto {

  private int id;
//  private String serverDescription;
  private String serverURI;
  private String username;
  private String password;
  private String token;
  private int timeConnection;
  private ServerType type;
  private boolean active;
  private boolean debug;
  private List<DatasourcesConfigDto> datasources = new ArrayList<>();


  public ServerType getType() {
    return type;
  }

  public void setType(ServerType type) {
    this.type = type;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

//  public String getServerDescription() {
//    return serverDescription;
//  }
//
//  public void setServerDescription(String serverDescription) {
//    this.serverDescription = serverDescription;
//  }

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


  public static String getEntityFieldName(String dtoFieldName) {
    return dtoFieldName;
  }

  public List<DatasourcesConfigDto> getDatasources() {
    return datasources;
  }

  public void setDatasources(final List<DatasourcesConfigDto> datasources) {
    this.datasources = datasources;
  }
}
