/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model;


import java.util.ArrayList;
import java.util.List;

public class DashboardConfigDto {

    private int id;
    private String serverDescription;
    private String serverURI;
    private String username;
    private String password;
    private String token;
    private int timeConnection;
    private int active;
    private int debug;

    private List<DatasourcesConfigDto> datasources = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDebug() {
        return debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }


    public static String getEntityFieldName(String dtoFieldName) {
        return dtoFieldName;
    }

    public List<DatasourcesConfigDto> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<DatasourcesConfigDto> datasources) {
        this.datasources = datasources;
    }
}
