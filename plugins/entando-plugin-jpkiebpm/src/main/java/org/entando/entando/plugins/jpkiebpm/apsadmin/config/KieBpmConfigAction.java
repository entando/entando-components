/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpkiebpm.apsadmin.config;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;
import java.util.HashMap;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class KieBpmConfigAction extends BaseAction {

    private static final Logger _logger = LoggerFactory.getLogger(KieBpmConfigAction.class);

    public String add() {
        try {
            KieBpmConfig config = this.getFormManager().getConfig();
            this.configToModel(config);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "add");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String edit() {
        try {
            KieBpmConfig config = this.modelToConfig();
            this.getFormManager().setKieServerConfiguration(config.getId());
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "FAILURE");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String list() {
        try {
            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getCaseManager().getKieServerStasus().toString());

        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "list");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String save() {
        try {

            KieBpmConfig config = this.modelToConfig();
            this.getFormManager().addConfig(config);

            this.addActionMessage(this.getText("message.config.savedConfirm"));
            try {
                this.getFormManager().getContainersList();
                this.addActionMessage(this.getText("message.config.test.success"));
            } catch (ApsSystemException e) {
                _logger.error("Configuration test failed!", e);
                this.addActionError(this.getText("message.config.test.fail"));
            }

            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getCaseManager().getKieServerStasus().toString());

        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "save");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String test() {
        try {
            // enable plugin by default;
            this.setActive(true);
            KieBpmConfig config = this.modelToConfig();

            this.getFormManager().setConfig(config);
            try {
                this.getFormManager().getContainersList();
                this.addActionMessage(this.getText("message.config.test.success"));
            } catch (ApsSystemException e) {
                _logger.error("Configuration test failed!", e);
                this.addActionError(this.getText("message.config.test.fail"));
            }

            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getCaseManager().getKieServerStasus().toString());

        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "test");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String testall() {
        try {

            JSONArray output = new JSONArray();

            HashMap<String, KieBpmConfig> ServerConfigurations = this.getCaseManager().getKieServerConfigurations();

            for (String key : ServerConfigurations.keySet()) {

                this.getCaseManager().setKieBpmConfig(ServerConfigurations.get(key));

                JSONObject serverJS = new JSONObject();

                serverJS.put("kie-server-id", key);

                try {
                    this.getCaseManager().getContainersList();
                    serverJS.put("passed", true);

                } catch (Throwable t) {
                    _logger.error("Configuration test failed!", t);
                    serverJS.put("passed", false);
                }

                output.put(serverJS);
            }
            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getCaseManager().getKieServerStasus().toString());

            this.addActionMessage(this.getText("message.config.test.success"));
            this.setKnowledgeSourceTestAllResult(output.toString());
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "testall");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String delete() {
        try {

            this.getFormManager().deleteConfig(this.getId());

            this.addActionMessage("Configuration Deleted");

            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getCaseManager().getKieServerStasus().toString());

        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "delete");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String addConf() {
        return SUCCESS;
    }

    protected void configToModel(KieBpmConfig config) {
        this.setActive(config.getActive());
        this.setId(config.getId());
        this.setName(config.getName());
        this.setUserName(config.getUsername());
        this.setPassword(config.getPassword());
        this.setHostName(config.getHostname());
        this.setSchema(config.getSchema());
        this.setPort(config.getPort());
        this.setWebappName(config.getWebapp());
        this.setTimeout(config.getTimeoutMsec());
        this.setDebug(config.getDebug());
    }

    protected KieBpmConfig modelToConfig() {
        KieBpmConfig config = new KieBpmConfig();
        config.setActive(this.getActive());
        config.setId(this.getId());
        config.setName(this.getName());
        config.setUsername(this.getUserName());
        config.setPassword(this.getPassword());
        config.setHostname(this.getHostName());
        config.setSchema(this.getSchema());
        config.setPort(this.getPort());
        config.setWebapp(this.getWebappName());
        config.setTimeoutMsec(this.getTimeout());
        config.setDebug(this.getDebug());
        return config;
    }

    public IKieFormManager getFormManager() {
        return _formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this._formManager = formManager;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    public Boolean getActive() {
        return _active;
    }

    public void setActive(Boolean active) {
        this._active = active;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        this._userName = userName;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getHostName() {
        return _hostName;
    }

    public void setHostName(String hostName) {
        this._hostName = hostName;
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

    public String getWebappName() {
        return _webappName;
    }

    public void setWebappName(String webappName) {
        this._webappName = webappName;
    }

    public Integer getTimeout() {
        return _timeout;
    }

    public void setTimeout(Integer _timeout) {
        this._timeout = _timeout;
    }

    public Boolean getDebug() {
        return _debug;
    }

    public void setDebug(Boolean debug) {
        this._debug = debug;
    }

    public HashMap<String, KieBpmConfig> getKnowledgeSource() {
        return knowledgeSource;
    }

    public void setKnowledgeSource(HashMap<String, KieBpmConfig> knowledgeSource) {
        this.knowledgeSource = knowledgeSource;
    }

    public String getKnowledgeSourceStatus() {
        return knowledgeSourceStatus;
    }

    public void setKnowledgeSourceStatus(String knowledgeSourceStatus) {
        this.knowledgeSourceStatus = knowledgeSourceStatus;
    }

    public String getKnowledgeSourceTestAllResult() {
        return knowledgeSourceTestAllResult;
    }

    public void setKnowledgeSourceTestAllResult(String knowledgeSourceTestAllResult) {
        this.knowledgeSourceTestAllResult = knowledgeSourceTestAllResult;
    }

    private IKieFormManager _formManager;
    private CaseManager caseManager;

    private Boolean _active;
    private String _id;
    private String _name;
    private String _userName;
    private String _password;
    private String _hostName;
    private String _schema;
    private Integer _port;
    private String _webappName;
    private Integer _timeout;
    private Boolean _debug;

    private HashMap<String, KieBpmConfig> knowledgeSource;
    private String knowledgeSourceStatus;
    private String knowledgeSourceTestAllResult;
;
}
