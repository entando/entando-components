
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

package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;


import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;

import java.util.ArrayList;
import java.util.List;

public class DashboardConfig {

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getServerDescription() {
        return _serverDescription;
    }

    public void setServerDescription(String serverDescription) {
        this._serverDescription = serverDescription;
    }

    public String getServerURI() {
        return _serverURI;
    }

    public void setServerURI(String serverURI) {
        this._serverURI = serverURI;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getToken() {
        return _token;
    }

    public void setToken(String token) {
        this._token = token;
    }

    public int getTimeConnection() {
        return _timeConnection;
    }

    public void setTimeConnection(int timeConnection) {
        this._timeConnection = timeConnection;
    }

    public boolean getActive() {
        return _active;
    }

    public void setActive(boolean active) {
        this._active = active;
    }

    public boolean getDebug() {
        return _debug;
    }

    public void setDebug(boolean debug) {
        this._debug = debug;
    }

    public String getType() {
        return _type;
    }

    public DashboardConfig setType(String type) {
        this._type = type;
        return this;
    }


    public List<DatasourcesConfigDto> getDatasources() {
        if (datasources == null) {
            datasources = new ArrayList<>();
        }
        return datasources;
    }

    public void setDatasources(final List<DatasourcesConfigDto> datasources) {
        this.datasources = datasources;
    }

    private int _id;
    private String _serverDescription;
    private String _serverURI;
    private String _username;
    private String _password;
    private String _token;
    private String _type;
    private int _timeConnection;
    private boolean _active;
    private boolean _debug;
    private List<DatasourcesConfigDto> datasources;


}
