/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.internalservlet;

import com.agiletec.aps.system.exception.ApsSystemException;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author own_strong
 */
public class BpmCaseInstanceFileAction extends BpmCaseInstanceActionBase {

    private static final Logger logger = LoggerFactory.getLogger(BpmCaseInstanceFileAction.class);
    private String data;
    private String dataId;
    private String casefile;

    public String view() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            this.setFrontEndCaseData(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);
            KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
            if (null == config) {
                logger.warn("Null configuration");
                this.setErrorCode(ERROR_NULL_CONFIG);
                return SUCCESS;
            }
            this.setCasefile(this.getCaseManager().getCaseFile(config, this.getContainerid(), this.getCasePath()).toString());
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String updateData() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            this.setFrontEndCaseData(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);

            if (this.getChannelPath().equalsIgnoreCase(this.getChannel())) {

                KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
                caseManager.postCaseFile(config, this.getContainerid(), this.getCasePath(), this.getData());
                this.setCasefile(caseManager.getCaseFile(config, this.getContainerid(), this.getCasePath()).toString());
            }
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String deleteData() {
        try {
            String frontEndCaseDataIn = extractWidgetConfig("frontEndCaseData");
            this.setFrontEndCaseData(frontEndCaseDataIn);
            String channelIn = extractWidgetConfig("channel");
            this.setChannel(channelIn);

            if (this.getChannelPath().equalsIgnoreCase(this.getChannel())) {

                KieBpmConfig config = formManager.getKieServerConfigurations().get(this.getKnowledgeSourceId());
                caseManager.deleteCaseFile(config, this.getContainerid(), this.getCasePath(), this.getDataId());
                this.setCasefile(caseManager.getCaseFile(config, this.getContainerid(), this.getCasePath()).toString());
            }
        } catch (ApsSystemException t) {
            logger.error("Error getting the configuration parameter", t);
            return FAILURE;
        }
        return SUCCESS;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getCasefile() {
        return casefile;
    }

    public void setCasefile(String casefile) {
        this.casefile = casefile;
    }
}
