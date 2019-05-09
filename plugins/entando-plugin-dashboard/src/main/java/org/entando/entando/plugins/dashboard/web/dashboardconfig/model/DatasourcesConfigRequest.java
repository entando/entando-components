
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

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.google.gson.JsonObject;

public class DatasourcesConfigRequest {

  @Size(max = 30, message = "string.size.invalid")
  @NotBlank(message = "dashboardConfig.datasource.notBlank")
  private String datasource;

  @Size(max = 100, message = "string.size.invalid")
  private String datasourceURI;

  @Size(max = 100, message = "string.size.invalid")
  @NotBlank(message = "dashboardConfig.datasourceCode.notBlank")
  private String datasourceCode;

  private String status;

  private Map<String, Object> metadata = new HashMap();
  
  public String getDatasource() {
    return datasource;
  }

  public void setDatasource(String datasource) {
    this.datasource = datasource;
  }

  public String getDatasourceURI() {
    return datasourceURI;
  }

  public void setDatasourceURI(String datasourceURI) {
    this.datasourceURI = datasourceURI;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDatasourceCode() {
    return datasourceCode;
  }

  public void setDatasourceCode(String datasourceCode) {
    this.datasourceCode = datasourceCode;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

}
