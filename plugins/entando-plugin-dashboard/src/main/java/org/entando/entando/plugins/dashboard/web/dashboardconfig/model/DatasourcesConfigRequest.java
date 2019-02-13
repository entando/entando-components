package org.entando.entando.plugins.dashboard.web.dashboardconfig.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class DatasourcesConfigRequest {

    @Size(max = 30, message = "string.size.invalid")
    @NotBlank(message = "dashboardConfig.datasource.notBlank")
    private String datasource;

    @Size(max = 100, message = "string.size.invalid")
    @NotBlank(message = "dashboardConfig.datasourceURI.notBlank")
    private String datasourceURI;

    private String status;

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
}
