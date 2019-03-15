package org.entando.entando.aps.system.services.digitalexchange.job;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.function.Consumer;

import javax.servlet.ServletContext;

import com.agiletec.aps.util.ApsWebApplicationUtils;
import org.entando.entando.aps.system.init.DatabaseManager;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.aps.system.services.digitalexchange.client.DigitalExchangesClient;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

public abstract class DigitalExchangeAbstractJobExecutor implements DigitalExchangeJobExecutor<Void>, ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DigitalExchangeAbstractJobExecutor.class);

    protected static final String RESOURCES_DIR = "resources";

    protected final DigitalExchangesClient client;
    protected final ComponentStorageManager storageManager;
    protected final DatabaseManager databaseManager;
    protected final InitializerManager initializerManager;
    protected final CommandExecutor commandExecutor;

    private ServletContext servletContext;

    @Autowired
    public DigitalExchangeAbstractJobExecutor(DigitalExchangesClient client, ComponentStorageManager storageManager,
            DatabaseManager databaseManager, InitializerManager initializerManager,
            CommandExecutor commandExecutor) {
        this.client = client;
        this.storageManager = storageManager;
        this.databaseManager = databaseManager;
        this.initializerManager = initializerManager;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    protected ComponentDescriptor parseComponentDescriptor(String componentCode) {

        String componentDefinitionPath = componentCode + File.separator + "component.xml";

        try {
            if (!storageManager.existsProtected(componentDefinitionPath)) {
                throw new JobExecutionException("component.xml not found for " + componentCode);
            }

            try (InputStream in = storageManager.getProtectedStream(componentDefinitionPath)) {
                Document document = new SAXBuilder().build(in);
                return new ComponentDescriptor(document.getRootElement(), storageManager);
            }
        } catch (Throwable t) {
            logger.error("Error during component defintion parsing", t);
            throw new JobExecutionException("Unable to parse component.xml for " + componentCode);
        }
    }

    protected void completeJob(DigitalExchangeJob job, Consumer<DigitalExchangeJob> consumer) {

        reloadSystem();
        job.setProgress(1);
        job.setStatus(JobStatus.COMPLETED);
        job.setEnded(new Date());
        consumer.accept(job);
    }

    protected void reloadSystem() {
        try {
            ApsWebApplicationUtils.executeSystemRefresh(servletContext);
        } catch (Throwable t) {
            logger.error("Error during system reloading", t);
            throw new JobExecutionException("Unable to reload the system after the installation");
        }
    }

    @Override
    public void execute(DigitalExchangeJob job, Consumer<DigitalExchangeJob> consumer, Void additionalParam) {
        execute(job, consumer);
    }

    public abstract void execute(DigitalExchangeJob job, Consumer<DigitalExchangeJob> consumer);
}
