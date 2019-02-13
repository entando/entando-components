package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model;

public class DatasourcesConfigDto {

    private String datasource;

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
