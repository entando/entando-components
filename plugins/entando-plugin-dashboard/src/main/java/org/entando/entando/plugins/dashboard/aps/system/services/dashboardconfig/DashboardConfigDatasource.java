/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;


public class DashboardConfigDatasource {

    private int id;
    private int fkDashboardConfig;
    private String datasource;
    private String datasourceURI;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFkDashboardConfig() {
        return fkDashboardConfig;
    }

    public void setFkDashboardConfig(int fkDashboardConfig) {
        this.fkDashboardConfig = fkDashboardConfig;
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

    private String status;


}
