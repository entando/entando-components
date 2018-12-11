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
