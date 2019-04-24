/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpServerErrorException;
import java.net.SocketException;

public class BpmSourceAndProcessSelectorHelper<A extends ActionSupport & Preparable & BpmSourceAndProcessSelector<T>, T> {

    private static final Logger logger = LoggerFactory.getLogger(BpmSourceAndProcessSelectorHelper.class);

    private final A action;

    public BpmSourceAndProcessSelectorHelper(A action) throws ApsSystemException {
        this.action = action;
        loadSources();
    }

    private void loadSources() throws ApsSystemException {
        action.setKnowledgeSource(action.getFormManager().getKieServerConfigurations());
    }

    public boolean loadProcesses() throws ApsSystemException {

        KieBpmConfig config = getSourceConfigIfValid();

        try {
            if (config != null) {
                action.loadProcesses(config);
                return true;
            }
        } catch (ApsSystemException ex) {

            Throwable cause = ex.getCause();

            if (cause != null
                    && (cause instanceof HttpServerErrorException
                    || cause instanceof SocketException)) {

                logger.warn("Error while loading processes: {}", cause.getClass().getCanonicalName());
                action.addActionError(action.getText("kie.server.fail", new String[]{config.getName()}));
                unsetAll();

            } else {
                throw ex;
            }
        }

        return false;
    }

    private KieBpmConfig getSourceConfigIfValid() throws ApsSystemException {

        KieBpmConfig config = action.getKnowledgeSource().get(action.getKnowledgeSourcePath());

        if (config == null) {
            logger.warn("Null KieBpmConfig - Check the configuration");
            action.addActionError(action.getText("kie.config.notFound"));
            unsetAll();
        } else if (!config.getActive()) {
            action.addActionError(action.getText("kie.config.disabled", new String[]{config.getName()}));
            unsetAll();
            return null;
        }
        return config;
    }

    protected void unsetAll() {
        action.setKnowledgeSourcePath(null);
        action.setProcessPath(null);
        action.setProcess(null);
    }
}
