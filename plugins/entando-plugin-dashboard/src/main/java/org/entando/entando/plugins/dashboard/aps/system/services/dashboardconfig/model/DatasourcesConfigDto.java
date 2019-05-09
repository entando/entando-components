
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

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DatasourcesConfigDto {

    private int fk_dashboard_config;
  
    private String name;
    
    private String datasourceCode;
    
    private String datasource;

    private String datasourceURI;

    private String status;
    
    private Map<String, Object> metadata = new HashMap();


    public Map<String, Object> getMetadata() {
		return metadata;
	}

	public void setMetadata(Map<String, Object> metadata) {
		this.metadata = metadata;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public void setStatus(DatasourceStatus status) {
      this.status = status.toString();
    }

  public int getFk_dashboard_config() {
    return fk_dashboard_config;
  }

  public void setFk_dashboard_config(int fk_dashboard_config) {
    this.fk_dashboard_config = fk_dashboard_config;
  }

  public String getDatasourceCode() {
        return datasourceCode;
    }

    public void setDatasourceCode(String datasourceCode) {
        this.datasourceCode = datasourceCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DatasourcesConfigDto that = (DatasourcesConfigDto) o;
        return Objects.equals(name, that.name) &&
            Objects.equals(datasourceCode, that.datasourceCode) &&
            Objects.equals(datasource, that.datasource) &&
            Objects.equals(datasourceURI, that.datasourceURI) &&
            Objects.equals(status, that.status) &&
            Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, datasourceCode, datasource, datasourceURI, status, metadata);
    }
}
