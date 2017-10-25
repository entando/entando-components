/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Entando
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class KieBpmConfig {


    @Override
    public KieBpmConfig clone() {
        KieBpmConfig cfg = new KieBpmConfig();

        cfg.setActive(this.getActive());
        cfg.setHostname(this.getHostname());
        cfg.setPassword(this.getPassword());
        cfg.setPort(this.getPort());
        cfg.setSchema(this.getSchema());
        cfg.setUsername(this.getUsername());
        cfg.setWebapp(this.getWebapp());

        return cfg;
    }

    public Boolean getActive() {
        return _active;
    }

    public void setActive(Boolean active) {
        this._active = active;
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

    public String getHostname() {
        return _hostname;
    }

    public void setHostname(String hostname) {
        this._hostname = hostname;
    }

    public String getSchema() {
        return _schema;
    }

    public void setSchema(String schema) {
        this._schema = schema;
    }

    public Integer getPort() {
        return _port;
    }

    public void setPort(Integer port) {
        this._port = port;
    }

    public String getWebapp() {
        return _webapp;
    }

    public void setWebapp(String webapp) {
        this._webapp = webapp;
    }

    @XmlElement(name="active")
    private Boolean _active;
    @XmlElement(name="username")
    private String _username;
    @XmlElement(name="password")
    private String _password;
    @XmlElement(name="hostname")
    private String _hostname;
    @XmlElement(name="schema")
    private String _schema;
    @XmlElement(name="port")
    private Integer _port;
    @XmlElement(name="webapp")
    private String _webapp;
}
