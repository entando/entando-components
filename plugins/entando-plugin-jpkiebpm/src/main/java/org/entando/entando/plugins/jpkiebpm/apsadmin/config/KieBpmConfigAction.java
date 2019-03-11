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

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.CaseManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.IKieFormManager;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class KieBpmConfigAction extends BaseAction {

    private static final Logger logger = LoggerFactory.getLogger(KieBpmConfigAction.class);

    private IKieFormManager formManager;
    private CaseManager caseManager;

    private Boolean active;
    private String id;
    private String name;
    private String userName;
    private String password;
    private String hostName;
    private String schema;
    private Integer port;
    private String webappName;
    private Integer timeout;
    private Boolean debug;

    private Map<String, KieBpmConfig> knowledgeSource;
    private String knowledgeSourceStatus;
    private String knowledgeSourceTestAllResult;

    public String add() {

//        //TODO -- JPW
//        try {
//            KieBpmConfig config = this.getFormManager().get();
//            this.configToModel(config);
//        } catch (Throwable t) {
//            ApsSystemUtils.logThrowable(t, this, "add");
//            return FAILURE;
//        }
        return SUCCESS;
    }

    public String edit() {
        //TODO -- JPW
        try {
            KieBpmConfig config = this.modelToConfig();
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "FAILURE");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String list() {
        try {
            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getFormManager().getKieServerStatus().toString());
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
                this.getFormManager().getContainersList(config);
                this.addActionMessage(this.getText("message.config.test.success"));
            } catch (ApsSystemException e) {
                logger.error("Configuration test failed!", e);
                this.addActionError(this.getText("message.config.test.fail"));
            }
            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());

            JSONArray serverStat = this.getFormManager().getKieServerStatus();
            for(int i =0; i<serverStat.length(); i++) {
                Object obj = serverStat.get(i);
                if (obj instanceof JSONObject) {
                    JSONObject details = (JSONObject) obj;
                    String id = details.getString("id");
                    JSONObject conf = details.getJSONObject("config");
                    if (conf.has("version")) {
                        String ver = conf.getString("version");
                        this.getFormManager().getHostNameVersionMap().put(id, ver);
                    }
                }
            }

            this.setKnowledgeSourceStatus(serverStat.toString());
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

            try {
                this.getFormManager().getContainersList(config);
                this.addActionMessage(this.getText("message.config.test.success"));
            } catch (ApsSystemException e) {
                logger.error("Configuration test failed!", e);
                this.addActionError(this.getText("message.config.test.fail"));
            }
            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getFormManager().getKieServerStatus().toString());
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "test");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String testall() {
        try {
            //Save the current Config

            JSONArray output = new JSONArray();
            Map<String, KieBpmConfig> serverConfigurations = this.getFormManager().getKieServerConfigurations();
            for (String key : serverConfigurations.keySet()) {
                KieBpmConfig config = serverConfigurations.get(key);
                JSONObject serverJS = new JSONObject();
                serverJS.put("kie-server-id", key);
                try {
                    this.getFormManager().getContainersList(config);
                    serverJS.put("passed", true);

                } catch (ApsSystemException e) {
                    logger.error("Configuration test failed!", e);
                    serverJS.put("passed", false);
                }
                output.put(serverJS);
            }
            //load the current config
            this.setKnowledgeSource(this.getFormManager().getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.getFormManager().getKieServerStatus().toString());
            this.addActionMessage(this.getText("message.config.test.success"));
            this.setKnowledgeSourceTestAllResult(output.toString());
        } catch (ApsSystemException | JSONException t) {
            ApsSystemUtils.logThrowable(t, this, "testall");
            return FAILURE;
        }
        return SUCCESS;
    }

    public String delete() {
        try {
            this.getFormManager().deleteConfig(this.getId());
            this.addActionMessage("Configuration Deleted");
            this.setKnowledgeSource(this.formManager.getKieServerConfigurations());
            this.setKnowledgeSourceStatus(this.formManager.getKieServerStatus().toString());
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
        return formManager;
    }

    public void setFormManager(IKieFormManager formManager) {
        this.formManager = formManager;
    }

    public CaseManager getCaseManager() {
        return caseManager;
    }

    public void setCaseManager(CaseManager caseManager) {
        this.caseManager = caseManager;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getWebappName() {
        return webappName;
    }

    public void setWebappName(String webappName) {
        this.webappName = webappName;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Boolean getDebug() {
        return debug;
    }

    public void setDebug(Boolean debug) {
        this.debug = debug;
    }

    public Map<String, KieBpmConfig> getKnowledgeSource() {
        return knowledgeSource;
    }

    public void setKnowledgeSource(Map<String, KieBpmConfig> knowledgeSource) {
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


}
