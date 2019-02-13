/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.web.dashboardconfig.model;


import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

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

    private int active;

    private int debug;

    //@Not(message = "dashboardConfig.datasources.notBlank")
    private List<DatasourcesConfigRequest> datasources;

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

    public List<DatasourcesConfigRequest> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<DatasourcesConfigRequest> datasources) {
        this.datasources = datasources;
    }
}
